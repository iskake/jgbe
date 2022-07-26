package iskake.jgbe.core.gb.mem

import iskake.jgbe.core.Bitwise.intAsShort
import iskake.jgbe.core.gb.HardwareRegisters

/**
 * Memory map of the Game boy.
 *
 *
 * The memory map contains the current addressable memory, with a range of
 * $0000-$ffff ($10000 bytes)
 */
class MemoryMap(private val hwreg: HardwareRegisters, private val VRAM: VRAM, oam: OAM) : WritableMemory, ReadableMemory {
    private var rom: CartridgeROM? = null
    private val WRAM1: RAM = RAM(0x1000)
    private val WRAM2: RAM = RAM(0x1000)
    private val OAM: OAM = oam
    private val prohibited: ProhibitedMemory = ProhibitedMemory(hwreg)
    private val IO: HardwareRegisterMap = HardwareRegisterMap(hwreg)
    private val HRAM: RAM = RAM(0x200)
    private var fixedAddress = 0

    /**
     * Initialize the memory each region.
     */
    fun init(rom: CartridgeROM?) {
        this.rom = rom
        VRAM.clear()
        WRAM1.randomize()
        WRAM2.randomize()
        OAM.randomize()
        HRAM.randomize()
    }

    override fun read(address: Int): Byte {
        // TODO: DMA
        if (hwreg.isDMATransfer && intAsShort(address) < 0xff00.toShort()) {
            println(address)
            return 0xff.toByte()
        }
        val memory = getMemoryIndex(address) as ReadableMemory?
        return memory!!.read(fixedAddress)
    }

    override fun write(address: Int, value: Byte) {
        // TODO: DMA
        if (hwreg.isDMATransfer && intAsShort(address) < 0xff00.toShort()) {
            throw RuntimeException("DMA transfer")
        }
        val memory = getMemoryIndex(address) as WritableMemory?
        memory!!.write(fixedAddress, value)
    }

    /**
     * Get the 'correct memory' mapped based on the specified address.
     *
     *
     * Note that this will also set `fixedAddress` to the correct relative
     * address for indexing the mapped memory.
     *
     * @param address The address to get the memory from.
     * @return The correct memory at the specified address.
     */
    private fun getMemoryIndex(address: Int): Any? {
        var address = address
        address = intAsShort(address)
        fixedAddress = address
        return if (address in 0..0x7fff) {
            // 0000-3fff = 4000
            // ROM bank0-n, (note that MBC will control )
            rom
        } else if (address < 0xA000) {
            // 8000-9fff = 2000
            // VRAM
            fixedAddress -= 0x8000
            VRAM
        } else if (address < 0xC000) {
            // a000-bfff = 2000
            // Optional switchable bank from Cartridge
            fixedAddress -= 0xa000
            rom!!.ramBank
        } else if (address < 0xD000) {
            // c000-cfff = 1000
            fixedAddress -= 0xc000
            WRAM1
        } else if (address < 0xE000) {
            // d000-dfff = 1000
            fixedAddress -= 0xd000
            WRAM2
        } else if (address < 0xF000) {
            // e000-f000 = 1000
            // (Mirror of c000-ddff)
            // System.out.println("Accessing ECHO RAM.");
            fixedAddress -= 0xe000
            WRAM1
        } else if (address < 0xFE00) {
            // e000-fdff = e00
            // (Mirror of c000-ddff)
            // System.out.println("Accessing ECHO RAM.");
            fixedAddress -= 0xf000
            WRAM2
        } else if (address < 0xFEA0) {
            // fe00-fe9f = a0
            fixedAddress -= 0xfe00
            OAM
        } else if (address < 0xFF00) {
            // fea0-feff = 60
            // ('Not usable/prohibited' memory)
            prohibited
        } else if (address < 0xFF80 || address == 0xFFFF) {
            // ff00-ff7f = 80
            IO
        } else {
            // ff80-fffe (HRAM)
            fixedAddress -= 0xff80
            HRAM
        }
    }
}