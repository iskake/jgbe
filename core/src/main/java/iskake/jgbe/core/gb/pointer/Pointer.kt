package iskake.jgbe.core.gb.pointer

/**
 * Class holding a memory address in the range `$0000-$ffff`.
 *
 *
 * As an example, (in the GameBoy's memory map,) a pointer with the value
 * `$fffe` points to the last byte of HRAM.
 */
abstract class Pointer(protected var ptr: Short) {
    /**
     * Get the memory address currently pointed to by the pointer.
     *
     * @return The address of the pointer.
     */
    fun get(): Short {
        return ptr
    }

    /**
     * Set the pointer to point at the the specified address.
     *
     * @param address The address to set the pointer to.
     */
    open fun set(address: Short) {
        ptr = address
    }

    /**
     * Increment the pointer.
     *
     *
     * Equivalent to `ptr++`
     *
     * @return The pointer before incrementing.
     */
    fun inc(): Short {
        return ptr++
    }

    /**
     * Decrement the pointer.
     *
     *
     * Equivalent to `ptr--`
     *
     * @return The pointer before decrementing.
     */
    fun dec(): Short {
        return ptr--
    }
}