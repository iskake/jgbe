package iskake.jgbe.core.gb.mem

/**
 * Represents a byte that is mapped to a specific address in memory.
 */
class MappedByte(val address: Int, private val getFunc: ReadableMemory, private val setFunc: WritableMemory) {
    /**
     * Get the value of the mapped byte.
     *
     * @return The value stored at the mapped address.
     */
    fun get(): Byte {
        return getFunc.read(address)
    }

    /**
     * Set the value of the mapped byte.
     *
     * @param value The value to write to the byte at the mapped address.
     */
    fun set(value: Byte) {
        setFunc.write(address, value)
    }
}