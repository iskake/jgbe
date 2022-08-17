package iskake.jgbe.core.gb.cpu.inst;

import iskake.jgbe.core.gb.IGameBoy;

/**
 * Prefixed instructions.
 * 
 * Implements opcodes: {@code $CB $xx}
 */
public class Prefixed {
    private static final Instruction ROT_INST = ROT::rot;
    private static final Instruction BIT_INST = BIT::doOp;

    public static void doOp(IGameBoy gb, int opcode) {
        /*
         * Instead of creating a whole table to hold all prefixed opcodes (as in [1]),
         * it is possible to find each 'instruction type' by looking at the value of the
         * opcode (again, arranged as in [1]).
         * The prefixed opcodes can be indexed like so:
         *     00-3f => Rotate/shift instructions
         *     40-ff => Bitwise instructions
         * See the BIT and ROT classes for more detailed info.
         * 
         * [1] https://gbdev.io/gb-opcodes/optables/#prefixed
         */
        int nextOpcode = Byte.toUnsignedInt(gb.readNextByte());

        if (nextOpcode >> 6 == 0)
            ROT_INST.doOp(gb, nextOpcode);
        else
            BIT_INST.doOp(gb, nextOpcode);
    }

}
