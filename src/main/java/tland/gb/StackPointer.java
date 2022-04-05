package tland.gb;

import tland.Bitwise;

/**
 * Stack pointer, a pointer to a memory address.
 * <p>
 * 'Pushing' to the stack pointer will write a short to the memory address at
 * the stack pointer and increment the stack pointer by 2.
 * <p>
 * 'Popping' the stack pointer will return the short at the stack pointer and
 * decrement the stack pointer by 2.
 */
public class StackPointer {
    private short sp;
    private final GameBoy gb;

    public StackPointer(GameBoy gb, short address) {
        this.gb = gb;
        sp = address;
    }

    /**
     * Get the memory address currently pointed to by the stack pointer.
     * 
     * @return The address of the stack pointer.
     */
    public short get() {
        return sp;
    }

    /**
     * Set the stack pointer to point at the the specified address.
     * 
     * @param address The address to set the stack pointer to.
     */
    public void set(short address) {
        sp = address;
    }

    /**
     * Decrement the stack pointer by one.
     */
    public void dec() {
        sp--;
    }

    /**
     * Increment the stack pointer by one.
     */
    public void inc() {
        sp++;
    }

    /**
     * 'Push' the value {@code value} onto the stack.
     * <p>
     * Writes the value to the memory location pointed to by the stack pointer and
     * decrements the stack pointer.
     * 
     * @param value The value to push onto the stack.
     */
    public void push(short value) {
        dec();
        gb.writeMemoryAddress(sp, Bitwise.getHighByte(value));
        dec();
        gb.writeMemoryAddress(sp, Bitwise.getLowByte(value));
    }

    /**
     * 'Pop' the value at the stack pointer from the stack.
     * <p>
     * Gets the value at the memory location pointed to by the stack pointer and
     * increments the stack pointer.
     * 
     * @return The value 'popped' off the stack.
     */
    public short pop() {
        byte lo = gb.readMemoryAddress(sp);
        inc();
        byte hi = gb.readMemoryAddress(sp);
        inc();
        return Bitwise.toShort(hi, lo);
    }

}
