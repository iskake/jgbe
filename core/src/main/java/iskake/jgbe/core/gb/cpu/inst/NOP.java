package iskake.jgbe.core.gb.cpu.inst;

import iskake.jgbe.core.gb.IGameBoy;

/**
 * No operation instruction.
 * <p>
 * The instruction does the following: nothing.
 */
public class NOP implements Instruction {

    @Override
    public void doOp(IGameBoy gb, int opcode) {
    }

}
