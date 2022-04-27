package tland.emu.cpu.inst;

import tland.emu.IEmulator;

public class IllegalInst extends Instruction {

    public IllegalInst(String name) {
        super(name);
    }

    @Override
    public void doOp(IEmulator emu, int opcode) {
        throw new IllegalInstructionException();
    }
    
}
