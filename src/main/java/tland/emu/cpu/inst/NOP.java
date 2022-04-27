package tland.emu.cpu.inst;

import tland.emu.IEmulator;

/**
 * No operation instruction.
 * 
 * <p> Note: the operation still takes 
 */
public class NOP extends Instruction {

    public NOP() {
        super("nop");
    }

    @Override
    public void doOp(IEmulator emu, int opcode) {
    }

}
