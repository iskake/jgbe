package iskake.jgbe.core.gb.cpu.inst;

import iskake.jgbe.core.gb.IGameBoy;

/**
 * Illegal instruction. Will only throw an IllegalInstructionException.
 */
public class IllegalInst {

    public static void illegal(IGameBoy gb, int opcode) {
        throw new IllegalInstructionException();
    }
    
}
