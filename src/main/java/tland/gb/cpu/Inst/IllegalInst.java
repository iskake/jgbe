package tland.gb.cpu.Inst;

import tland.gb.IGameBoy;

public class IllegalInst extends Instruction {

    public IllegalInst(String name) {
        super(name);
    }

    @Override
    public void doOp(IGameBoy gb, int opcode) {
        throw new IllegalInstructionException();
    }
    
}
