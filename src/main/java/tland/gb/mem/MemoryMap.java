package tland.gb.mem;

import tland.Bitwise;
import tland.gb.HardwareRegisters;

/**
 * Memory map of the Game boy.
 * <p>
 * The memory map contains the current addressable memory, with a range of
 * $0000-$ffff ($10000 bytes)
 */
public class MemoryMap implements WritableMemory, ReadableMemory {
    private CartridgeROM rom;
    private final HardwareRegisters hwreg;
    private final VRAM VRAM;
    private final RAM WRAM1;
    private final RAM WRAM2;
    private final OAM OAM;
    private final ProhibitedMemory prohibited;
    private final HardwareRegisterMap IO;
    private final RAM HRAM;

    private int fixedAddress;


    public MemoryMap(HardwareRegisters hwreg, VRAM vram, OAM oam) {
        this.hwreg = hwreg;
        VRAM = vram;
        WRAM1 = new RAM(0x1000);
        WRAM2 = new RAM(0x1000);
        OAM = oam;
        IO = new HardwareRegisterMap(hwreg);
        prohibited = new ProhibitedMemory(hwreg);
        HRAM = new RAM(0x200);
    }

    /**
     * Initialize the memory each region.
     */
    public void init(CartridgeROM rom) {
        this.rom = rom;
        VRAM.clear();
        WRAM1.randomize();
        WRAM2.randomize();
        OAM.randomize();
        HRAM.randomize();
    }

    @Override
    public byte readByte(int address) {
        // TODO: DMA
        if (hwreg.isDMATransfer() && Bitwise.intAsShort(address) < (short)0xff00) {
            System.out.println(address);
            return (byte) 0xff;
        }
        ReadableMemory memory = (ReadableMemory) getMemoryIndex(address);
        return memory.readByte(fixedAddress);
    }

    @Override
    public void writeByte(int address, byte value) {
        // TODO: DMA
        if (hwreg.isDMATransfer() && Bitwise.intAsShort(address) < (short)0xff00) {
            throw new RuntimeException("DMA transfer");
        }
        WritableMemory memory = (WritableMemory) getMemoryIndex(address);
        memory.writeByte(fixedAddress, value);
    }

    /**
     * Get the 'correct memory' mapped based on the specified address.
     * <p>
     * Note that this will also set {@code fixedAddress} to the correct relative
     * address for indexing the mapped memory.
     * 
     * @param address The address to get the memory from.
     * @return The correct memory at the specified address.
     */
    private Object getMemoryIndex(int address) {
        address = Bitwise.intAsShort(address);
        fixedAddress = address;
        if (address >= 0 && address < 0x8000) {
            // 0000-3fff = 4000
            // ROM bank0-n, (note that MBC will control )
            return rom;
        } else if (address < 0xA000) {
            // 8000-9fff = 2000
            // VRAM
            fixedAddress -= 0x8000;
            return VRAM;
        } else if (address < 0xC000) {
            // a000-bfff = 2000
            // Optional switchable bank from Cartridge
            fixedAddress -= 0xa000;
            return rom.getRAMBank();
        } else if (address < 0xD000) {
            // c000-cfff = 1000
            fixedAddress -= 0xc000;
            return WRAM1;
        } else if (address < 0xE000) {
            // d000-dfff = 1000
            fixedAddress -= 0xd000;
            return WRAM2;
        } else if (address < 0xF000) {
            // e000-f000 = 1000
            // (Mirror of c000-ddff)
            // System.out.println("Accessing ECHO RAM.");
            fixedAddress -= 0xe000;
            return WRAM1;
        } else if (address < 0xFE00) {
            // e000-fdff = e00
            // (Mirror of c000-ddff)
            // System.out.println("Accessing ECHO RAM.");
            fixedAddress -= 0xf000;
            return WRAM2;
        } else if (address < 0xFEA0) {
            // fe00-fe9f = a0
            fixedAddress -= 0xfe00;
            return OAM;
        } else if (address < 0xFF00) {
            // fea0-feff = 60
            // ('Not usable/prohibited' memory)
            return prohibited;
        } else if (address < 0xFF80 || address == 0xFFFF) {
            // ff00-ff7f = 80
            return IO;
        } else {
            // ff80-fffe (HRAM)
            fixedAddress -= 0xff80;
            return HRAM;
        }
    }
}
