package iskake.gb.cpu.inst;

import iskake.gb.IGameBoy;
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
    public void doOp(IGameBoy emu, int opcode) {
        emu.reg().writeRegisterByte(RegisterIndex.A, ~emu.reg().readRegisterByte(RegisterIndex.A));
        emu.reg().setFlag(Flags.N);
        emu.reg().setFlag(Flags.H);
    }
    
}
