package iskake.jgbe.core.gb.cpu.inst;

import iskake.jgbe.core.gb.IGameBoy;
import iskake.jgbe.core.gb.Registers.Flags;
import iskake.jgbe.core.gb.Registers.RegisterIndex;

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
