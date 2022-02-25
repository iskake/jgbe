package tland.gb.Inst;

import tland.gb.GameBoy;
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
    public void doOp(GameBoy gb, int opcode) {
        gb.reg.writeRegisterByte(r1, gb.reg.readRegisterByte(r2));
    }
    
}
