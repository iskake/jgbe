package iskake.jgbe.core.gb.mem

import iskake.jgbe.core.NotImplementedException
import iskake.jgbe.core.gb.ROMHeader

/**
 * Read Only Memory of 'Game Boy game pak', separated into multiple
 * `ROMBank`s.
 */
class CartridgeROM(bytes: ByteArray?) : ReadableMemory, WritableMemory {
    private val romBanks: Array<ROMBank?>
    private val mbc: MemoryBankController?
    private val ramBanks: Array<RAM?>

    init {
        var numROMBanks = ROMHeader.getROMBanksNum(bytes)
        if (numROMBanks == -1) {
            System.err.println("Invalid/unknown ROM bank size, assuming no extra banks...")
            numROMBanks = 2

            // ?Also possible fallback:
            // numROMBanks = (bytes.length / ROMBank.BANK_SIZE);
            // ?If this is used, should we try to assume the MBC type too?
        }
        romBanks = arrayOfNulls(numROMBanks)
        for (i in 0 until numROMBanks) {
            val offset = i * ROMBank.BANK_SIZE
            romBanks[i] = ROMBank((bytes!!).copyOfRange(offset, offset + ROMBank.BANK_SIZE))
        }
        val tmpMBC: MemoryBankController? = try {
            ROMHeader.getMBCType(romBanks[0])
        } catch (e: NotImplementedException) {
            System.err.println("Unimplemented/unknown MBC type, assuming no MBC...")
            NoMBC()
        } catch (e: IllegalArgumentException) {
            System.err.println("Unimplemented/unknown MBC type, assuming no MBC...")
            NoMBC()
        }
        mbc = tmpMBC
        var numRAMBanks = ROMHeader.getRAMBanksNum(romBanks[0])
        if (numRAMBanks == -1) {
            System.err.println("Invalid/unknown RAM bank size, assuming no external RAM...")
            numRAMBanks = 0
        }
        ramBanks = Array(numRAMBanks) { RAM(0x2000) }
    }// ? Some MBCs can switch the first bank to other banks than bank0.

    /**
     * Get the ROM bank with index 0.
     *
     * @return The correct ROM bank.
     */
    val rOMBank0: ROMBank?
        get() =// ? Some MBCs can switch the first bank to other banks than bank0.
            romBanks[0]

    /**
     * Get the current switchable ROM bank, according to the MBC.
     *
     * @return The correct ROM bank.
     */
    val romBankX: ROMBank?
        get() = romBanks[mbc!!.switchableIndex]// TODO: Test what reads to EXTRAM with no RAM bank returns.

    /**
     * Get the current RAM bank, if any.
     *
     * @return The current RAM bank. If there is none, then `null` is
     * returned instead.
     */
    val ramBank: RAM?
        get() = ramBanks[0] // TODO: Test what reads to EXTRAM with no RAM bank returns.

    @Throws(IndexOutOfBoundsException::class)
    override fun write(address: Int, value: Byte) {
        if (mbc == null) return
        mbc.write(address, value)
    }

    @Throws(IndexOutOfBoundsException::class)
    override fun read(address: Int): Byte {
        var addr = address
        return if (addr < 0x4000) {
            rOMBank0!!.read(addr)
        } else {
            addr -= 0x4000
            romBankX!!.read(addr)
        }
    }
}