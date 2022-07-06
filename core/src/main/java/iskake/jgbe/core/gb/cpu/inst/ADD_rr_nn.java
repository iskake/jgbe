package iskake.jgbe.core.gb.cpu.inst;

import iskake.jgbe.core.gb.IGameBoy;
import iskake.jgbe.core.gb.Registers;
import iskake.jgbe.core.gb.Registers.Flags;
import iskake.jgbe.core.gb.Registers.Register;

/**
 * Addition instruction.
 * 
 * <p>
 * Implements opcodes: {@code add a, r8}, {@code add a, $n8},
 * {@code add hl, r16}, {@code add sp, $e8}, {@code adc r8} and
 * {@code adc $n8}
 */
public class ADD_rr_nn extends Instruction {
    private static final int OP_ADD_A_$HL = 0x86;
    private static final int OP_ADD_SP_E8 = 0xe8;

    private final Register r1;
    private final Register r2;
    public final boolean carry;

    public ADD_rr_nn(String name, Register r1, Register r2) {
        // ld hl, r16 cannot use carry, so we can set `carry` to false.
        this(name, r1, r2, false);
    }

    public ADD_rr_nn(String name, Register r1, Register r2, boolean carry) {
        super(name);
        this.r1 = r1;
        this.r2 = r2;
        this.carry = carry;
    }

    @Override
    public void doOp(IGameBoy gb, int opcode) {
        if (Registers.isRegisterByte(r1) || opcode == OP_ADD_A_$HL) {
            byte value;

            if (r2 == null) {
                value = gb.readNextByte();
            } else {
                value = gb.reg().readRegisterByte(r2);
            }

            byte c = 0;
            if (carry) {
                // adc rr, nn
                c += gb.reg().isFlagSet(Flags.C) ? 1 : 0;
            }

            byte regVal = gb.reg().readRegisterByte(r1);

            gb.reg().writeRegisterByte(r1, regVal + value + c);
            gb.reg().setFlagConditional(Flags.Z, (regVal + value + c == 0));
            gb.reg().resetFlag(Flags.N);
            gb.reg().setFlagConditional(Flags.H, (((Byte.toUnsignedInt(regVal) & 0b1111)
                    + (Byte.toUnsignedInt(value) & 0b1111) + c) > 0b1111));

            gb.reg().setFlagConditional(Flags.C, (Byte.toUnsignedInt(regVal) + Byte.toUnsignedInt(value) > 0xff));
        } else if (opcode == OP_ADD_SP_E8) {
            // add sp, $e8
            byte value = gb.readNextByte();
            short regVal = gb.sp().get();
            gb.sp().set((short) (regVal + value));

            gb.reg().resetFlag(Flags.Z);
            gb.reg().resetFlag(Flags.N);
            gb.reg().setFlagConditional(Flags.H,
                    ((Short.toUnsignedInt(regVal) & 0b1111) + (Byte.toUnsignedInt(value) & 0b1111)) > 0b1111);
            gb.reg().setFlagConditional(Flags.C, (Short.toUnsignedInt(regVal) & 0xff) + Byte.toUnsignedInt(value) > 0xff);
        } else {
            short r1Val = gb.reg().readRegisterShort(r1);
            short r2Val = gb.reg().readRegisterShort(r2);

            gb.reg().writeRegisterShort(r1, r1Val + r2Val);
            gb.reg().resetFlag(Flags.N);
            gb.reg().setFlagConditional(Flags.H,
                    ((Short.toUnsignedInt(r1Val) & 0xfff) + (Short.toUnsignedInt(r2Val) & 0xfff) > 0xfff));
            gb.reg().setFlagConditional(Flags.C, Short.toUnsignedInt(r1Val) + Short.toUnsignedInt(r2Val) > 0xffff);
        }
    }

}
