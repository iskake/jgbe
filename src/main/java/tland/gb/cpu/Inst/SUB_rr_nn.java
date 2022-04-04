package tland.gb.cpu.Inst;

import tland.gb.GameBoy;
import tland.gb.Registers.Flags;
import tland.gb.Registers.RegisterIndex;

/**
 * Subtraction instruction.
 * 
 * <p>
 * Implements opcodes: {@code sub a, r8}, {@code sub a, $n8},
 * {@code sub hl, r16}, {@code sbc a, r8} and {@code sbc a, $n8}
 */
public class SUB_rr_nn extends Instruction {
    private final RegisterIndex r1;
    private final RegisterIndex r2;
    public final boolean carry;

    public SUB_rr_nn(String name, RegisterIndex r1, RegisterIndex r2, boolean carry) {
        super(name);
        this.r1 = r1;
        this.r2 = r2;
        this.carry = carry;
    }

    @Override
    public void doOp(GameBoy gb, int opcode) {
        byte value;

        if (r2 == null) {
            value = gb.readNextByte();
        } else {
            value = gb.reg.readRegisterByte(r2);
        }

        if (carry) {
            // sbc a, nn
            value += gb.reg.isFlagSet(Flags.C) ? 1 : 0;
        }

        byte a = gb.reg.readRegisterByte(r1);

        gb.reg.writeRegisterByte(r1, a - value);

        gb.reg.setFlagConditional(Flags.Z, a - value == 0);
        gb.reg.setFlag(Flags.N);
        gb.reg.setFlagConditional(Flags.H, (a & (byte) 0b1111) < (value & (byte) 0b1111));
        gb.reg.setFlagConditional(Flags.C, Byte.toUnsignedInt(a) < Byte.toUnsignedInt(value));
    }

}
