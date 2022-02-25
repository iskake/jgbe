package tland.gb.Inst;

import tland.gb.CPU;
import tland.gb.GameBoy;

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
    public abstract void doOp(GameBoy gb, int opcode);

    public String getName() {
        return name;
    }
}