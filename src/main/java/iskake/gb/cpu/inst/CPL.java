package iskake.gb.cpu.inst;

import iskake.gb.GameBoy;
import iskake.gb.Registers.Flags;
import iskake.gb.Registers.RegisterIndex;

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
    public void doOp(GameBoy gb, int opcode) {
        gb.reg.writeRegisterByte(RegisterIndex.A, ~gb.reg.readRegisterByte(RegisterIndex.A));
        gb.reg.setFlag(Flags.N);
        gb.reg.setFlag(Flags.H);
    }
    
}
