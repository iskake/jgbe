package tland.gb.cpu.Inst;

import tland.gb.IGameBoy;
import tland.gb.Registers.RegisterIndex;

/**
 * Opcodes that 'halt' the operation of the Game Boy.
 * 
 * <p>
 * Implements opcodes: {@code stop} and {@code halt}
 */
public class Halt extends Instruction {

    public Halt(String name) {
        super(name);
    }

    @Override
    public void doOp(IGameBoy gb, int opcode) {
        switch (opcode) {
            case 0x10 -> gb.stop();
            case 0x76 -> gb.halt(gb.reg().readRegisterShort(RegisterIndex.HL));
            default -> throw new IllegalInstructionException();
        }
    }
}
