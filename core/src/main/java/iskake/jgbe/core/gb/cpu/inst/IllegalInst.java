package iskake.jgbe.core.gb.cpu.inst;

import iskake.jgbe.core.gb.IGameBoy;

/**
 * Illegal instruction. Will only throw an IllegalInstructionException.
 */
public class IllegalInst extends Instruction {

    public IllegalInst(String name) {
        super(name);
    }

    @Override
    public void doOp(IGameBoy gb, int opcode) {
        throw new IllegalInstructionException();
    }
    
}
