package iskake.jgbe.core.gb.mem

import iskake.jgbe.core.Bitwise

/**
 * ROM bank of a 'Game Boy game pak'. Bank size is 16KiB (0x4000 bytes).
 */
class ROMBank(val bytes: ByteArray) : ReadableMemory {
    override fun read(address: Int): Byte {
        val addr = Bitwise.intAsShort(address)
        return bytes[addr]
    }

    companion object {
        const val BANK_SIZE = 0x4000
    }
}