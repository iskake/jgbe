package tland.gb.cpu.Inst;

import tland.Bitwise;
import tland.gb.GameBoy;
import tland.gb.Registers;
import tland.gb.Registers.Flags;
import tland.gb.Registers.RegisterIndex;

/**
 * Addition instruction.
 * 
 * <p>
 * Implements opcodes: {@code add a, r8}, {@code add a, $n8},
 * {@code add hl, r16}, {@code add sp, $e8}, {@code adc a, r8} and
 * {@code adc a, $n8}
 */
public class ADD_rr_nn extends Instruction {
    private final RegisterIndex r1;
    private final RegisterIndex r2;
    public final boolean carry;

    public ADD_rr_nn(String name, RegisterIndex r1, RegisterIndex r2) {
        // ld hl, r16 cannot use carry, so we can set `carry` to false.
        this(name, r1, r2, false);
    }

    public ADD_rr_nn(String name, RegisterIndex r1, RegisterIndex r2, boolean carry) {
        super(name);
        this.r1 = r1;
        this.r2 = r2;
        this.carry = carry;
    }

    @Override
    public void doOp(GameBoy gb, int opcode) {
        // TODO: handle `add hl, sp` and `add sp, e8`
        if (Registers.isRegisterByte(r1) || opcode == 0x86) {
            byte value;

            if (r2 == null) {
                value = gb.readNextByte();
            } else {
                value = gb.reg.readRegisterByte(r2);
            }

            byte carryVal = 0;

            if (carry) {
                // adc rr, nn
                carryVal = gb.reg.isFlagSet(Flags.C) ? (byte) 1 : (byte) 0;
            }

            byte regVal = gb.reg.readRegisterByte(r1);

            gb.reg.writeRegisterByte(r1, regVal + value + carryVal);

            if (regVal + value + carryVal == 0) {
                gb.reg.setFlag(Flags.Z);
            } else {
                gb.reg.resetFlag(Flags.Z);
            }

            gb.reg.resetFlag(Flags.N);

            if (((Byte.toUnsignedInt(regVal) & 0b1111) + (Byte.toUnsignedInt(value) & 0b1111) + carryVal) > 0b1111) {
                gb.reg.setFlag(Flags.H);
            } else {
                gb.reg.resetFlag(Flags.H);
            }

            if (Byte.toUnsignedInt(regVal) + Byte.toUnsignedInt(value) > 0xff) {
                gb.reg.setFlag(Flags.C);
            } else {
                gb.reg.resetFlag(Flags.C);
            }
        } else if (opcode == 0xe8) {
            // add sp, $e8
            byte value = gb.readNextByte();
            short regVal = gb.getSP();
            gb.setSP((short) (regVal + value));

            gb.reg.resetFlag(Flags.Z);
            gb.reg.resetFlag(Flags.N);

            if (((Short.toUnsignedInt(regVal) & 0b1111) + (Byte.toUnsignedInt(value) & 0b1111)) > 0b1111) {
                gb.reg.setFlag(Flags.H);
            } else {
                gb.reg.resetFlag(Flags.H);
            }

            if ((Short.toUnsignedInt(regVal) & 0xff) + Byte.toUnsignedInt(value) > 0xff) {
                gb.reg.setFlag(Flags.C);
            } else {
                gb.reg.resetFlag(Flags.C);
            }
        } else {
            short r1Val = gb.reg.readRegisterShort(r1);
            short r2Val = gb.reg.readRegisterShort(r2);

            gb.reg.writeRegisterShort(r1, r1Val + r2Val);

            gb.reg.resetFlag(Flags.N);

            if ((Short.toUnsignedInt(r1Val) & 0xfff) + (Short.toUnsignedInt(r2Val) & 0xfff) > 0xfff) {
                // fff -> 0b111111111111
                gb.reg.setFlag(Flags.H);
            } else {
                gb.reg.resetFlag(Flags.H);
            }

            if (Short.toUnsignedInt(r1Val) + Short.toUnsignedInt(r2Val) > 0xffff) {
                gb.reg.setFlag(Flags.C);
            } else {
                gb.reg.resetFlag(Flags.C);
            }
        }
    }

}
