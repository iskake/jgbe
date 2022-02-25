package tland.gb.Inst;

import tland.gb.CPU;
import tland.gb.GameBoy;

public class NOP extends Instruction {

    public NOP() {
        super("nop");
    }

    @Override
    public void doOp(GameBoy gb, int opcode) {
    }

}
