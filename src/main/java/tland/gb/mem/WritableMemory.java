package tland.gb.mem;

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
    public void writeByte(int address, byte value) throws IndexOutOfBoundsException;

    /**
     * Write {@code value} to the short (little-endian) starting at address
     * {@code address} in memory.
     * 
     * @param address The address of the short to be written to.
     * @param value   The value to write.
     * @throws IndexOutOfBoundsException If {@code address} is outside of memory.
     */
    // TODO? Check if this is really needed (there aren't any 16-bit load
    // TODO? instructions that writes a short to memory)
    public void writeShort(int address, short value) throws IndexOutOfBoundsException;
}
