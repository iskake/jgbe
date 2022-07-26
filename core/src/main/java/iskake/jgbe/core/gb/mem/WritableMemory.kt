package iskake.jgbe.core.gb.mem

/**
 * Interface for memory that can be written to (but not necessarily read from).
 *
 * @see ReadableMemory
 */
fun interface WritableMemory {
    /**
     * Write `value` to the address `address` memory.
     *
     * @param address The address to be written to.
     * @param value   The value to write.
     * @throws IndexOutOfBoundsException If `address` is outside of memory.
     */
    @Throws(IndexOutOfBoundsException::class)
    fun write(address: Int, value: Byte)
}