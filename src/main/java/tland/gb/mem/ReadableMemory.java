package tland.gb.mem;

/**
 * Interface for memory that can be read from (but not necessarily writable).
 * 
 * @see Memory
 */
public interface ReadableMemory {
    // Note: there is no (practical) use for 'write-only memory' (in the context of
    // Game Boy memory, at least), so there is no need for a 'WritableMemory'
    // interface.

    /**
     * Read the byte at address {@code address} in memory.
     * 
     * @param address The address of the byte to be read from.
     * @return The byte at the address of {@code address}.
     * @throws IndexOutOfBoundsException If {@code address} is outside of memory.
     */
    public byte readByte(int address) throws IndexOutOfBoundsException;

    /**
     * Read the short (little-endian) starting at address {@code address} in memory.
     * 
     * @param address The address of the short to be read from.
     * @return The byte at the address of {@code address}.
     * @throws IndexOutOfBoundsException If {@code address} is outside of memory.
     */
    public short readShort(int address) throws IndexOutOfBoundsException;
}
