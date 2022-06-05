package iskake.gb.cpu.inst;

import iskake.gb.GameBoy;
import iskake.gb.Registers.Flags;

/**
 * Complement Carry Flag, toggles the carry flag ({@code Flags.C}) and
 * resets {@code Flags.N} and {@code Flags.H}.
 * 
 * <p>
 * Implements opcode: {@code ccf}.
 */
public class CCF extends Instruction {

    public CCF() {
        super("ccf");
    }

    @Override
    public void doOp(GameBoy gb, int opcode) {
        gb.reg.resetFlag(Flags.N);
        gb.reg.resetFlag(Flags.H);
        gb.reg.complementFlag(Flags.C);
    }

}
