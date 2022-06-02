package iskake.gb.cpu.inst;

import iskake.gb.IGameBoy;

/**
 * Illegal instruction. Will only throw an IllegalInstructionException.
 */
public class IllegalInst extends Instruction {

    public IllegalInst(String name) {
        super(name);
    }

    @Override
    public void doOp(IGameBoy emu, int opcode) {
        throw new IllegalInstructionException();
    }
    
}
