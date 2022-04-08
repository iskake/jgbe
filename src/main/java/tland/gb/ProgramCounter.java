package tland.gb;

public class ProgramCounter {
    private short pc;

    /**
     * Stack pointer, simple pointer to a memory address.
     * 
     * @param address The address to start the program counter at.
     */
    public ProgramCounter(short address) {
        pc = address;
    }

    /**
     * Get the memory address currently pointed to by the program counter.
     * <p>
     * Used for jump instructions.
     * 
     * @return The address of the program counter.
     */
    public short get() {
        return pc;
    }

    /**
     * Set the program counter to point at the specified address.
     * <p>
     * Used for jump instructions.
     */
    public void set(short address) {
        pc = address;
    }

    /**
     * Get the current stack pointer and increment.
     * <p>
     * Equivalent to {@code pc++}
     * 
     * @return The stack pointer before incrementing.
     */
    public short postIncrement() {
        return pc++;
    }

}
