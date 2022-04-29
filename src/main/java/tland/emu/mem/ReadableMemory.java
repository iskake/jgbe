package tland.emu.mem;

/**
 * Interface for memory that can be read from (but not necessarily written to).
 * 
 * @see WritableMemory
 * 
 * @author Tarjei Landøy
 */
public interface ReadableMemory<T> extends Memory<T> {
    /**
     * Read the T at address {@code address} in memory.
     * 
     * @param address The address of the element to read.
     * @return The element at the address of {@code address}.
     * @throws IndexOutOfBoundsException If {@code address} is outside of memory.
     */
    T readAddress(int address) throws IndexOutOfBoundsException;
}
