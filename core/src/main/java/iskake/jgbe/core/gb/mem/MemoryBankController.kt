package iskake.jgbe.core.gb.mem

/**
 * Memory Bank Controller, controls the currently mapped ROM banks and RAM
 * banks.
 */
abstract class MemoryBankController : WritableMemory {
    /**
     * Get the index of the current switchable bank.
     *
     * @return The correct index.
     */
    var switchableIndex = 1
        protected set
}