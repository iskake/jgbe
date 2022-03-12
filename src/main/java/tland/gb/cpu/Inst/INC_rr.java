package tland.gb.cpu.Inst;

import tland.gb.GameBoy;
import tland.gb.Registers;
import tland.gb.Registers.Flags;
import tland.gb.Registers.RegisterIndex;

/**
 * Increment value in register / address pointed to by register (with [HL]).
 * 
 * <p>
 * Implements opcodes: {@code inc r8}, {@code inc [hl]} and {@code inc r16}
 */
public class INC_rr extends Instruction {
    private final RegisterIndex reg;

    public INC_rr(String name, RegisterIndex reg) {
        super(name);
        this.reg = reg;
    }

    @Override
    public void doOp(GameBoy gb, int opcode) {
        // 0x34 -> inc [hl]
        if (Registers.isRegisterByte(reg) || opcode == 0x34) {
            gb.reg.incRegisterByte(reg);

            byte value = gb.reg.readRegisterByte(reg);

            if (value == 0) {
                gb.reg.setFlag(Flags.Z);
            } else {
                gb.reg.resetFlag(Flags.Z);
            }

            gb.reg.resetFlag(Flags.N);

            if ((Byte.toUnsignedInt(value) & 0b1111) == 0) {
                gb.reg.setFlag(Flags.H);
            } else {
                gb.reg.resetFlag(Flags.H);
            }

        } else {
            gb.reg.incRegisterShort(reg);
        }
    }

}
