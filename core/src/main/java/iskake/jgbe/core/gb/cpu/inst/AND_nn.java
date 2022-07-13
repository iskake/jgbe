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
public class AND_nn extends Instruction {
    private final Register reg;

    public AND_nn(String name, Register reg) {
        super(name);
        this.reg = reg;
    }

    @Override
    public void doOp(IGameBoy gb, int opcode) {
        byte value;

        if (reg == null) {
            value = gb.readNextByte();
        } else {
            value = gb.reg().readByte(reg);
        }

        byte a = gb.reg().readByte(Register.A);

        gb.reg().writeByte(Register.A, a & value);

        gb.reg().setFlagConditional(Flags.Z, (a & value) == 0);
        gb.reg().resetFlag(Flags.N);
        gb.reg().setFlag(Flags.H);
        gb.reg().resetFlag(Flags.C);
    }

}
