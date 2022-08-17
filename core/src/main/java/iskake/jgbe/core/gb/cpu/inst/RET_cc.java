package iskake.jgbe.core.gb.cpu.inst;

import iskake.jgbe.core.gb.IGameBoy;

/**
 * Return from subroutine, enable interrupts if the instruction is {@code reti}.
 * 
 * <p>
 * Implements opcodes: {@code ret}, {@code ret cc} and {@code reti}
 */
public class RET_cc implements Instruction {
    private static final int OP_RETI = 0xd9;

    @Override
    public void doOp(IGameBoy gb, int opcode) {
        Conditions condition = (opcode & 1) == 0 ? Conditions.tableIndex[(opcode & 0b111000) >> 3] : Conditions.NONE;

        if (Conditions.conditionSatisfied(gb.reg(), condition)) {
            if (opcode == OP_RETI) {
                // reti
                gb.enableInterrupts();
            }

            gb.pc().set(gb.sp().pop());
        }
    }

}
