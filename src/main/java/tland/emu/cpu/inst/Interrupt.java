package tland.emu.cpu.inst;

import tland.emu.IEmulator;

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
    public void doOp(IEmulator emu, int opcode) {
        switch (opcode) {
            case 0xf3 -> emu.undecidedDI();
            case 0xfb -> emu.undecidedEI();
            default -> throw new IllegalInstructionException();
        }
    }

}
