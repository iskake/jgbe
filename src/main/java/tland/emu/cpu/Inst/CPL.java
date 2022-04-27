package tland.emu.cpu.Inst;

import tland.emu.IEmulator;
import tland.emu.Registers.Flags;
import tland.emu.Registers.RegisterIndex;

/**
 * ComPLement the accumulator ({@code a = ~a})
 * 
 * <p>
 * Implements opcode: {@code cpl}
 */
public class CPL extends Instruction {

    public CPL() {
        super("cpl");
    }

    @Override
    public void doOp(IEmulator emu, int opcode) {
        emu.reg().writeRegisterByte(RegisterIndex.A, ~emu.reg().readRegisterByte(RegisterIndex.A));
        emu.reg().setFlag(Flags.N);
        emu.reg().setFlag(Flags.H);
    }
    
}
