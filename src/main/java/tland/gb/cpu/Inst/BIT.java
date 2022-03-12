package tland.gb.cpu.Inst;

import tland.gb.GameBoy;
import tland.gb.Registers.RegisterIndex;

/**
 * BIT instructions.
 * 
 * Implements opcodes: {@code bit b, r8}, {@code res b, r8} and
 * {@code set b, r8}
 */
public class BIT extends Instruction {
    public int opcode;

    public BIT() {
        super("BIT_INST");
    }

    @Override
    public void doOp(GameBoy gb, int opcode) {
        /*
         * The prefixed Bitwise opcodes can be indexed like so:
         * 40-ff => Bitwise instructions
         *     40-7f => BIT b, r8
         *         40-47 => BIT 0, r8
         *         48-4f => BIT 1, r8
         *         50-57 => BIT 2, r8
         *         58-5f => BIT 3, r8
         *         60-67 => BIT 4, r8
         *         68-6f => BIT 5, r8
         *         70-77 => BIT 6, r8
         *         78-7f => BIT 7, r8
         *     80-bf => RES b, r8
         *         80-87 => RES 0, r8
         *         88-8f => RES 1, r8
         *         90-97 => RES 2, r8
         *         98-9f => RES 3, r8
         *         a0-a7 => RES 4, r8
         *         a8-af => RES 5, r8
         *         b0-b7 => RES 6, r8
         *         b8-bf => RES 7, r8
         *     c0-ff => SET b, r8
         *         c0-c7 => SET 0, r8
         *         c8-cf => SET 1, r8
         *         d0-d7 => SET 2, r8
         *         d8-df => SET 3, r8
         *         e0-e7 => SET 4, r8
         *         e8-ef => SET 5, r8
         *         f0-f7 => SET 6, r8
         *         f8-ff => SET 7, r8
         */
        int opcodeType = (opcode >> 6);
        int bitNum = (opcode >> 3) & 0b111;
        int regNum = opcode & 0b111;
        System.out.printf("OPCODE: %02x\nInstruction: %s %d, %s\n", opcode,
                opcodeType == 1 ? "bit" : (opcodeType == 2 ? "res" : "set"), bitNum,
                RegisterIndex.values()[(regNum + 2) % 8]);

    }

}
