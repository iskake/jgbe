package iskake.jgbe.core.gb.cpu.inst;

import iskake.jgbe.core.gb.IGameBoy;

/**
 * Abstract implementation of an instruction.
 */
@FunctionalInterface
public interface Instruction {
    /**
     * Do the operation of the instruction.
     * 
     * @param gb    IGameBoy object to read/write values.
     * @param opcode The opcode of the instruction, used for special cases.
     */
    void doOp(IGameBoy gb, int opcode);
}