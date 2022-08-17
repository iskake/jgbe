package iskake.jgbe.core.gb.cpu.inst;

import iskake.jgbe.core.gb.IGameBoy;

/**
 * Opcodes that 'halt' the operation of the GameBoy.
 * 
 * <p>
 * Implements opcodes: {@code stop} and {@code halt}
 */
public class Halt {

    public static void halt(IGameBoy gb, int opcode) {
        gb.halt();
    }

    public static void stop(IGameBoy gb, int opcode) {
        gb.stop();
    }
}
