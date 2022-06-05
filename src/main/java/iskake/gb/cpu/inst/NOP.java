package iskake.gb.cpu.inst;

import iskake.gb.IGameBoy;

/**
 * No operation instruction.
 * <p>
 * The instruction does the following: nothing.
 */
public class NOP extends Instruction {

    public NOP() {
        super("nop");
    }

    @Override
    public void doOp(IGameBoy gb, int opcode) {
    }

}
