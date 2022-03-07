package tland.gb.Inst;

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
            int oldVal = gb.reg.readRegisterByte(reg);

            gb.reg.incRegisterByte(reg);

            int newVal = gb.reg.readRegisterByte(reg);

            if (newVal == 0) {
                gb.reg.setFlag(Flags.Z);
            }

            gb.reg.resetFlag(Flags.N);

            // TODO: Test if this works correctly.
            if ((oldVal & (byte) 0b1111) == (byte) 0b1111 && ((newVal & (byte) 0b1111) > 0b1111)) {
                gb.reg.setFlag(Flags.H);
            }

        } else {
            gb.reg.incRegisterShort(reg);
        }
    }

}
