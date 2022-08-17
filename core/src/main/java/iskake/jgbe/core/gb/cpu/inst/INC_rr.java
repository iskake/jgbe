package iskake.jgbe.core.gb.cpu.inst;

import iskake.jgbe.core.gb.IGameBoy;
import iskake.jgbe.core.gb.Registers.Flags;
import iskake.jgbe.core.gb.Registers.Register;

/**
 * Increment value in register / address pointed to by register (with [HL]).
 * 
 * <p>
 * Implements opcodes: {@code inc r8}, {@code inc [hl]} and {@code inc r16}
 */
public class INC_rr {
    private static final int OP_INC_$HL = 0x34;

    public static void inc_rr(IGameBoy gb, int opcode) {
        boolean inc8 = (opcode & 0b111) == 0b100;

        // 0x34 -> inc [hl]
        if (inc8 || opcode == OP_INC_$HL) {
            Register reg = Register.tableByte[(opcode & 0b111000) >> 3];
            gb.reg().incByte(reg);

            byte value = gb.reg().readByte(reg);

            gb.reg().setFlagIf(Flags.Z, value == 0);
            gb.reg().resetFlag(Flags.N);
            gb.reg().setFlagIf(Flags.H, (Byte.toUnsignedInt(value) & 0b1111) == 0);

        } else {
            Register reg = Register.tableShortSP[(opcode & 0b110000) >> 4];
            gb.reg().incShort(reg);
        }
    }

}
