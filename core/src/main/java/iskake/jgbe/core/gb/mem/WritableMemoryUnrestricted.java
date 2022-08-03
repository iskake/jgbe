package iskake.jgbe.core.gb.mem;

/**
 * Interface for memory that has two methods for writing memory:
 * <ul>
 *     <li>{@link #write} - a method for writes that can be 'restricted' in a way,
 *     should be used for most 'normal' operations.
 *     <br>Example: normal VRAM writes during mode 3 do nothing.</li>
 *     <li>{@link #writeUnrestricted} - a method for writes that do not have the above restrictions,
 *     should be used for 'special' operations.
 *     <br>Example: OAM writes under DMA.</li>
 * </ul>
 *
 * @see ReadableMemoryUnrestricted
 * @see WritableMemory
 */
public interface WritableMemoryUnrestricted extends WritableMemory {
    /**
     * Write {@code value} to the address {@code address} memory, without the restrictions
     * of the {@link #write} method.
     *
     * @param address The address to be written to.
     * @param value   The value to write.
     * @throws IndexOutOfBoundsException If {@code address} is outside of memory.
     */
    void writeUnrestricted(int address, byte value);
}
