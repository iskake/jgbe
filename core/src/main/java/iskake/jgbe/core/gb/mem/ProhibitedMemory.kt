package iskake.jgbe.core.gb.mem

import iskake.jgbe.core.gb.HardwareRegisters
import iskake.jgbe.core.gb.HardwareRegisters.HardwareRegister

/**
 * 'Not usable' memory region. Writing does nothing, and reads have weird
 * behaviour.
 *
 *
 * According to Nintendo, use of this area is prohibited.
 */
class ProhibitedMemory(private val hwreg: HardwareRegisters) : WritableMemory, ReadableMemory {
    override fun read(address: Int): Byte {
        System.out.printf("Reading from unusable memory address: $%04x\n")
        return when (hwreg.read(HardwareRegister.STAT).toInt() and 3) {
            0, 1 -> 0.toByte()
            else -> 0xff.toByte()
        }
    }

    override fun write(address: Int, value: Byte) {
        System.out.printf("Attempting write to unusable memory address: $%04x\n", address)
    }
}