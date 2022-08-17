package iskake.jgbe.core.gb.cpu.inst;

import iskake.jgbe.core.gb.IGameBoy;

/**
 * {@code di} and {@code ei} instructions for disabling and enabling
 * the Interrupt Master Enable CPU flag respectively.
 * 
 * <p>
 * Implements opcodes: {@code di} and {@code ei}
 */
public class Interrupt {

    public static void ei(IGameBoy gb, int opcode) {
        gb.waitEnableInterrupts();
    }

    public static void di(IGameBoy gb, int opcode) {
        gb.disableInterrupts();
    }

}
