package iskake.jgbe.core.gb.cpu.inst;

import iskake.jgbe.core.gb.IGameBoy;
import iskake.jgbe.core.gb.Registers.Flags;
import iskake.jgbe.core.gb.Registers.Register;

/**
 * Comparison instruction. Compares the A register with the register/value
 * nn, 'same' as {@code sub a, nn} but does not store the result in A.
 * 
 * <p>
 * Implements opcodes: {@code cp r8} and {@code cp $n8}
 */
public class CP_nn implements Instruction {
    private static final int OP_CP_N8 = 0xfe;

    @Override
    public void doOp(IGameBoy gb, int opcode) {
        Register reg = opcode == OP_CP_N8 ? null : Register.tableByte[opcode & 0b111];
        byte value = reg == null ? gb.readNextByte() : gb.reg().readByte(reg);

        byte a = gb.reg().readByte(Register.A);

        gb.reg().setFlagIf(Flags.Z, a - value == 0);
        gb.reg().setFlag(Flags.N);
        gb.reg().setFlagIf(Flags.H, (Byte.toUnsignedInt(a) & 0b1111) < (Byte.toUnsignedInt(value) & 0b1111));
        gb.reg().setFlagIf(Flags.C, Byte.toUnsignedInt(a) < Byte.toUnsignedInt(value));
    }

}
