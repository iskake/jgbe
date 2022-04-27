package tland.emu.cpu.Inst;

import tland.emu.IEmulator;
import tland.emu.Registers;
import tland.emu.Registers.Flags;
import tland.emu.Registers.RegisterIndex;

/**
 * Decrement value in register / address pointed to by register (with [HL]).
 * 
 * <p>
 * Implements opcodes: {@code dec r8}, {@code dec [hl]} and {@code dec r16}
 */
public class DEC_rr extends Instruction {
    private final RegisterIndex reg;

    public DEC_rr(String name, RegisterIndex reg) {
        super(name);
        this.reg = reg;
    }

    @Override
    public void doOp(IEmulator emu, int opcode) {
        // 0x35 -> dec [hl]
        if (Registers.isRegisterByte(reg) || opcode == 0x35) {
            emu.reg().decRegisterByte(reg);

            byte value = emu.reg().readRegisterByte(reg);

            emu.reg().setFlagConditional(Flags.Z, value == 0);
            emu.reg().setFlag(Flags.N);
            emu.reg().setFlagConditional(Flags.H, (Byte.toUnsignedInt(value) & 0b1111) == 0b1111);

        } else {
            emu.reg().decRegisterShort(reg);
        }
    }

}
