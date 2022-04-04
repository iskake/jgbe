package tland.gb.cpu.Inst;

import tland.gb.GameBoy;
import tland.gb.Registers.Flags;
import tland.gb.Registers.RegisterIndex;

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
    public void doOp(GameBoy gb, int opcode) {
        byte value;
        
        if (reg == null) {
            value = gb.readNextByte();
        } else {
            value = gb.reg.readRegisterByte(reg);
        }

        byte a = gb.reg.readRegisterByte(RegisterIndex.A);

        gb.reg.writeRegisterByte(RegisterIndex.A, a ^ value);

        gb.reg.setFlagConditional(Flags.Z, (a ^ value) == 0);
        gb.reg.resetFlag(Flags.N);
        gb.reg.resetFlag(Flags.H);
        gb.reg.resetFlag(Flags.C);
    }

}
