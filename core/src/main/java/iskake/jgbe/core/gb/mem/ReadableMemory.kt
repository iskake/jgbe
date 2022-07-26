package iskake.jgbe.core.gb.mem

/**
 * Interface for memory that can be read from (but not necessarily written to).
 *
 * @see WritableMemory
 */
fun interface ReadableMemory {
    /**
     * Read the byte at address `address` in memory.
     *
     * @param address The address of the byte to be read from.
     * @return The byte at the address of `address`.
     * @throws IndexOutOfBoundsException If `address` is outside of memory.
     */
    @Throws(IndexOutOfBoundsException::class)
    fun read(address: Int): Byte
}