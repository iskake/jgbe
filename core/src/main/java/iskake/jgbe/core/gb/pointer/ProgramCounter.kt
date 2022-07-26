package iskake.jgbe.core.gb.pointer

import iskake.jgbe.core.gb.GameBoy

/**
 * Program counter, a pointer to a memory address.
 *
 *
 * The program counter points to the memory address of the next instruction to
 * be executed.
 */
class ProgramCounter(private val gb: GameBoy, address: Short) : Pointer(address) {
    override fun set(address: Short) {
        gb.timing.incCycles()
        super.set(address)
    }

    fun setNoCycle(address: Short) {
        super.set(address)
    }
}