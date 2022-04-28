package tland.emu.cpu.inst;

import tland.emu.IEmulator;

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
    public void doOp(IEmulator emu, int opcode) {
        if (Conditions.conditionSatisfied(emu.reg(), condition)) {
            // Note: reti does not do anything different in JGBE.
            emu.pc().set(emu.sp().pop());
        }
    }

}
