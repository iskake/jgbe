package iskake.jgbe.core.gb.cpu.inst;

import iskake.jgbe.core.gb.IGameBoy;

/**
 * Call the specified address.
 * 
 * <p>
 * Implements opcodes: {@code call $n16}, {@code call nz, $n16},
 * {@code call nc, $n16}, {@code call z, $n16} and {@code call c, $n16}
 */
public class CALL {

    public static void call_cc_n16(IGameBoy gb, int opcode) {
        short address = gb.readNextShort();

        Conditions condition = (opcode & 0b111) == 0b100 ? Conditions.tableIndex[(opcode & 0b111000) >> 3] : Conditions.NONE;

        if (Conditions.conditionSatisfied(gb.reg(), condition)) {
            gb.sp().push(gb.pc().get());
            gb.pc().set(address);
        }
    }

}
