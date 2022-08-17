package iskake.jgbe.core.gb.cpu.inst;

import iskake.jgbe.core.gb.IGameBoy;
import iskake.jgbe.core.gb.Registers.Register;

/**
 * Load instructions
 */
public final class LD {

    public static void ld_ptr(IGameBoy gb, int opcode) {
        boolean q = (opcode & 0b1000) != 0;
        int p = (opcode & 0b110000) >> 4;

        Register rA = Register.A;
        Register rPtr = Register.tableShortHLID[p];

        if (q) {
            gb.reg().writeByte(rA, gb.reg().readByte(rPtr));
        } else {
            gb.reg().writeByte(rPtr, gb.reg().readByte(rA));
        }

        if (p > 1) {
            // Handle HLI and HLD opcodes
            if (p == 2) {
                // HLI
                gb.reg().incShort(Register.HL);
            } else {
                // HLD
                gb.reg().decShort(Register.HL);
            }
        }
    }

    public static void ld_r8_r8(IGameBoy gb, int opcode) {
        Register r1 = Register.tableByte[(opcode & 0b111000) >> 3];
        Register r2 = Register.tableByte[opcode & 0b111];
        gb.reg().writeByte(r1, gb.reg().readByte(r2));
    }

    public static void ld_r8_n8(IGameBoy gb, int opcode) {
        Register reg = Register.tableByte[(opcode & 0b111000) >> 3];
        gb.reg().writeByte(reg, gb.readNextByte());
    }

    public static void ld_r16_n16(IGameBoy gb, int opcode) {
        Register reg = Register.tableShortSP[(opcode & 0b110000) >> 4];
        gb.reg().writeShort(reg, gb.readNextShort());
    }

}
