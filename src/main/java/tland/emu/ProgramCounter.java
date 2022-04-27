package tland.emu;

/**
 * Program counter, a pointer to a memory address.
 * <p>
 * The program counter points to the memory address of the next instruction to
 * be executed.
 */
public class ProgramCounter extends Pointer {

    public ProgramCounter(short address) {
        super(address);
    }
}
