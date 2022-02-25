package tland.gb.Inst;

import tland.gb.CPU;
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
    void doOp(CPU cpu, int opcode) {
        cpu.reg.writeRegisterByte(reg, cpu.readNextByte());
    }
    
}
