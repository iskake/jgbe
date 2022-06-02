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
    public void doOp(IGameBoy emu, int opcode) {
        switch (opcode) {
            case 0x10 -> emu.stop();
            case 0x76 -> emu.halt(emu.reg().readRegisterShort(RegisterIndex.HL));
        }
    }
}
