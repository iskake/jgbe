package iskake.gb.cpu.inst;

import iskake.gb.GameBoy;
import iskake.gb.Registers.Flags;
import iskake.gb.Registers.RegisterIndex;

/**
 * Logical OR instruction. Takes A OR the register/value nn ({@code a | nn})
 * 
 * <p>
 * Implements opcodes: {@code or r8} and {@code or $n8}
 */
public class OR_nn extends Instruction {
    public final RegisterIndex reg;

    public OR_nn(String name, RegisterIndex reg) {
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

        gb.reg.writeRegisterByte(RegisterIndex.A, a | value);

        gb.reg.setFlagConditional(Flags.Z, (a | value) == 0);
        gb.reg.resetFlag(Flags.N);
        gb.reg.resetFlag(Flags.H);
        gb.reg.resetFlag(Flags.C);
    }

}
