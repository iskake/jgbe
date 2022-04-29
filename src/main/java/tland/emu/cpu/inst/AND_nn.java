package tland.emu.cpu.inst;

import tland.emu.IEmulator;
import tland.emu.Registers.Flags;
import tland.emu.Registers.RegisterIndex;

/**
 * Logical AND instruction. Takes A AND the register/value nn ({@code a & nn})
 * 
 * <p>
 * Implements opcodes: {@code and r8} and {@code and $n8}
 * 
 * @author Tarjei Landøy
 */
public class AND_nn extends Instruction {
    private final RegisterIndex reg;

    public AND_nn(String name, RegisterIndex reg) {
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

        emu.reg().writeRegisterByte(RegisterIndex.A, a & value);

        emu.reg().setFlagConditional(Flags.Z, (a & value) == 0);
        emu.reg().resetFlag(Flags.N);
        emu.reg().setFlag(Flags.H);
        emu.reg().resetFlag(Flags.C);
    }

}
