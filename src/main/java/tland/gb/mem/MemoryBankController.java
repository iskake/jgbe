package tland.gb.mem;

/**
 * Memory Bank Controller, controls the currently mapped ROM banks and RAM
 * banks.
 */
public abstract class MemoryBankController implements WritableMemory<Byte> {
    {
        // Initialize the switchable ROM bank to 1
        currSwitchableBank = 1;
    }
    protected int currSwitchableBank;

    /**
     * Get the index of the current switchable bank.
     * 
     * @return The correct index.
     */
    public int getSwitchableIndex() {
        return currSwitchableBank;
    }
}
