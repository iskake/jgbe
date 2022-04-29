package tland.emu.pointer;

import tland.Bitwise;
import tland.emu.IEmulator;

/**
 * Stack pointer, a pointer to a memory address.
 * <p>
 * 'Pushing' to the stack pointer will write a short to the memory address at
 * the stack pointer and increment the stack pointer by 2.
 * <p>
 * 'Popping' the stack pointer will return the short at the stack pointer and
 * decrement the stack pointer by 2.
 * 
 * @author Tarjei Landøy
 */
public class StackPointer extends Pointer {
    private final IEmulator emu;

    public StackPointer(IEmulator emu, short address) {
        super(address);
        this.emu = emu;
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
        emu.writeMemoryAddress(ptr, Bitwise.getHighByte(value));
        dec();
        emu.writeMemoryAddress(ptr, Bitwise.getLowByte(value));
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
        byte lo = emu.readMemoryAddress(ptr);
        inc();
        byte hi = emu.readMemoryAddress(ptr);
        inc();
        return Bitwise.toShort(hi, lo);
    }

}
