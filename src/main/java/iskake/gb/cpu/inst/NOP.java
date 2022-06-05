package iskake.gb.cpu.inst;

import iskake.gb.GameBoy;

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
    public void doOp(GameBoy gb, int opcode) {
    }

}
