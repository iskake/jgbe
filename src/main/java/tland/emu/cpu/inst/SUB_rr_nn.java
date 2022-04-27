package tland.emu.cpu.inst;

import tland.emu.IEmulator;
import tland.emu.Registers.Flags;
import tland.emu.Registers.RegisterIndex;

/**
 * Subtraction instruction.
 * 
 * <p>
 * Implements opcodes: {@code sub r8}, {@code sub $n8}, {@code sbc r8} and {@code sbc $n8}
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
    public void doOp(IEmulator emu, int opcode) {
        byte value;

        if (r2 == null) {
            value = emu.readNextByte();
        } else {
            value = emu.reg().readRegisterByte(r2);
        }

        byte c = 0;
        if (carry) {
            // sbc nn
            c += emu.reg().isFlagSet(Flags.C) ? 1 : 0;
        }

        byte a = emu.reg().readRegisterByte(r1);

        emu.reg().writeRegisterByte(r1, a - value - c);

        emu.reg().setFlagConditional(Flags.Z, (byte) (a - value - c) == 0);
        emu.reg().setFlag(Flags.N);
        emu.reg().setFlagConditional(Flags.H, (Byte.toUnsignedInt(a) & 0b1111) < ((Byte.toUnsignedInt(value) & 0b1111) + c));
        emu.reg().setFlagConditional(Flags.C, ((Byte.toUnsignedInt(a) - Byte.toUnsignedInt(value) - c) & 0xfff) > 0xff);
    }

}
