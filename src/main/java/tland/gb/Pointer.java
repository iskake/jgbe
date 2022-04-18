package tland.gb;

/**
 * Class holding a memory address in the range {@code $0000-$ffff}.
 * <p>
 * As an example, (in the Game Boy's memory map,) a pointer with the value
 * {@code $fffe} points to the last byte of HRAM.
 */
public abstract class Pointer {
    protected short ptr;

    /**
     * Create a new pointer. The specified address will be the address
     * 'pointed to' by the pointer.
     * 
     * @param address the address to point to.
     */
    public Pointer(short address) {
        this.ptr = address;
    }

    /**
     * Get the memory address currently pointed to by the pointer.
     * 
     * @return The address of the pointer.
     */
    public short get() {
        return ptr;
    }

    /**
     * Set the pointer to point at the the specified address.
     * 
     * @param address The address to set the pointer to.
     */
    public void set(short address) {
        this.ptr = address;
    }

    /**
     * Increment the pointer.
     * <p>
     * Equivalent to {@code ptr++}
     * 
     * @return The pointer before incrementing.
     */
    public short inc() {
        return ptr++;
    }

    /**
     * Decrement the pointer.
     * <p>
     * Equivalent to {@code ptr--}
     * 
     * @return The pointer before decrementing.
     */
    public short dec() {
        return ptr--;
    }
}
