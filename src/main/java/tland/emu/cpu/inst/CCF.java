package tland.emu.cpu.inst;

import tland.emu.IEmulator;
import tland.emu.Registers.Flags;

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
    public void doOp(IEmulator emu, int opcode) {
        emu.reg().resetFlag(Flags.N);
        emu.reg().resetFlag(Flags.H);
        emu.reg().complementFlag(Flags.C);
    }

}
