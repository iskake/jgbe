package iskake.jgbe.core.gb.cpu.inst;

import iskake.jgbe.core.gb.IGameBoy;

/**
 * Opcodes that 'halt' the operation of the GameBoy.
 * 
 * <p>
 * Implements opcodes: {@code stop} and {@code halt}
 */
public class Halt extends Instruction {

    private static final int OP_STOP = 0x10;
    private static final int OP_HALT = 0x76;

    public Halt(String name) {
        super(name);
    }

    @Override
    public void doOp(IGameBoy gb, int opcode) {
        switch (opcode) {
            case OP_STOP -> gb.stop();
            case OP_HALT -> gb.halt();
        }
    }
}
