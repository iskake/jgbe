package tland.gb.cpu.Inst;

import tland.gb.IGameBoy;
import tland.gb.Registers.RegisterIndex;

/**
 * 'Push' the short value stored in the specified register into the stack and
 * decrement the stack pointer.
 * 
 * <p>
 * Implements opcodes: {@code pop r16}
 */
public class PUSH_r16 extends Instruction {
    private final RegisterIndex reg;

    public PUSH_r16(String name, RegisterIndex reg) {
        super(name);
        this.reg = reg;
    }

    @Override
    public void doOp(IGameBoy gb, int opcode) {
        gb.sp().push(gb.reg().readRegisterShort(reg));
    }

}
