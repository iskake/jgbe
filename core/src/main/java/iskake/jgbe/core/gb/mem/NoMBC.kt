package iskake.jgbe.core.gb.mem

/**
 * 'No Memory Bank Controller'.
 * All memory writes will not do anything.
 */
class NoMBC : MemoryBankController() {
    @Throws(IndexOutOfBoundsException::class)
    override fun write(address: Int, value: Byte) {
    }
}