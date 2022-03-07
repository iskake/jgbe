package tland.gb.Inst;

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
 * {@code add hl, r16}, {@code adc a, r8} and {@code adc a, $n8}
 */
public class ADD_rr_nn extends Instruction {
    private final RegisterIndex r1;
    private final RegisterIndex r2;
    public final boolean carry;

    public ADD_rr_nn(String name, RegisterIndex r1, RegisterIndex r2, boolean carry) {
        super(name);
        this.r1 = r1;
        this.r2 = r2;
        this.carry = carry;
    }

    @Override
    public void doOp(GameBoy gb, int opcode) {
        if (Registers.isRegisterByte(r1) || opcode == 0x86) {
            if (opcode == 0x86) {
                System.out.println("HL!");
            }
            byte value;

            if (r2 == null) {
                value = gb.readNextByte();
            } else {
                value = gb.reg.readRegisterByte(r2);
            }

            if (carry) {
                // adc rr, nn
                value += gb.reg.isFlagSet(Flags.C) ? 1 : 0;
            }

            byte a = gb.reg.readRegisterByte(r1);

            gb.reg.writeRegisterByte(r1, a + value);

            if (a + value == 0) {
                gb.reg.setFlag(Flags.Z);
            } else {
                gb.reg.resetFlag(Flags.Z);
            }

            gb.reg.resetFlag(Flags.N);

            if ((a & (byte) 0b1111) + (value & (byte) 0b1111) > 0b1111) {
                gb.reg.setFlag(Flags.H);
            } else {
                gb.reg.resetFlag(Flags.H);
            }

            if (Byte.toUnsignedInt(a) + Byte.toUnsignedInt(value) > 0xff) {
                gb.reg.setFlag(Flags.C);
            } else {
                gb.reg.resetFlag(Flags.C);
            }
        } else {
            // TODO
        }
    }

}
