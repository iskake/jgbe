package tland.gb.cpu.Inst;

import tland.gb.IGameBoy;
import tland.gb.Registers.Flags;

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
    public void doOp(IGameBoy gb, int opcode) {
        gb.reg().resetFlag(Flags.N);
        gb.reg().resetFlag(Flags.H);
        gb.reg().complementFlag(Flags.C);
    }

}
