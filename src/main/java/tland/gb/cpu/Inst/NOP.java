package tland.gb.cpu.Inst;

import tland.gb.IGameBoy;

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
    public void doOp(IGameBoy gb, int opcode) {
    }

}
