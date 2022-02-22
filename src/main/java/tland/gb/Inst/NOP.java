package tland.gb.Inst;

import tland.gb.CPU;

public class NOP extends Instruction {

    public NOP() {
        super("nop");
    }

    @Override
    public void doOp(CPU cpu, int opcode) {
    }

}
