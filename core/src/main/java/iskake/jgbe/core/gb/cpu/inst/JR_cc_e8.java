package iskake.jgbe.core.gb.cpu.inst;

import iskake.jgbe.core.gb.IGameBoy;

/**
 * Jump to the next address + the signed value $e8, with optional condition.
 * 
 * <p>
 * Implements opcodes: {@code jr $e8}, {@code jr nz, $e8},
 * {@code jr nc, $e8}, {@code jr z, $e8} and {@code jr c, $e8}
 */
public class JR_cc_e8 {

    public static void jr_cc_e8(IGameBoy gb, int opcode) {
        short value = gb.readNextByte();

        Conditions condition = (opcode & 0b111000) != 0b011000 ? Conditions.tableIndex[((opcode & 0b111000) >> 3) - 4] : Conditions.NONE;

        if (Conditions.conditionSatisfied(gb.reg(), condition)) {
            short address = gb.pc().get();
            gb.pc().set((short) (address + value));
        }
    }

}
