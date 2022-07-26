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
public class SUB_rr_nn extends Instruction {
    private final Register r1;
    private final Register r2;
    public final boolean carry;

    public SUB_rr_nn(String name, Register r1, Register r2, boolean carry) {
        super(name);
        this.r1 = r1;
        this.r2 = r2;
        this.carry = carry;
    }

    @Override
    public void doOp(IGameBoy gb, int opcode) {
        byte value = r2 == null ? gb.readNextByte() : gb.reg().readByte(r2);

        byte c = 0;
        if (carry) {
            // sbc nn
            c += gb.reg().isFlagSet(Flags.C) ? 1 : 0;
        }

        byte a = gb.reg().readByte(r1);

        gb.reg().writeByte(r1, a - value - c);

        gb.reg().setFlagConditional(Flags.Z, (byte) (a - value - c) == 0);
        gb.reg().setFlag(Flags.N);
        gb.reg().setFlagConditional(Flags.H, (Byte.toUnsignedInt(a) & 0b1111) < ((Byte.toUnsignedInt(value) & 0b1111) + c));
        gb.reg().setFlagConditional(Flags.C, ((Byte.toUnsignedInt(a) - Byte.toUnsignedInt(value) - c) & 0xfff) > 0xff);
    }

}
