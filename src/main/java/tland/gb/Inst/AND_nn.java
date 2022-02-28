package tland.gb.Inst;

import tland.gb.GameBoy;
import tland.gb.Registers.Flags;
import tland.gb.Registers.RegisterIndex;

/**
 * Logical AND instruction.
 * 
 * <p>
 * Implements opcodes: {@code and r8} and {@code and $n8}
 */
public class AND_nn extends Instruction {
    private final RegisterIndex reg;

    public AND_nn(String name, RegisterIndex reg) {
        super(name);
        this.reg = reg;
    }

    @Override
    public void doOp(GameBoy gb, int opcode) {
        byte value;

        if (reg.equals(null)) {
            value = gb.readNextByte();
        } else {
            value = gb.reg.readRegisterByte(reg);
        }

        byte a = gb.reg.readRegisterByte(RegisterIndex.A);

        gb.reg.writeRegisterByte(RegisterIndex.A, a & value);

        if ((a & value) == 0) {
            gb.reg.setFlag(Flags.Z);
        }

        gb.reg.resetFlag(Flags.N);

        gb.reg.setFlag(Flags.H);

        gb.reg.resetFlag(Flags.C);
    }

}
