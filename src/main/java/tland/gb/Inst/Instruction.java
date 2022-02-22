package tland.gb.Inst;

import tland.gb.CPU;

/**
 * Opcode
 */
public abstract class Instruction {
    private String name;

    public Instruction(String name) {
        this.name = name;
    }

    /**
     * Do the operation of the instcution.
     * @param cpu
     * @param opcode
     */
    abstract void doOp(CPU cpu, int opcode);

    public String getName() {
        return name;
    }
}