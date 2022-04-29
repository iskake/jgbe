package tland.emu.cpu.inst;

import tland.emu.IEmulator;

/**
 * No operation instruction.
 * <p>
 * The instruction does the following: nothing.
 * 
 * @author Tarjei Landøy
 */
public class NOP extends Instruction {

    public NOP() {
        super("nop");
    }

    @Override
    public void doOp(IEmulator emu, int opcode) {
    }

}
