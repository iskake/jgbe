package iskake.gb.cpu.inst;

import iskake.gb.IGameBoy;
import iskake.gb.Registers.RegisterIndex;

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
    public void doOp(IGameBoy emu, int opcode) {
        short address;

        if (opcode == 0xe9) {
            address = emu.reg().readRegisterShort(RegisterIndex.HL);
        } else {
            address = emu.readNextShort();
        }

        if (Conditions.conditionSatisfied(emu.reg(), condition)) {
            emu.pc().set(address);
        }
    }

}
