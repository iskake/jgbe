package tland.emu.cpu.Inst;

import tland.emu.IEmulator;
import tland.emu.Registers.RegisterIndex;

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
    public void doOp(IEmulator emu, int opcode) {
        switch (opcode) {
            case 0x10 -> emu.stop();
            case 0x76 -> emu.halt(emu.reg().readRegisterShort(RegisterIndex.HL));
            default -> throw new IllegalInstructionException();
        }
    }
}
