package tland.emu.cpu.Inst;

import tland.emu.IEmulator;
import tland.emu.Registers.Flags;
import tland.emu.Registers.RegisterIndex;

/**
 * Comparison instruction. Compares the A register with the register/value
 * nn, 'same' as {@code sub a, nn} but does not store the result in A.
 * 
 * <p>
 * Implements opcodes: {@code cp r8} and {@code cp $n8}
 */
public class CP_nn extends Instruction {
    public final RegisterIndex reg;

    public CP_nn(String name, RegisterIndex reg) {
        super(name);
        this.reg = reg;
    }

    @Override
    public void doOp(IEmulator emu, int opcode) {
        byte value;

        if (reg == null) {
            value = emu.readNextByte();
        } else {
            value = emu.reg().readRegisterByte(reg);
        }

        byte a = emu.reg().readRegisterByte(RegisterIndex.A);

        emu.reg().setFlagConditional(Flags.Z, a - value == 0);
        emu.reg().setFlag(Flags.N);
        emu.reg().setFlagConditional(Flags.H, (Byte.toUnsignedInt(a) & 0b1111) < (Byte.toUnsignedInt(value) & 0b1111));
        emu.reg().setFlagConditional(Flags.C, Byte.toUnsignedInt(a) < Byte.toUnsignedInt(value));
    }

}
