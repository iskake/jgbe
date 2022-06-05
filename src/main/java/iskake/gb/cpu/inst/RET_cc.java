package iskake.gb.cpu.inst;

import iskake.gb.GameBoy;
import iskake.gb.IGameBoy;

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
            if (gb instanceof GameBoy gameboy) { // TODO!!!!
                if (opcode == 0xd9) {
                    // reti
                    gameboy.enableInterrupts(false);
                }
            }
            // Note: reti does not do anything different in JGBE.
            gb.pc().set(gb.sp().pop());
        }
    }

}
