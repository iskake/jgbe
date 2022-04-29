package tland.emu.cpu.inst;

import tland.emu.IEmulator;

/**
 * Abstract implementation of an instruction.
 * 
 * @author Tarjei Landøy
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
     * @param emu    IEmulator object to read/write values.
     * @param opcode The opcode of the instruction, used for special cases.
     */
    public abstract void doOp(IEmulator emu, int opcode);

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