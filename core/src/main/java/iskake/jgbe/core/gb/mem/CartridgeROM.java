package iskake.jgbe.core.gb.mem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Read Only Memory of 'Game Boy game pak', separated into multiple
 * {@code ROMBank}s.
 */
public class CartridgeROM implements ReadableMemory, WritableMemory {
    private static final Logger log = LoggerFactory.getLogger(CartridgeROM.class);
    private final ROMBank[] ROMBanks;
    private final MemoryBankController mbc;
    private final RAM[] RAMBanks;

    CartridgeROM(ROMBank[] romBanks, RAM[] ramBanks, MemoryBankController mbc) {
        this.ROMBanks = romBanks;
        this.RAMBanks = ramBanks;
        this.mbc = mbc;
    }

    /**
     * Get the ROM bank with index 0.
     * 
     * @return The correct ROM bank.
     */
    public ROMBank getROMBank0() {
        // ? Some MBCs can switch the first bank to other banks than bank0.
        return ROMBanks[0];
    }

    /**
     * Get the current switchable ROM bank, according to the MBC.
     * 
     * @return The correct ROM bank.
     */
    public ROMBank getROMBankX() {
        return ROMBanks[mbc.getROMBankIndex()];
    }

    /**
     * Get the current RAM bank, if any.
     * 
     * @return The current RAM bank. If there is none, then {@code null} is
     *         returned instead.
     */
    public RAM getRAMBank() {
        if (RAMBanks.length == 0)
            return null;

        return RAMBanks[mbc.getRAMBankIndex()];
    }

    @Override
    public void write(int address, byte value) throws IndexOutOfBoundsException {
        mbc.write(address, value);
    }

    @Override
    public byte read(int address) throws IndexOutOfBoundsException {
        if (address < 0x4000) {
            return getROMBank0().read(address);
        } else {
            address -= 0x4000;
            return getROMBankX().read(address);
        }
    }
}
