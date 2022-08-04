package iskake.jgbe.core.gb.mem.mbc;

import iskake.jgbe.core.gb.mem.WritableMemory;

/**
 * Memory Bank Controller, controls the currently mapped ROM banks and RAM
 * banks.
 */
public abstract class MemoryBankController implements WritableMemory {
    public final boolean battery;
    protected final int numROMBanks;
    protected final int numRAMBanks;
    protected int currROMBank = 1;
    protected int currRAMBank = 0;
    protected boolean ramEnable = false;

    public MemoryBankController(int numROMBanks, int numRAMBanks, boolean battery) {
        this.battery = battery;
        this.numROMBanks = numROMBanks;
        this.numRAMBanks = numRAMBanks;
    }

    public void init() {
        currROMBank = 1;
        currRAMBank = 0;
        ramEnable = false;
    }

    public boolean isRAMEnabled() {
        return ramEnable;
    }

    /**
     * Get the index of the ROM bank that should (in most cases) be bank number 0
     *
     * @return 0, unless overridden.
     */
    public int getROMBank0Index() {
        return 0;
    }

    /**
     * Get the index of the current switchable ROM bank.
     *
     * @return The correct ROM bank index.
     */
    public int getROMBankNIndex() {
        return currROMBank;
    }

    /**
     * Get the index of the current switchable RAM bank.
     *
     * @return The correct RAM bank index.
     */
    public int getRAMBankIndex() {
        return currRAMBank;
    }
}
