package tland.emu.cpu.Inst;

import tland.emu.IEmulator;
import tland.emu.Registers;
import tland.emu.Registers.RegisterIndex;

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
    public void doOp(IEmulator emu, int opcode) {
        // 0x36 -> ld [hl], $n8
        if (Registers.isRegisterByte(reg) || opcode == 0x36) {
            emu.reg().writeRegisterByte(reg, emu.readNextByte());
        } else if (Registers.isRegisterShort(reg)) {
            emu.reg().writeRegisterShort(reg, emu.readNextShort());
        }
    }

}
