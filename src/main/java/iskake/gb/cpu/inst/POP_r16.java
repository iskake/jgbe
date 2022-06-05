package iskake.gb.cpu.inst;

import iskake.gb.IGameBoy;
import iskake.gb.Registers.RegisterIndex;

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
    public void doOp(IGameBoy gb, int opcode) {
        short value = gb.sp().pop();
        gb.reg().writeRegisterShort(reg, value);
    }

}
