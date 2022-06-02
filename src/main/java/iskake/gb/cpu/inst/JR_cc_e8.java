package iskake.gb.cpu.inst;

import iskake.gb.IGameBoy;

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
    public void doOp(IGameBoy emu, int opcode) {
        short value = emu.readNextByte();

        if (Conditions.conditionSatisfied(emu.reg(), condition)) {
            short address = emu.pc().get();
            emu.pc().set((short) (address + value));
        }
    }

}
