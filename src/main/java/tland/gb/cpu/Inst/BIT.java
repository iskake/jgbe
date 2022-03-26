package tland.gb.cpu.Inst;

import tland.Bitwise;
import tland.gb.GameBoy;
import tland.gb.Registers.Flags;
import tland.gb.Registers.RegisterIndex;

/**
 * Bitwise check/set/reset instructions.
 * 
 * Implements opcodes: {@code bit u3, r8}, {@code res u3, r8} and
 * {@code set u3, r8}
 */
public class BIT extends Instruction {
    public int opcode;

    // Values from bit 7-6 from opcode
    private final int BIT_VAL = 0b01;
    private final int RES_VAL = 0b10;
    private final int SET_VAL = 0b11;

    public BIT() {
        super("BIT_INST");
    }

    @Override
    public void doOp(GameBoy gb, int opcode) {
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
        RegisterIndex reg = RegisterIndex.tableIndex[regNum];

        switch (opcodeType) {
            case BIT_VAL:
                boolean bitIsSet = gb.reg.isBitSet(reg, bitNum);

                if (!bitIsSet) {
                    gb.reg.setFlag(Flags.Z);
                } else {
                    gb.reg.resetFlag(Flags.Z);
                }
                gb.reg.resetFlag(Flags.N);
                gb.reg.setFlag(Flags.H);
                break;
            case RES_VAL:
                gb.reg.resetBit(reg, bitNum);
                break;
            case SET_VAL:
                gb.reg.setBit(reg, bitNum);
                break;
            default:
                throw new IllegalInstructionException(
                        "Value %d is not a valid bitwise instruction.".formatted(opcodeType));
        }
    }

    /**
     * Unsupported, use {@code getFixedName} instead.
     * 
     * @throws IllegalInstructionException
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
    public String getFixedName(int opcode) {
        int opcodeType = (opcode >> 6);
        int bitNum = (opcode >> 3) & 0b111;
        int regNum = opcode & 0b111;

        String opcodeName = switch (opcodeType) {
            case BIT_VAL -> "bit";
            case RES_VAL -> "res";
            case SET_VAL -> "set";
            default -> "invalid";
        };

        RegisterIndex r = RegisterIndex.tableIndex[regNum];
        String regName = switch (r) {
            case HL -> "[hl]";
            default -> r.name().toLowerCase();
        };

        return String.format("%s %d, %s", opcodeName, bitNum, regName);
    }

}
