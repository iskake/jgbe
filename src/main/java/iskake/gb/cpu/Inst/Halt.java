package iskake.gb.cpu.Inst;

import iskake.gb.GameBoy;

/**
 * Opcodes that 'halt' the operation of the Game Boy.
 * 
 * <p>
 * Implements opcodes: {@code stop} and {@code halt}
 */
public class Halt extends Instruction {

    public Halt(String name) {
        super(name);
    }

    @Override
    public void doOp(GameBoy gb, int opcode) {
        switch (opcode) {
            case 0x10 -> gb.stop();
            case 0x76 -> gb.halt();
            default -> throw new IllegalInstructionException();
        }
    }
}
