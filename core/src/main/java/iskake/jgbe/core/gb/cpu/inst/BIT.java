package iskake.jgbe.core.gb.cpu.inst;

import iskake.jgbe.core.gb.IGameBoy;
import iskake.jgbe.core.gb.Registers.Flags;
import iskake.jgbe.core.gb.Registers.Register;
import iskake.jgbe.core.Bitwise;

/**
 * Bitwise check/set/reset instructions.
 * 
 * Implements opcodes: {@code bit u3, r8}, {@code res u3, r8} and
 * {@code set u3, r8}
 */
public class BIT extends Instruction {
    public int opcode;

    // Values from bit 7-6 from opcode
    private static final int BIT_VAL = 0b01;
    private static final int RES_VAL = 0b10;
    private static final int SET_VAL = 0b11;

    public BIT() {
        super("BIT_INST");
    }

    @Override
    public void doOp(IGameBoy gb, int opcode) {
        /*
         * The prefixed Bitwise opcodes can be indexed like so:
         * 40-ff => Bitwise instructions
         * 40-7f => BIT u3, r8
         * 80-bf => RES u3, r8
         * c0-ff => SET u3, r8
         */

        // Make sure opcode only occupies 8 bits.
        opcode = Bitwise.intAsByte(opcode);

        int opcodeType = (opcode >> 6);
        int bitNum = (opcode >> 3) & 0b111;
        int regNum = opcode & 0b111;
        Register reg = Register.tableByte[regNum];

        switch (opcodeType) {
            case BIT_VAL -> {
                boolean bitIsSet = gb.reg().isBitSet(reg, bitNum);

                gb.reg().setFlagIf(Flags.Z, !bitIsSet);
                gb.reg().resetFlag(Flags.N);
                gb.reg().setFlag(Flags.H);
            }
            case RES_VAL -> gb.reg().resetBit(reg, bitNum);
            case SET_VAL -> gb.reg().setBit(reg, bitNum);
            default -> throw new IllegalInstructionException(
                    "Value %02x is not a valid bitwise instruction.".formatted(opcodeType));
        }
    }

    /**
     * Unsupported, use {@code getFixedName} instead.
     * 
     * @throws IllegalInstructionException Always.
     * @return Nothing, will throw an exception instead.
     */
    @Override
    public String getName() {
        throw new IllegalInstructionException("getName is not supported, use getFixedName instead.");
    }

    /**
     * Get the correct instruction name.
     * 
     * @param opcode The opcode to get the correct name from. Needs to be specified
     *               because of how the opcodes are set up.
     * @return The correct name of the instruction, based on the value in
     *         {@code opcode}.
     */
    public static String getFixedName(int opcode) {
        int opcodeType = (opcode >> 6);
        int bitNum = (opcode >> 3) & 0b111;
        int regNum = opcode & 0b111;
        Register r = Register.tableByte[regNum];

        String opcodeName = switch (opcodeType) {
            case BIT_VAL -> "bit";
            case RES_VAL -> "res";
            case SET_VAL -> "set";
            default -> "invalid";
        };

        String regName = r == Register.HL ? "[hl]" : r.name().toLowerCase();

        return String.format("%s %d, %s", opcodeName, bitNum, regName);
    }

}
