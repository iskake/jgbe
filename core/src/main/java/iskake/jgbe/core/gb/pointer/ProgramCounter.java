package iskake.jgbe.core.gb.pointer;

import iskake.jgbe.core.gb.GameBoy;

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

    @Override
    public void set(short address) {
        gb.timing().incCycles();
        super.set(address);
    }

    public void setNoCycle(short address) {
        super.set(address);
    }
}
