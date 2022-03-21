package tland.gb.cpu;

import tland.Bitwise;
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
        short oldPC = gb.getPC();

        byte opcode = gb.readNextByte();
        Opcodes.getOpcode(opcode).doOp(gb, Byte.toUnsignedInt(opcode));

        short newPC = gb.getPC();

        // Temp.
        if (oldPC == newPC) {
            gb.printHRAM();
            System.out.println("\nInfinite loop! Exiting.");
            System.exit(0);
        }
    }

    /**
     * Get the name of the instruction based on the opcode at the specified memory
     * address.
     * 
     * @param address The address of the instruction.
     * @return The name of the instruction.
     */
    public String getInstructionName(short address) {
        byte opcode = gb.readMemoryAddress(address);

        String opcodeName = Opcodes.getOpcode(opcode).getName();
        opcodeName = opcodeName.replace("_N8", "%02x");
        opcodeName = opcodeName.replace("_N16", "%2$02x%1$02x");

        byte[] operands = {
                gb.readMemoryAddress(Bitwise.toShort(address + 1)),
                gb.readMemoryAddress(Bitwise.toShort(address + 2))
        };

        return String.format(opcodeName, operands[0], operands[1]);
    }

}
