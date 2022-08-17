package iskake.jgbe.core.gb.cpu.inst;

import iskake.jgbe.core.gb.IGameBoy;
import iskake.jgbe.core.gb.Registers.Flags;
import iskake.jgbe.core.gb.Registers.Register;

/**
 * Logical XOR instruction. Takes A XOR the register/value nn ({@code a ^ nn})
 * 
 * <p>
 * Implements opcodes: {@code xor r8} and {@code xor $n8}
 */
public class XOR_nn extends Instruction {
    public static final int OP_XOR_N8 = 0xee;

    public XOR_nn(String name) {
        super(name);
    }

    @Override
    public void doOp(IGameBoy gb, int opcode) {
        Register reg = opcode == OP_XOR_N8 ? null : Register.tableByte[opcode & 0b111];
        byte value = reg == null ? gb.readNextByte() : gb.reg().readByte(reg);

        byte a = gb.reg().readByte(Register.A);

        gb.reg().writeByte(Register.A, a ^ value);

        gb.reg().setFlagIf(Flags.Z, (a ^ value) == 0);
        gb.reg().resetFlag(Flags.N);
        gb.reg().resetFlag(Flags.H);
        gb.reg().resetFlag(Flags.C);
    }

}
