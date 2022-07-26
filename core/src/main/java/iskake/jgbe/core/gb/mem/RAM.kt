package iskake.jgbe.core.gb.mem

import java.util.*

/**
 * Random Access Memory. Implementation of memory that can be both read and
 * written to.
 */
open class RAM(size: Int) : WritableMemory, ReadableMemory {
    val size = size.toUShort()
    protected var bytes: ByteArray = ByteArray(size)
    private val rand: Random = Random()

    @Throws(IndexOutOfBoundsException::class)
    override fun read(address: Int): Byte {
        return bytes[address]
    }

    @Throws(IndexOutOfBoundsException::class)
    override fun write(address: Int, value: Byte) {
        bytes[address] = value
    }

    /**
     * Clear the memory.
     * Each byte will have a value of $00.
     */
    fun clear() {
        Arrays.fill(bytes, 0.toByte())
    }

    /**
     * Randomize each byte in memory.
     * Each byte will have a value from $00 to $ff.
     */
    fun randomize() {
        rand.nextBytes(bytes)
    }
}