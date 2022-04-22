package tland.gb.cpu.Inst;

import tland.gb.GameBoy;

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
            case 0xf3 -> gb.undecidedDI();
            case 0xfb -> gb.undecidedEI();
            default -> throw new IllegalInstructionException();
        }
    }

}
