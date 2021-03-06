package iskake.jgbe.core.gb.cpu.inst;

import iskake.jgbe.core.gb.IGameBoy;

/**
 * Abstract implementation of an instruction.
 */
public abstract class Instruction {
    private String name;

    /**
     * Instruction constructor.
     * 
     * @param name The name of the instruction.
     */
    public Instruction(String name) {
        this.name = name;
    }

    /**
     * Do the operation of the instruction.
     * 
     * @param gb    IGameBoy object to read/write values.
     * @param opcode The opcode of the instruction, used for special cases.
     */
    public abstract void doOp(IGameBoy gb, int opcode);

    /**
     * Get the name of the instruction.
     * 
     * @return The name of the instruction, given in the constructor or specified
     *         internally.
     */
    public String getName() {
        return name;
    }
}