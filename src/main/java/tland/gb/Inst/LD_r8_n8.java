package tland.gb.Inst;

import tland.gb.GameBoy;
import tland.gb.Registers.RegisterIndex;

/**
 * Load byte `n8` into register
 * Implements opcodes: `ld r8, n8`
 */
public class LD_r8_n8 extends Instruction {
    private final RegisterIndex reg;

    public LD_r8_n8(String name, RegisterIndex register) {
        super(name);

        this.reg = register;
    }

    @Override
    public void doOp(GameBoy gb, int opcode) {
        gb.reg.writeRegisterByte(reg, gb.readNextByte());
    }
    
}
