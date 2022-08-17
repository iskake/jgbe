package iskake.jgbe.core.gb.cpu.inst;

import iskake.jgbe.core.gb.IGameBoy;
import iskake.jgbe.core.gb.Registers.Flags;
import iskake.jgbe.core.gb.Registers.Register;

/**
 * Subtraction instruction.
 * 
 * <p>
 * Implements opcodes: {@code sub r8}, {@code sub $n8}, {@code sbc r8} and {@code sbc $n8}
 */
public class SUB {

    public static void sub(IGameBoy gb, int opcode) {
        Register r1 = Register.A;
        Register r2 = ((opcode & 0b1100_0000) >> 6) == 3 ? null : Register.tableByte[(opcode & 0b111)];
        byte value = r2 == null ? gb.readNextByte() : gb.reg().readByte(r2);

        byte c = 0;
        boolean carry = (opcode & 0b111000) == 0b11000;
        if (carry) {
            // sbc nn
            c += gb.reg().isFlagSet(Flags.C) ? 1 : 0;
        }

        byte a = gb.reg().readByte(r1);

        gb.reg().writeByte(r1, a - value - c);

        gb.reg().setFlagIf(Flags.Z, (byte) (a - value - c) == 0);
        gb.reg().setFlag(Flags.N);
        gb.reg().setFlagIf(Flags.H, (Byte.toUnsignedInt(a) & 0b1111) < ((Byte.toUnsignedInt(value) & 0b1111) + c));
        gb.reg().setFlagIf(Flags.C, ((Byte.toUnsignedInt(a) - Byte.toUnsignedInt(value) - c) & 0xfff) > 0xff);
    }

}
