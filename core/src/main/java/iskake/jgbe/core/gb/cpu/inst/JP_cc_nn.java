package iskake.jgbe.core.gb.cpu.inst;

import iskake.jgbe.core.gb.IGameBoy;
import iskake.jgbe.core.gb.Registers.Register;

/**
 * Jump to address (set PC), with optional condition.
 * 
 * <p>
 * Implements opcodes: {@code jp $n16}, {@code jp nz, $n16},
 * {@code jp nc, $n16}, {@code jp z, $n16}, {@code jp c, $n16} and
 * {@code jp hl}
 */
public class JP_cc_nn implements Instruction {
    private static final int OP_JP_HL = 0xe9;

    @Override
    public void doOp(IGameBoy gb, int opcode) {
        short address = opcode == OP_JP_HL ? gb.reg().readShort(Register.HL) : gb.readNextShort();

        Conditions condition = (opcode & 0b111) == 0b10 ? Conditions.tableIndex[(opcode & 0b111000) >> 3] : Conditions.NONE;

        if (Conditions.conditionSatisfied(gb.reg(), condition)) {
            if (opcode != 0xe9) {
                gb.pc().set(address);
            } else {
                gb.pc().setNoCycle(address);
            }
        }
    }

}
