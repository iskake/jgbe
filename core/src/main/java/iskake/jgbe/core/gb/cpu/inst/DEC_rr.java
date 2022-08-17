package iskake.jgbe.core.gb.cpu.inst;

import iskake.jgbe.core.gb.IGameBoy;
import iskake.jgbe.core.gb.Registers.Flags;
import iskake.jgbe.core.gb.Registers.Register;

/**
 * Decrement value in register / address pointed to by register (with [HL]).
 * 
 * <p>
 * Implements opcodes: {@code dec r8}, {@code dec [hl]} and {@code dec r16}
 */
public class DEC_rr implements Instruction {
    private static final int OP_DEC_$HL = 0x35;

    @Override
    public void doOp(IGameBoy gb, int opcode) {
        boolean dec8 = (opcode & 0b111) == 0b101;

        // 0x35 -> dec [hl]
        if (dec8 || opcode == OP_DEC_$HL) {
            Register reg = Register.tableByte[(opcode & 0b111000) >> 3];
            gb.reg().decByte(reg);

            byte value = gb.reg().readByte(reg);

            gb.reg().setFlagIf(Flags.Z, value == 0);
            gb.reg().setFlag(Flags.N);
            gb.reg().setFlagIf(Flags.H, (Byte.toUnsignedInt(value) & 0b1111) == 0b1111);

        } else {
            Register reg = Register.tableShortSP[(opcode & 0b110000) >> 4];
            gb.reg().decShort(reg);
        }
    }

}
