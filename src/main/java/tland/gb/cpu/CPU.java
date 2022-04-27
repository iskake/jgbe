package tland.gb.cpu;

import tland.gb.GameBoy;

public class CPU {
    private final GameBoy gb;

    public CPU(GameBoy gameBoy) {
        this.gb = gameBoy;
    }

    /**
     * 'Step' the CPU forward by a single instruction.
     */
    public void step() {
        short oldPC = gb.pc().get();

        byte opcode = gb.readNextByte();
        Opcodes.getOpcode(opcode).doOp(gb, Byte.toUnsignedInt(opcode));

        short newPC = gb.pc().get();

        if (oldPC == newPC) {
            System.out.println("\nInfinite loop detected! Exiting.");
            System.exit(0);
        }
    }

    /**
     * Print the name of the next instruction to be extecuted.
     */
    public void printNextInstruction() {
        short address = gb.pc().get();
        byte opcode = gb.readMemoryAddress(address);
        System.out.printf("%02x -> %s\n", opcode, Opcodes.getInstructionName(gb, address));
    }

}
