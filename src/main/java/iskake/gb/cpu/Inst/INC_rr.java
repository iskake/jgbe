package iskake.gb.cpu.Inst;

import iskake.gb.GameBoy;
import iskake.gb.Registers;
import iskake.gb.Registers.Flags;
import iskake.gb.Registers.RegisterIndex;

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

            gb.reg.setFlagConditional(Flags.Z, value == 0);
            gb.reg.resetFlag(Flags.N);
            gb.reg.setFlagConditional(Flags.H, (Byte.toUnsignedInt(value) & 0b1111) == 0);

        } else {
            gb.reg.incRegisterShort(reg);
        }
    }

}
