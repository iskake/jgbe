package iskake.gb.cpu.Inst;

import iskake.gb.GameBoy;

/**
 * {@code di} and {@code ei} instructions for disabling and enabling
 * the Interrupt Master Enable CPU flag respectively.
 * 
 * <p>
 * Implements opcodes: {@code di} and {@code ei}
 */
public class Interrupt extends Instruction {

    public Interrupt(String name) {
        super(name);
    }

    @Override
    public void doOp(GameBoy gb, int opcode) {
        switch (opcode) {
            case 0xf3 -> gb.disableInterrupts();
            case 0xfb -> gb.enableInterrupts(true);
            default -> throw new IllegalInstructionException();
        }
    }

}
