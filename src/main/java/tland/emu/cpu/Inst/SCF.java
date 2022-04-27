package tland.emu.cpu.Inst;

import tland.emu.IEmulator;
import tland.emu.Registers.Flags;

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
    public void doOp(IEmulator emu, int opcode) {
        emu.reg().resetFlag(Flags.N);
        emu.reg().resetFlag(Flags.H);
        emu.reg().setFlag(Flags.C);
    }

}
