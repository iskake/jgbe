package tland.gb.Inst;

import tland.gb.GameBoy;

public class IllegalInst extends Instruction {

    public IllegalInst(String name) {
        super(name);
    }

    @Override
    public void doOp(GameBoy gb, int opcode) {
        throw new IllegalInstructionException();
    }
    
}
