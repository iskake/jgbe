package iskake.jgbe.core.gb.cpu.inst;

import iskake.jgbe.core.gb.IGameBoy;
import iskake.jgbe.core.gb.Registers.Flags;
import iskake.jgbe.core.gb.Registers.Register;

/**
 * Logical AND instruction. Takes A AND the register/value nn ({@code a & nn})
 * 
 * <p>
 * Implements opcodes: {@code and r8} and {@code and $n8}
 */
public class AND_nn implements Instruction {
    private static final int OP_AND_N8 = 0xe6;

    @Override
    public void doOp(IGameBoy gb, int opcode) {
        Register reg = opcode == OP_AND_N8 ? null : Register.tableByte[opcode & 0b111];
        byte value = reg == null ? gb.readNextByte() : gb.reg().readByte(reg);

        byte a = gb.reg().readByte(Register.A);

        gb.reg().writeByte(Register.A, a & value);

        gb.reg().setFlagIf(Flags.Z, (a & value) == 0);
        gb.reg().resetFlag(Flags.N);
        gb.reg().setFlag(Flags.H);
        gb.reg().resetFlag(Flags.C);
    }

}
