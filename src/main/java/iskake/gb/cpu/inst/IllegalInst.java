package iskake.gb.cpu.inst;

import iskake.gb.GameBoy;

public class IllegalInst extends Instruction {

    public IllegalInst(String name) {
        super(name);
    }

    @Override
    public void doOp(GameBoy gb, int opcode) {
        throw new IllegalInstructionException();
    }
    
}
