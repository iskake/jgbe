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
    public final Register reg;

    public XOR_nn(String name, Register reg) {
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

        byte a = gb.reg().readRegisterByte(Register.A);

        gb.reg().writeRegisterByte(Register.A, a ^ value);

        gb.reg().setFlagConditional(Flags.Z, (a ^ value) == 0);
        gb.reg().resetFlag(Flags.N);
        gb.reg().resetFlag(Flags.H);
        gb.reg().resetFlag(Flags.C);
    }

}
