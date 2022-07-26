package iskake.jgbe.core.gb.mem

import iskake.jgbe.core.gb.HardwareRegisters
import iskake.jgbe.core.gb.HardwareRegisters.HardwareRegister

/**
 * Memory map of hardware registers.
 */
class HardwareRegisterMap(private val hwregisters: HardwareRegisters) : ReadableMemory, WritableMemory {
    @Throws(IndexOutOfBoundsException::class)
    override fun write(address: Int, value: Byte) {
        val hwreg = HardwareRegister.getRegisterFromAddress(address)
        if (!hwregisters.write(hwreg, value)) {
            System.out.printf("Writing to invalid hw register: %04x\n", address)
        }
    }

    @Throws(IndexOutOfBoundsException::class)
    override fun read(address: Int): Byte {
        val hwreg = HardwareRegister.getRegisterFromAddress(address)
        return hwregisters.read(hwreg)
    }
}