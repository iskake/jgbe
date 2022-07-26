package iskake.jgbe.core.gb.mem

/**
 * Represents a range of bytes that are mapped to memory.
 */
class MappedByteRange(val startAddress: Int, val length: Int, private val getFunc: ReadableMemory, private val setFunc: WritableMemory) {
    /**
     * Get the value stored at the specified index in the mapped range.
     *
     * @param index The index (relative address) to get the byte from.
     * @return The value stored relative to the range's starting address.
     */
    operator fun get(index: Int): Byte {
        if (index > length) {
            throw IndexOutOfBoundsException("The specified index is larger than the mapped range's length.")
        }
        return getFunc.read(startAddress + index)
    }

    /**
     * Set the value at the specified index in the mapped range.
     *
     * @param index The index (relative address) to set the byte at.
     * @param value The value to write.
     */
    operator fun set(index: Int, value: Byte) {
        if (index > length) {
            throw IndexOutOfBoundsException("The specified index is larger than the mapped range's length.")
        }
        setFunc.write(startAddress + index, value)
    }
}