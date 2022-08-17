package iskake.jgbe.core.gb.cpu.inst;

import iskake.jgbe.core.gb.IGameBoy;
import iskake.jgbe.core.gb.Registers.Flags;
import iskake.jgbe.core.gb.Registers.Register;

/**
 * Rotate and shift instructions.
 * 
 * <p>
 * Implements opcodes: {@code rlc r8}, {@code rrc r8},
 * {@code rl r8}, {@code rr r8}, {@code sla r8}, {@code sra r8},
 * {@code swap r8}, and {@code srl r8}
 */
public class ROT {
    private static final int RLC_VAL = 0b000;
    private static final int RRC_VAL = 0b001;
    private static final int RL_VAL = 0b010;
    private static final int RR_VAL = 0b011;
    private static final int SLA_VAL = 0b100;
    private static final int SRA_VAL = 0b101;
    private static final int SWAP_VAL = 0b110;
    private static final int SRL_VAL = 0b111;

    public static void rota(IGameBoy gb, int opcode) {
        rotInternal(gb, opcode, false);
    }

    public static void rot(IGameBoy gb, int opcode) {
        rotInternal(gb, opcode, true);
    }

    public static void rotInternal(IGameBoy gb, int opcode, boolean prefixed) {
        /*
         * The prefixed rotate/shift opcodes can be indexed like so:
         * 00-3f => Rotate/shift instructions
         *
         * 00-07 => RLC r8
         * 08-0f => RRC r8
         * 10-17 => RL r8
         * 18-1f => RR r8
         * 20-27 => SLA r8
         * 28-2f => SRA r8
         * 30-37 => SWAP r8
         * 38-3f => SRL r8
         */
        int opcodeType = (opcode >> 3);
        int regNum = opcode & 0b111;
        Register reg = Register.tableByte[regNum];

        int result = switch (opcodeType) {
            case RLC_VAL -> gb.reg().rotateLeft(reg, false);
            case RRC_VAL -> gb.reg().rotateRight(reg, false);
            case RL_VAL -> gb.reg().rotateLeft(reg, true);
            case RR_VAL -> gb.reg().rotateRight(reg, true);
            case SLA_VAL -> gb.reg().shiftLeft(reg);
            case SRA_VAL -> gb.reg().shiftRight(reg, false);
            case SWAP_VAL -> gb.reg().swap(reg);
            case SRL_VAL -> gb.reg().shiftRight(reg, true);
            default -> throw new IllegalInstructionException(
                    "Value %d is not a valid bitwise instruction.".formatted(opcodeType));
        };

        if (prefixed) {
            gb.reg().setFlagIf(Flags.Z, gb.reg().readByte(reg) == 0);
        } else {
            gb.reg().resetFlag(Flags.Z);
        }

        gb.reg().resetFlag(Flags.N);
        gb.reg().resetFlag(Flags.H);
        gb.reg().setFlagIf(Flags.C, result == 1);
    }

}
