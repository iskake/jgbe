package tland.emu.cpu;

import tland.emu.IEmulator;

public class CPU {
    private final IEmulator emu;

    public CPU(IEmulator emu) {
        this.emu = emu;
    }

    /**
     * 'Step' the CPU forward by a single instruction.
     */
    public void step() {
        short oldPC = emu.pc().get();

        byte opcode = emu.readNextByte();
        Opcodes.getOpcode(opcode).doOp(emu, Byte.toUnsignedInt(opcode));

        short newPC = emu.pc().get();

        if (oldPC == newPC) {
            System.out.println("\nInfinite loop detected! Exiting.");
            System.exit(0);
        }
    }

    /**
     * Print the name of the next instruction to be extecuted.
     */
    public void printNextInstruction() {
        short address = emu.pc().get();
        byte opcode = emu.readMemoryAddress(address);
        System.out.printf("%02x -> %s\n", opcode, Opcodes.getInstructionName(emu, address));
    }

}
