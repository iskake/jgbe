package iskake.jgbe.core.gb.cpu.inst;

import iskake.jgbe.core.gb.IGameBoy;

/**
 * {@code di} and {@code ei} instructions for disabling and enabling
 * the Interrupt Master Enable CPU flag respectively.
 * 
 * <p>
 * Implements opcodes: {@code di} and {@code ei}
 */
public class Interrupt implements Instruction {

    private static final int OP_EI = 0xf3;
    private static final int OP_DI = 0xfb;

    @Override
    public void doOp(IGameBoy gb, int opcode) {
        switch (opcode) {
            case OP_EI -> gb.disableInterrupts();
            case OP_DI -> gb.waitEnableInterrupts();
            default -> throw new IllegalInstructionException();
        }
    }

}
