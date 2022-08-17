package iskake.jgbe.core.gb.cpu.inst;

import iskake.jgbe.core.gb.IGameBoy;
import iskake.jgbe.core.gb.Registers.Register;

/**
 * 'Push' the short value stored in the specified register into the stack and
 * decrement the stack pointer.
 * 
 * <p>
 * Implements opcodes: {@code pop r16}
 */
public class PUSH_r16 implements Instruction {

    @Override
    public void doOp(IGameBoy gb, int opcode) {
        Register reg = Register.tableShortAF[(opcode & 0b110000) >> 4];
        gb.sp().push(gb.reg().readShort(reg));
    }

}
