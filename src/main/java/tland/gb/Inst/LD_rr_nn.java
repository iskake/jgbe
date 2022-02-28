package tland.gb.Inst;

import tland.gb.GameBoy;
import tland.gb.Registers;
import tland.gb.Registers.RegisterIndex;

/**
 * Load byte/short into register.
 * 
 * Implements opcodes: {@code ld r8, $n8} and {@code ld r16, $n16}
 */
public class LD_rr_nn extends Instruction {
    private final RegisterIndex reg;

    public LD_rr_nn(String name, RegisterIndex register) {
        super(name);

        this.reg = register;
    }

    @Override
    public void doOp(GameBoy gb, int opcode) {
        if (Registers.isRegisterByte(reg)) {
            gb.reg.writeRegisterByte(reg, gb.readNextByte());
        } else if (Registers.isRegisterShort(reg)) {
            gb.reg.writeRegisterShort(reg, gb.readNextShort());
        }
    }

}
