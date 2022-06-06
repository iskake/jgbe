package iskake.jgbe.core.gb.cpu.inst;

import iskake.jgbe.core.gb.IGameBoy;
import iskake.jgbe.core.gb.Registers.Flags;
import iskake.jgbe.core.gb.Registers.RegisterIndex;

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
    public void doOp(IGameBoy gb, int opcode) {
        byte value;

        if (reg == null) {
            value = gb.readNextByte();
        } else {
            value = gb.reg().readRegisterByte(reg);
        }

        byte a = gb.reg().readRegisterByte(RegisterIndex.A);

        gb.reg().setFlagConditional(Flags.Z, a - value == 0);
        gb.reg().setFlag(Flags.N);
        gb.reg().setFlagConditional(Flags.H, (Byte.toUnsignedInt(a) & 0b1111) < (Byte.toUnsignedInt(value) & 0b1111));
        gb.reg().setFlagConditional(Flags.C, Byte.toUnsignedInt(a) < Byte.toUnsignedInt(value));
    }

}
