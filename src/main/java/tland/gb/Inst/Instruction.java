package tland.gb.Inst;

import tland.gb.GameBoy;

/**
 * Abstract implementation of an instruction
 */
public abstract class Instruction {
    private String name;

    public Instruction(String name) {
        this.name = name;
    }

    /**
     * Do the operation of the instruction.
     * @param gb
     * @param opcode
     */
    public abstract void doOp(GameBoy gb, int opcode);

    /**
     * Get the name of the instruction.
     * @return The name of the instruction, given in constructor.
     */
    public String getName() {
        return name;
    }
}