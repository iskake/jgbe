package tland.gb.cpu.Inst;

import tland.gb.GameBoy;
import tland.gb.Registers.Flags;
import tland.gb.Registers.RegisterIndex;

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
    public void doOp(GameBoy gb, int opcode) {
        byte value;

        if (reg == null) {
            value = gb.readNextByte();
        } else {
            value = gb.reg.readRegisterByte(reg);
        }

        byte a = gb.reg.readRegisterByte(RegisterIndex.A);

        if (a - value == 0) {
            gb.reg.setFlag(Flags.Z);
        } else {
            gb.reg.resetFlag(Flags.Z);
        }

        gb.reg.setFlag(Flags.N);

        if ((Byte.toUnsignedInt(a) & 0b1111) < (Byte.toUnsignedInt(value) & 0b1111)) {
            gb.reg.setFlag(Flags.H);
        } else {
            gb.reg.resetFlag(Flags.H);
        }

        if (Byte.toUnsignedInt(a) < Byte.toUnsignedInt(value)) {
            gb.reg.setFlag(Flags.C);
        } else {
            gb.reg.resetFlag(Flags.C);
        }
    }

}
