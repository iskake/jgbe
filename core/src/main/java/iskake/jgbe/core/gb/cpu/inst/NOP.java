package iskake.jgbe.core.gb.cpu.inst;

import iskake.jgbe.core.gb.IGameBoy;

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
