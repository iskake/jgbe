package tland.emu.cpu.inst;

import tland.emu.IEmulator;
import tland.emu.Registers;
import tland.emu.Registers.Flags;
import tland.emu.Registers.RegisterIndex;

/**
 * Increment value in register / address pointed to by register (with [HL]).
 * 
 * <p>
 * Implements opcodes: {@code inc r8}, {@code inc [hl]} and {@code inc r16}
 * 
 * @author Tarjei Landøy
 */
public class INC_rr extends Instruction {
    private final RegisterIndex reg;

    public INC_rr(String name, RegisterIndex reg) {
        super(name);
        this.reg = reg;
    }

    @Override
    public void doOp(IEmulator emu, int opcode) {
        // 0x34 -> inc [hl]
        if (Registers.isRegisterByte(reg) || opcode == 0x34) {
            emu.reg().incRegisterByte(reg);

            byte value = emu.reg().readRegisterByte(reg);

            emu.reg().setFlagConditional(Flags.Z, value == 0);
            emu.reg().resetFlag(Flags.N);
            emu.reg().setFlagConditional(Flags.H, (Byte.toUnsignedInt(value) & 0b1111) == 0);

        } else {
            emu.reg().incRegisterShort(reg);
        }
    }

}
