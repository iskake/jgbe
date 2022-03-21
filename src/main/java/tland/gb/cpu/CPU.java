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
    }

}
