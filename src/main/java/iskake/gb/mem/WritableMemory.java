package iskake.gb.mem;

/**
 * Interface for memory that can be written to (but not necessarily read from).
 * 
 * @see ReadableMemory
 */
public interface WritableMemory {
    /**
     * Write {@code value} to the byte at address {@code address} in memory.
     * 
     * @param address The address of the byte to be written to.
     * @param value   The value to write.
     * @throws IndexOutOfBoundsException If {@code address} is outside of memory.
     */
    void writeByte(int address, byte value) throws IndexOutOfBoundsException;
}
