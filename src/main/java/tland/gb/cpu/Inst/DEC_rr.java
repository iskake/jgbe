package tland.gb.cpu.Inst;

import tland.gb.IGameBoy;
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
    public void doOp(IGameBoy gb, int opcode) {
        // 0x35 -> dec [hl]
        if (Registers.isRegisterByte(reg) || opcode == 0x35) {
            gb.reg().decRegisterByte(reg);

            byte value = gb.reg().readRegisterByte(reg);

            gb.reg().setFlagConditional(Flags.Z, value == 0);
            gb.reg().setFlag(Flags.N);
            gb.reg().setFlagConditional(Flags.H, (Byte.toUnsignedInt(value) & 0b1111) == 0b1111);

        } else {
            gb.reg().decRegisterShort(reg);
        }
    }

}
