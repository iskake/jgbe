package iskake.jgbe.core.gb.cpu.inst;

import iskake.jgbe.core.gb.IGameBoy;

/**
 * Illegal instruction. Will only throw an IllegalInstructionException.
 */
public class IllegalInst implements Instruction {

    @Override
    public void doOp(IGameBoy gb, int opcode) {
        throw new IllegalInstructionException();
    }
    
}
