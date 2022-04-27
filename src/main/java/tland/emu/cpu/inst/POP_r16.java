package tland.emu.cpu.inst;

import tland.emu.IEmulator;
import tland.emu.Registers.RegisterIndex;

/**
 * 'Pop' the short value stored at the stack pointer, increment the stack
 * pointer, and store in the specified register.
 * 
 * <p>
 * Implements opcodes: {@code pop r16}
 */
public class POP_r16 extends Instruction {
    private final RegisterIndex reg;

    public POP_r16(String name, RegisterIndex reg) {
        super(name);
        this.reg = reg;
    }

    @Override
    public void doOp(IEmulator emu, int opcode) {
        short value = emu.sp().pop();
        emu.reg().writeRegisterShort(reg, value);
    }

}
