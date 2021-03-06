package iskake.jgbe.core.gb.cpu.inst;

import iskake.jgbe.core.gb.IGameBoy;

/**
 * Jump to the next address + the signed value $e8, with optional condition.
 * 
 * <p>
 * Implements opcodes: {@code jr $e8}, {@code jr nz, $e8},
 * {@code jr nc, $e8}, {@code jr z, $e8} and {@code jr c, $e8}
 */
public class JR_cc_e8 extends Instruction {
    private final Conditions condition;

    public JR_cc_e8(String name, Conditions condition) {
        super(name);
        this.condition = condition;
    }

    @Override
    public void doOp(IGameBoy gb, int opcode) {
        short value = gb.readNextByte();

        if (Conditions.conditionSatisfied(gb.reg(), condition)) {
            short address = gb.pc().get();
            gb.pc().set((short) (address + value));
        }
    }

}
