package tland.gb;

public class ProgramCounter {
    private short pc;
    private final short initialValue;
    private final GameBoy gb;

    /**
     * Stack pointer, simple pointer to a memory address.
     * 
     * @param address The address to start the program counter at.
     */
    public ProgramCounter(GameBoy gb, short address) {
        pc = address;
        initialValue = address;
        this.gb = gb;
    }

    /**
     * Initialize the program counter by setting the initial value specified in the
     * constructor.
     */
    public void init() {
        pc = initialValue;
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
     * Used for jump instructions. (NOT {@code jp hl})
     * 
     * @param address The address to set the program counter to.
     */
    public void set(short address) {
        gb.timing.incCycles();
        pc = address;
    }

    /**
     * Set the program counter to point at the specified address.
     * <p>
     * Used for jump instructions. ({@code jp hl})
     * 
     * @param address The address to set the program counter to.
     */
    public void setNoCycle(short address) {
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
