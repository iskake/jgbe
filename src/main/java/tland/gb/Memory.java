package tland.gb;

/**
 * Interface for memory reading / writing.
 */
public interface Memory {
    /**
     * Read the byte at address {@code address} in memory.
     * 
     * @param address The address of the byte to be read from.
     * @return The byte at the address of {@code address}.
     * @throws IndexOutOfBoundsException If {@code address} is outside of memory.
     */
    public int readByte(int address) throws IndexOutOfBoundsException;

    /**
     * Write {@code value} to the byte at address {@code address} in memory.
     * 
     * @param address The address of the byte to be written to.
     * @param value   The value to write.
     * @throws IndexOutOfBoundsException If {@code address} is outside of memory.
     */
    public void writeByte(int address, int value) throws IndexOutOfBoundsException;

    /**
     * Read the short (little-endian) starting at address {@code address} in memory.
     * 
     * @param address The address of the short to be read from.
     * @return The byte at the address of {@code address}.
     * @throws IndexOutOfBoundsException If {@code address} is outside of memory.
     */
    public int readShort(int address) throws IndexOutOfBoundsException;

    /**
     * Write {@code value} to the short (little-endian) starting at address
     * {@code address} in memory.
     * 
     * @param address The address of the short to be written to.
     * @param value   The value to write.
     * @throws IndexOutOfBoundsException If {@code address} is outside of memory.
     */
    public void writeShort(int address, int value) throws IndexOutOfBoundsException;
}
