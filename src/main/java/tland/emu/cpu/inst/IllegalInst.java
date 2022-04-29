package tland.emu.cpu.inst;

import tland.emu.IEmulator;

/**
 * Illegal instruction. Will only throw an IllegalInstructionException.
 * 
 * @author Tarjei Landøy
 */
public class IllegalInst extends Instruction {

    public IllegalInst(String name) {
        super(name);
    }

    @Override
    public void doOp(IEmulator emu, int opcode) {
        throw new IllegalInstructionException();
    }
    
}
