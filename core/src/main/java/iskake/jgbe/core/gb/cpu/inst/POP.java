package iskake.jgbe.core.gb.cpu.inst;

import iskake.jgbe.core.gb.IGameBoy;
import iskake.jgbe.core.gb.Registers.Register;

/**
 * 'Pop' the short value stored at the stack pointer, increment the stack
 * pointer, and store in the specified register.
 * 
 * <p>
 * Implements opcodes: {@code pop r16}
 */
public class POP {

    public static void pop_r16(IGameBoy gb, int opcode) {
        Register reg = Register.tableShortAF[(opcode & 0b110000) >> 4];
        short value = gb.sp().pop();
        gb.reg().writeShort(reg, value);
    }

}
