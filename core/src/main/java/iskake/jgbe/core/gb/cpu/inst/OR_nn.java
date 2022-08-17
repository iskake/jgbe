package iskake.jgbe.core.gb.cpu.inst;

import iskake.jgbe.core.gb.IGameBoy;
import iskake.jgbe.core.gb.Registers.Flags;
import iskake.jgbe.core.gb.Registers.Register;

/**
 * Logical OR instruction. Takes A OR the register/value nn ({@code a | nn})
 * 
 * <p>
 * Implements opcodes: {@code or r8} and {@code or $n8}
 */
public class OR_nn extends Instruction {
    private static final int OP_OR_N8 = 0xf6;

    public OR_nn(String name) {
        super(name);
    }

    @Override
    public void doOp(IGameBoy gb, int opcode) {
        Register reg = opcode == OP_OR_N8 ? null : Register.tableByte[opcode & 0b111];
        byte value = reg == null ? gb.readNextByte() : gb.reg().readByte(reg);

        byte a = gb.reg().readByte(Register.A);

        gb.reg().writeByte(Register.A, a | value);

        gb.reg().setFlagIf(Flags.Z, (a | value) == 0);
        gb.reg().resetFlag(Flags.N);
        gb.reg().resetFlag(Flags.H);
        gb.reg().resetFlag(Flags.C);
    }

}
