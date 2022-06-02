package iskake.gb.cpu.inst;

import iskake.gb.IGameBoy;
import iskake.gb.Registers.Flags;
import iskake.gb.Registers.RegisterIndex;

/**
 * Logical XOR instruction. Takes A XOR the register/value nn ({@code a ^ nn})
 * 
 * <p>
 * Implements opcodes: {@code xor r8} and {@code xor $n8}
 */
public class XOR_nn extends Instruction {
    public final RegisterIndex reg;

    public XOR_nn(String name, RegisterIndex reg) {
        super(name);
        this.reg = reg;
    }

    @Override
    public void doOp(IGameBoy emu, int opcode) {
        byte value;
        
        if (reg == null) {
            value = emu.readNextByte();
        } else {
            value = emu.reg().readRegisterByte(reg);
        }

        byte a = emu.reg().readRegisterByte(RegisterIndex.A);

        emu.reg().writeRegisterByte(RegisterIndex.A, a ^ value);

        emu.reg().setFlagConditional(Flags.Z, (a ^ value) == 0);
        emu.reg().resetFlag(Flags.N);
        emu.reg().resetFlag(Flags.H);
        emu.reg().resetFlag(Flags.C);
    }

}
