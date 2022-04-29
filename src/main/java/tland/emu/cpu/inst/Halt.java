package tland.emu.cpu.inst;

import tland.emu.IEmulator;
import tland.emu.Registers.RegisterIndex;

/**
 * Opcodes that 'halt' the operation of the emulator.
 * 
 * <p>
 * Implements opcodes: {@code stop} and {@code halt}
 * 
 * @author Tarjei Landøy
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
