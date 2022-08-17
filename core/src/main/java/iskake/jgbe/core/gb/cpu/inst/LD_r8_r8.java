package iskake.jgbe.core.gb.cpu.inst;

import iskake.jgbe.core.gb.IGameBoy;
import iskake.jgbe.core.gb.Registers.Register;

/**
 * Load byte from register2 into register1.
 * 
 * <p>
 * Implements opcodes: {@code ld r1, r2}, {@code ld [hli], a},
 * {@code ld [hld], a}, {@code ld a, [hli]}, {@code ld a, [hld]} and {@code ld sp, hl}
 */
public final class LD_r8_r8 extends Instruction {

    public LD_r8_r8(String name) {
        super(name);
    }

    @Override
    public void doOp(IGameBoy gb, int opcode) {
        int x = (opcode & 0b1100_0000) >> 6;
        if (x == 0) {
            ld_ptr(gb, opcode);
        } else {
            ld_r8_r8(gb, opcode);
        }
    }

    private void ld_ptr(IGameBoy gb, int opcode) {
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

    private void ld_r8_r8(IGameBoy gb, int opcode) {
        Register r1 = Register.tableByte[(opcode & 0b111000) >> 3];
        Register r2 = Register.tableByte[opcode & 0b111];
        gb.reg().writeByte(r1, gb.reg().readByte(r2));
    }

}
