package tland.gb.cpu.Inst;

import tland.gb.GameBoy;
import tland.gb.Registers;
import tland.gb.Registers.Flags;
import tland.gb.Registers.RegisterIndex;

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
    public void doOp(GameBoy gb, int opcode) {
        // 0x35 -> dec [hl]
        if (Registers.isRegisterByte(reg) || opcode == 0x35) {
            gb.reg.decRegisterByte(reg);

            byte value = gb.reg.readRegisterByte(reg);

            if (value == 0) {
                gb.reg.setFlag(Flags.Z);
            } else {
                gb.reg.resetFlag(Flags.Z);
            }

            gb.reg.setFlag(Flags.N);

            if ((Byte.toUnsignedInt(value) & 0b1111) == 0b1111) {
                gb.reg.setFlag(Flags.H);
            } else {
                gb.reg.resetFlag(Flags.H);
            }

        } else {
            gb.reg.decRegisterShort(reg);
        }
    }

}
