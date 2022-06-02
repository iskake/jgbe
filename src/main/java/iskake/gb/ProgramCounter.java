package iskake.gb;

/**
 * Program counter, a pointer to a memory address.
 * <p>
 * The program counter points to the memory address of the next instruction to
 * be executed.
 */
public class ProgramCounter extends Pointer {
    private final GameBoy gb;

    public ProgramCounter(GameBoy gb, short address) {
        super(address);
        this.gb = gb;
    }

    /**
     * Initialize the program counter by setting the initial value specified in the
     * constructor.
     */
    public void init(short address) {
        ptr = address;
    }

    /**
     * Set the program counter to point at the specified address.
     * <p>
     * Used for jump instructions. (NOT {@code jp hl})
     * 
     * @param address The address to set the program counter to.
     */
    @Override
    public void set(short address) {
        super.set(address);
        gb.timing.incCycles();
    }

    /**
     * Set the program counter to point at the specified address.
     * <p>
     * Used for jump instructions. ({@code jp hl})
     * 
     * @param address The address to set the program counter to.
     */
    public void setNoCycle(short address) {
        super.set(address);
    }

}
