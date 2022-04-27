package tland.emu.cpu.inst;

import tland.emu.IEmulator;
import tland.emu.Registers.Flags;
import tland.emu.Registers.RegisterIndex;

/**
 * Rotate and shift instructions.
 * 
 * <p>
 * Implements opcodes: {@code rlc r8}, {@code rrc r8},
 * {@code rl r8}, {@code rr r8}, {@code sla r8}, {@code sra r8},
 * {@code swap r8}, and {@code srl r8}
 */
public class ROT extends Instruction {
    private final boolean prefixed;

    private final int RLC_VAL = 0b000;
    private final int RRC_VAL = 0b001;
    private final int RL_VAL = 0b010;
    private final int RR_VAL = 0b011;
    private final int SLA_VAL = 0b100;
    private final int SRA_VAL = 0b101;
    private final int SWAP_VAL = 0b110;
    private final int SRL_VAL = 0b111;

    public ROT() {
        super("ROT_INST");
        prefixed = true;
    }

    public ROT(String name) {
        super(name);
        prefixed = false;
    }

    @Override
    public void doOp(IEmulator emu, int opcode) {
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
        RegisterIndex reg = RegisterIndex.tableIndex[regNum];

        int result = switch (opcodeType) {
            case RLC_VAL -> emu.reg().rotateLeft(reg, false);
            case RRC_VAL -> emu.reg().rotateRight(reg, false);
            case RL_VAL -> emu.reg().rotateLeft(reg, true);
            case RR_VAL -> emu.reg().rotateRight(reg, true);
            case SLA_VAL -> emu.reg().shiftLeft(reg);
            case SRA_VAL -> emu.reg().shiftRight(reg, false);
            case SWAP_VAL -> emu.reg().swap(reg);
            case SRL_VAL -> emu.reg().shiftRight(reg, true);
            default -> throw new IllegalInstructionException(
                    "Value %d is not a valid bitwise instruction.".formatted(opcodeType));
        };

        if (prefixed) {
            emu.reg().setFlagConditional(Flags.Z, emu.reg().readRegisterByte(reg) == 0);
        } else {
            emu.reg().resetFlag(Flags.Z);
        }

        emu.reg().resetFlag(Flags.N);
        emu.reg().resetFlag(Flags.H);
        emu.reg().setFlagConditional(Flags.C, result == 1);
    }

    public String getFixedName(int opcode) {
        int opcodeType = (opcode >> 3);
        int regNum = opcode & 0b111;

        String opcodeName = switch (opcodeType) {
            case RLC_VAL -> "rlc";
            case RRC_VAL -> "rrc";
            case RL_VAL -> "rl";
            case RR_VAL -> "rr";
            case SLA_VAL -> "sla";
            case SRA_VAL -> "sra";
            case SWAP_VAL -> "swap";
            case SRL_VAL -> "srl";
            default -> "invalid";
        };

        RegisterIndex r = RegisterIndex.tableIndex[regNum];
        String regName = switch (r) {
            case HL -> "[hl]";
            default -> r.name().toLowerCase();
        };

        return String.format("%s %s", opcodeName, regName);
    }

}
