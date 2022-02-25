package tland.gb.Inst;

import tland.gb.CPU;
import tland.gb.Registers.*;

/**
 * Load data from register2 into register1
 * Implements opcodes: `ld r1, r2`
 */
public final class LD_r8_r8 extends Instruction {
    private final RegisterIndex r1;
    private final RegisterIndex r2;

    /**
     * Load data from register2 into register1
     * Implements opcodes: `ld r1, r2`
     */
    public LD_r8_r8(String name, RegisterIndex r1, RegisterIndex r2) {
        super(name);

        this.r1 = r1;
        this.r2 = r2;
    }

    @Override
    public final void doOp(CPU cpu, int opcode) {
        cpu.reg.writeRegisterByte(r1, cpu.reg.readRegisterByte(r2));
    }
    
}
