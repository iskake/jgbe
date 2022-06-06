package iskake.jgbe.core.gb.mem;

/**
 * Interface for memory that can be written to (but not necessarily read from).
 * 
 * @see ReadableMemory
 */
public interface WritableMemory {
    /**
     * Write {@code value} to the address {@code address} memory.
     * 
     * @param address The address to be written to.
     * @param value   The value to write.
     * @throws IndexOutOfBoundsException If {@code address} is outside of memory.
     */
    void writeByte(int address, byte value) throws IndexOutOfBoundsException;
}
