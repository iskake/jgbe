package tland.gb.cpu.Inst;

import tland.gb.IGameBoy;

/**
 * Return from subroutine, enable interrupts if the instruction is {@code reti}.
 * 
 * <p>
 * Implements opcodes: {@code ret}, {@code ret cc} and {@code reti}
 */
public class RET_cc extends Instruction {
    private final Conditions condition;

    public RET_cc(String name, Conditions condition) {
        super(name);
        this.condition = condition;
    }

    @Override
    public void doOp(IGameBoy gb, int opcode) {
        if (Conditions.conditionSatisfied(gb.reg(), condition)) {
            if (opcode == 0xd9) {
                // reti
                gb.undecidedEI();
            }
            gb.pc().set(gb.sp().pop());
        }
    }

}
