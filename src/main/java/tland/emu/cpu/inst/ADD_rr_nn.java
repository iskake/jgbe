package tland.emu.cpu.inst;

import tland.emu.IEmulator;
import tland.emu.Registers;
import tland.emu.Registers.Flags;
import tland.emu.Registers.RegisterIndex;

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
    public void doOp(IEmulator emu, int opcode) {
        if (Registers.isRegisterByte(r1) || opcode == 0x86) {
            byte value;

            if (r2 == null) {
                value = emu.readNextByte();
            } else {
                value = emu.reg().readRegisterByte(r2);
            }

            byte c = 0;
            if (carry) {
                // adc rr, nn
                c += emu.reg().isFlagSet(Flags.C) ? 1 : 0;
            }

            byte regVal = emu.reg().readRegisterByte(r1);

            emu.reg().writeRegisterByte(r1, regVal + value + c);
            emu.reg().setFlagConditional(Flags.Z, (regVal + value + c == 0));
            emu.reg().resetFlag(Flags.N);
            emu.reg().setFlagConditional(Flags.H, (((Byte.toUnsignedInt(regVal) & 0b1111)
                    + (Byte.toUnsignedInt(value) & 0b1111) + c) > 0b1111));

            emu.reg().setFlagConditional(Flags.C, (Byte.toUnsignedInt(regVal) + Byte.toUnsignedInt(value) > 0xff));
        } else if (opcode == 0xe8) {
            // add sp, $e8
            byte value = emu.readNextByte();
            short regVal = emu.sp().get();
            emu.sp().set((short) (regVal + value));

            emu.reg().resetFlag(Flags.Z);
            emu.reg().resetFlag(Flags.N);
            emu.reg().setFlagConditional(Flags.H,
                    ((Short.toUnsignedInt(regVal) & 0b1111) + (Byte.toUnsignedInt(value) & 0b1111)) > 0b1111);
            emu.reg().setFlagConditional(Flags.C, (Short.toUnsignedInt(regVal) & 0xff) + Byte.toUnsignedInt(value) > 0xff);
        } else {
            short r1Val = emu.reg().readRegisterShort(r1);
            short r2Val = emu.reg().readRegisterShort(r2);

            emu.reg().writeRegisterShort(r1, r1Val + r2Val);
            emu.reg().resetFlag(Flags.N);
            emu.reg().setFlagConditional(Flags.H,
                    ((Short.toUnsignedInt(r1Val) & 0xfff) + (Short.toUnsignedInt(r2Val) & 0xfff) > 0xfff));
            emu.reg().setFlagConditional(Flags.C, Short.toUnsignedInt(r1Val) + Short.toUnsignedInt(r2Val) > 0xffff);
        }
    }

}
