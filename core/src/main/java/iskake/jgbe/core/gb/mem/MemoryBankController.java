package iskake.jgbe.core.gb.mem;

/**
 * Memory Bank Controller, controls the currently mapped ROM banks and RAM
 * banks.
 */
public abstract class MemoryBankController implements WritableMemory {
    protected final int numROMBanks;
    protected final int numRAMBanks;
    protected int currROMBank = 1;
    protected int currRAMBank = 0;

    public MemoryBankController(int numROMBanks, int numRAMBanks) {
        this.numROMBanks = numROMBanks;
        this.numRAMBanks = numRAMBanks;
    }

    /**
     * Get the index of the current switchable ROM bank.
     * 
     * @return The correct ROM bank index.
     */
    public int getROMBankIndex() {
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
