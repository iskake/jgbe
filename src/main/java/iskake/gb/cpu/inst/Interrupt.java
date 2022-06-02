package iskake.gb.cpu.inst;

import iskake.gb.IGameBoy;

/**
 * {@code di} and {@code ei} instructions. In the case of JGBE, these instructions
 * do nothing (equivalent to {@code nop}.)
 * <p>
 * In the Game Boy's CPU, the instructions are used for disabling and enabling
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
    public void doOp(IGameBoy emu, int opcode) {
        // Since there are no interrupts in JGBE, these instructions
        // do nothing.
    }

}
