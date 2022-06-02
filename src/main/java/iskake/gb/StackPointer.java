package iskake.gb;

import iskake.Bitwise;

/**
 * Stack pointer, a pointer to a memory address.
 * <p>
 * 'Pushing' to the stack pointer will write a short to the memory address at
 * the stack pointer and increment the stack pointer by 2.
 * <p>
 * 'Popping' the stack pointer will return the short at the stack pointer and
 * decrement the stack pointer by 2.
 */
public class StackPointer extends Pointer {
    private final GameBoy gb;

    public StackPointer(GameBoy gb, short address) {
        super(address);
        this.gb = gb;
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
        gb.writeMemoryAddress(ptr, Bitwise.getHighByte(value));
        dec();
        gb.writeMemoryAddress(ptr, Bitwise.getLowByte(value));
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
        byte lo = gb.readMemoryAddress(ptr);
        inc();
        byte hi = gb.readMemoryAddress(ptr);
        inc();
        return Bitwise.toShort(hi, lo);
    }

}
