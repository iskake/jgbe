package iskake.gb.cpu.inst;

import iskake.gb.IGameBoy;
import iskake.gb.Registers.RegisterIndex;

/**
 * Opcodes that 'halt' the operation of the GameBoy.
 * 
 * <p>
 * Implements opcodes: {@code stop} and {@code halt}
 */
public class Halt extends Instruction {

    public Halt(String name) {
        super(name);
    }

    @Override
    public void doOp(IGameBoy gb, int opcode) {
        switch (opcode) {
            case 0x10 -> gb.stop();
            case 0x76 -> gb.halt(gb.reg().readRegisterShort(RegisterIndex.HL));
        }
    }
}
