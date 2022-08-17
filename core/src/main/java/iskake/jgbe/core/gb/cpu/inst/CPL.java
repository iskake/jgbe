package iskake.jgbe.core.gb.cpu.inst;

import iskake.jgbe.core.gb.IGameBoy;
import iskake.jgbe.core.gb.Registers.Flags;
import iskake.jgbe.core.gb.Registers.Register;

/**
 * ComPLement the accumulator ({@code a = ~a})
 * 
 * <p>
 * Implements opcode: {@code cpl}
 */
public class CPL implements Instruction {

    @Override
    public void doOp(IGameBoy gb, int opcode) {
        gb.reg().writeByte(Register.A, ~gb.reg().readByte(Register.A));
        gb.reg().setFlag(Flags.N);
        gb.reg().setFlag(Flags.H);
    }
    
}
