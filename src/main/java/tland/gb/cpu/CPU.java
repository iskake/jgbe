package tland.gb.cpu;

import tland.Bitwise;
import tland.gb.GameBoy;
import tland.gb.InterruptHandler;
import tland.gb.cpu.Inst.Prefixed;

public class CPU {
    private final GameBoy gb;
    private final InterruptHandler interrupts;

    /** The CPU clock speed, measured in Hz. */
    public static final int CLOCK_SPEED = 0x400000;

    public CPU(GameBoy gameBoy, InterruptHandler interrupts) {
        this.gb = gameBoy;
        this.interrupts = interrupts;
    }

    /**
     * 'Step' the CPU forward by a single instruction.
     */
    public void step() {
        short oldPC = gb.pc.get();
        boolean ime_wait = interrupts.waitingIME();
        
        // Call waiting interrupts (if any)
        if (interrupts.callWaiting()) {
            return;
        }

        byte opcode = gb.readNextByte();
        Opcodes.getOpcode(opcode).doOp(gb, Byte.toUnsignedInt(opcode));

        if (ime_wait) {
            interrupts.enable(false);
        }

        short newPC = gb.pc.get();

        // Temp.
        if (oldPC == newPC && !interrupts.enabled()) {
            gb.printHRAM();
            System.out.println("\nInfinite loop! Exiting.");
            System.exit(0);
        }
    }

    /**
     * Print the name of the next instruction to be extecuted.
     */
    public void printNextInstruction() {
        short address = gb.pc.get();
        byte opcode = gb.readMemoryNoCycle(address);
        System.out.printf("%02x -> %s\n", opcode, getInstructionName(address));
    }

    /**
     * Get the name of the instruction based on the opcode at the specified memory
     * address.
     * 
     * @param address The address of the instruction.
     * @return The name of the instruction.
     */
    public String getInstructionName(short address) {
        byte opcode = gb.readMemoryNoCycle(address);

        String opcodeName;

        if (Opcodes.getOpcode(opcode) instanceof Prefixed o) {
            opcode = gb.readMemoryNoCycle(Bitwise.toShort(address + 1));
            opcodeName = o.getPrefixedName(Byte.toUnsignedInt(opcode));
        } else {
            opcodeName = Opcodes.getOpcode(opcode).getName();
        }

        opcodeName = opcodeName.replace("_N8", "%02x");
        opcodeName = opcodeName.replace("_N16", "%2$02x%1$02x");

        byte[] operands = {
                gb.readMemoryNoCycle(Bitwise.toShort(address + 1)),
                gb.readMemoryNoCycle(Bitwise.toShort(address + 2))
        };

        return String.format(opcodeName, operands[0], operands[1]);
    }

}
