package tland.gb.cpu.Inst;

import tland.gb.IGameBoy;
import tland.gb.Registers.RegisterIndex;

/**
 * Jump to address (set PC), with optional condition.
 * 
 * <p>
 * Implements opcodes: {@code jp $n16}, {@code jp nz, $n16},
 * {@code jp nc, $n16}, {@code jp z, $n16}, {@code jp c, $n16} and
 * {@code jp hl}
 */
public class JP_cc_nn extends Instruction {
    private final Conditions condition;

    public JP_cc_nn(String name, Conditions condition) {
        super(name);
        this.condition = condition;
    }

    @Override
    public void doOp(IGameBoy gb, int opcode) {
        short address;

        if (opcode == 0xe9) {
            address = gb.reg().readRegisterShort(RegisterIndex.HL);
        } else {
            address = gb.readNextShort();
        }

        if (Conditions.conditionSatisfied(gb.reg(), condition)) {
            gb.pc().set(address);
        }
    }

}
