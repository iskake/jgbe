package iskake.jgbe.core.gb.cpu.inst;

import iskake.jgbe.core.gb.IGameBoy;

/**
 * Return from subroutine, enable interrupts if the instruction is {@code reti}.
 * 
 * <p>
 * Implements opcodes: {@code ret}, {@code ret cc} and {@code reti}
 */
public class RET_cc extends Instruction {
    private static final int OP_RETI = 0xd9;

    private final Conditions condition;

    public RET_cc(String name, Conditions condition) {
        super(name);
        this.condition = condition;
    }

    @Override
    public void doOp(IGameBoy gb, int opcode) {
        if (Conditions.conditionSatisfied(gb.reg(), condition)) {
            if (opcode == OP_RETI) {
                // reti
                gb.enableInterrupts(false);
            }

            gb.pc().set(gb.sp().pop());
        }
    }

}
