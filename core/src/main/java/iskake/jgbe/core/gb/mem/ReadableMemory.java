package iskake.jgbe.core.gb.mem;

/**
 * Interface for memory that can be read from (but not necessarily written to).
 * 
 * @see WritableMemory
 */
public interface ReadableMemory {
    /**
     * Read the byte at address {@code address} in memory.
     * 
     * @param address The address of the byte to be read from.
     * @return The byte at the address of {@code address}.
     * @throws IndexOutOfBoundsException If {@code address} is outside of memory.
     */
    byte read(int address) throws IndexOutOfBoundsException;
}
