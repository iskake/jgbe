package tland.gb.cpu.Inst;

import tland.gb.IGameBoy;
import tland.gb.Registers.Flags;
import tland.gb.Registers.RegisterIndex;

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
    public void doOp(IGameBoy gb, int opcode) {
        gb.reg().writeRegisterByte(RegisterIndex.A, ~gb.reg().readRegisterByte(RegisterIndex.A));
        gb.reg().setFlag(Flags.N);
        gb.reg().setFlag(Flags.H);
    }
    
}
