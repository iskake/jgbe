package iskake.jgbe.core.gb.mem;

/**
 * Interface for memory that has two methods for reading memory:
 * <ul>
 *     <li>{@link #read} - a method for reads that can be 'restricted' in a way,
 *  *     should be used for most 'normal' operations.
 *     <br>Example: normal VRAM reads during mode 3 return 0xff.</li>
 *     <li>{@link #readUnrestricted} - a method for writes that do not have the above restrictions,
 *  *     should be used for 'special' operations.
 *     <br>Example: VRAM reads during rendering by the pixel FIFO.</li>
 * </ul>
 *
 * @see WritableMemoryUnrestricted
 * @see ReadableMemory
 */
public interface ReadableMemoryUnrestricted extends ReadableMemory {
    /**
     * Read the byte at address {@code address} in memory, without the restrictions
     * of the {@link #read} method.
     *
     * @param address The address of the byte to be read from.
     * @return The byte at the address of {@code address}.
     * @throws IndexOutOfBoundsException If {@code address} is outside of memory.
     */
    byte readUnrestricted(int address);
}
