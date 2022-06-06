package iskake.jgbe.core.gb.cpu.inst;

import iskake.jgbe.core.gb.GameBoy;
import iskake.jgbe.core.gb.IGameBoy;

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
    public void doOp(IGameBoy gb, int opcode) {
        if (gb instanceof GameBoy gameboy) { // TODO!!!
            switch (opcode) {
                case 0xf3 -> gameboy.disableInterrupts();
                case 0xfb -> gameboy.enableInterrupts(true);
                default -> throw new IllegalInstructionException();
            }
        }
    }

}
