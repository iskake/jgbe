package iskake.gb.pointer;

import iskake.gb.timing.Timing;

/**
 * Program counter, a pointer to a memory address.
 * <p>
 * The program counter points to the memory address of the next instruction to
 * be executed.
 */
public class ProgramCounter extends Pointer {

    private final Timing timing;

    public ProgramCounter(Timing timing, short address) {
        super(address);
        this.timing = timing;
    }

    @Override
    public void set(short address) {
        timing.incCycles();
        super.set(address);
    }

    public void setNoCycle(short address) {
        super.set(address);
    }
}
