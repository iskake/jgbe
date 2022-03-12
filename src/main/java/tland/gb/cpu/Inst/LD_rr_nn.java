package tland.gb.cpu.Inst;

import tland.gb.GameBoy;
import tland.gb.Registers;
import tland.gb.Registers.RegisterIndex;

/**
 * Load byte/short into register / address pointed to by register (with [HL]).
 * 
 * <p>
 * Implements opcodes: {@code ld r8, $n8}, {@code ld r16, $n16} and {@code ld [hl], $n8}
 */
public class LD_rr_nn extends Instruction {
    private final RegisterIndex reg;

    public LD_rr_nn(String name, RegisterIndex reg) {
        super(name);
        this.reg = reg;
    }

    @Override
    public void doOp(GameBoy gb, int opcode) {
        // 0x36 -> ld [hl], $n8
        if (Registers.isRegisterByte(reg) || opcode == 0x36) {
            gb.reg.writeRegisterByte(reg, gb.readNextByte());
        } else if (Registers.isRegisterShort(reg)) {
            gb.reg.writeRegisterShort(reg, gb.readNextShort());
        }
    }

}
