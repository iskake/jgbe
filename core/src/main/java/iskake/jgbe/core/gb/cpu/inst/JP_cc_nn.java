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
public class JP_cc_nn extends Instruction {
    private static final int OP_JP_HL = 0xe9;

    private final Conditions condition;

    public JP_cc_nn(String name, Conditions condition) {
        super(name);
        this.condition = condition;
    }

    @Override
    public void doOp(IGameBoy gb, int opcode) {
        short address;

        if (opcode == OP_JP_HL) {
            address = gb.reg().readRegisterShort(Register.HL);
        } else {
            address = gb.readNextShort();
        }

        if (Conditions.conditionSatisfied(gb.reg(), condition)) {
            if (opcode != 0xe9) {
                gb.pc().set(address);
            } else {
                gb.pc().setNoCycle(address);
            }
        }
    }

}
