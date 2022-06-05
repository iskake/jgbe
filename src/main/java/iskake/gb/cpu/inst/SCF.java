package iskake.gb.cpu.inst;

import iskake.gb.IGameBoy;
import iskake.gb.Registers.Flags;

/**
 * Set Carry Flag, sets the carry flag ({@code Flags.C}) and
 * resets {@code Flags.N} and {@code Flags.H}.
 * 
 * <p>
 * Implements opcode: {@code scf}.
 */
public class SCF extends Instruction {

    public SCF() {
        super("scf");
    }

    @Override
    public void doOp(IGameBoy gb, int opcode) {
        gb.reg().resetFlag(Flags.N);
        gb.reg().resetFlag(Flags.H);
        gb.reg().setFlag(Flags.C);
    }

}
