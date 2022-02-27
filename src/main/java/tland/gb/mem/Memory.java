package tland.gb.mem;

/**
 * Interface for memory that is can be read and written to.
 * 
 * @see ReadableMemory
 */
public interface Memory extends ReadableMemory {
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
    public void writeShort(int address, short value) throws IndexOutOfBoundsException;
}
