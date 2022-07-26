package iskake.jgbe.core.gb.pointer

import iskake.jgbe.core.Bitwise
import iskake.jgbe.core.gb.IGameBoy

/**
 * Stack pointer, a pointer to a memory address.
 *
 *
 * 'Pushing' to the stack pointer will write a short to the memory address at
 * the stack pointer and increment the stack pointer by 2.
 *
 *
 * 'Popping' the stack pointer will return the short at the stack pointer and
 * decrement the stack pointer by 2.
 */
class StackPointer(private val gb: IGameBoy, address: Short) : Pointer(address) {
    /**
     * 'Push' the value `value` onto the stack.
     *
     *
     * Writes the value to the memory location pointed to by the stack pointer and
     * decrements the stack pointer.
     *
     * @param value The value to push onto the stack.
     */
    fun push(value: Short) {
        dec()
        gb.writeAddress(ptr, Bitwise.getHighByte(value))
        dec()
        gb.writeAddress(ptr, Bitwise.getLowByte(value))
    }

    /**
     * 'Pop' the value at the stack pointer from the stack.
     *
     *
     * Gets the value at the memory location pointed to by the stack pointer and
     * increments the stack pointer.
     *
     * @return The value 'popped' off the stack.
     */
    fun pop(): Short {
        val lo = gb.readAddress(ptr)
        inc()
        val hi = gb.readAddress(ptr)
        inc()
        return Bitwise.toShort(hi, lo)
    }
}