package iskake.gb.cpu.Inst;

import iskake.gb.GameBoy;

/**
 * Prefixed instructions.
 * 
 * Implements opcodes: {@code $CB $xx}
 */
public class Prefixed extends Instruction {
    private final static ROT ROT_INST = new ROT();
    private final static BIT BIT_INST = new BIT();

    public Prefixed() {
        super("PREFIXED");
    }

    @Override
    public void doOp(GameBoy gb, int opcode) {
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
        opcode = Byte.toUnsignedInt(gb.readNextByte());
        Instruction inst;

        inst = (opcode >> 6 == 0) ? ROT_INST : BIT_INST;
        inst.doOp(gb, opcode);
    }

    public String getPrefixedName(int opcode) {
        return (opcode >> 6 == 0) ? ROT_INST.getFixedName(opcode) : BIT_INST.getFixedName(opcode);
    }

}
