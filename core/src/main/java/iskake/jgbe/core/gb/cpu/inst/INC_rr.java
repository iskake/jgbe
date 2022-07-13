package iskake.jgbe.core.gb.cpu.inst;

import iskake.jgbe.core.gb.IGameBoy;
import iskake.jgbe.core.gb.Registers;
import iskake.jgbe.core.gb.Registers.Flags;
import iskake.jgbe.core.gb.Registers.Register;

/**
 * Increment value in register / address pointed to by register (with [HL]).
 * 
 * <p>
 * Implements opcodes: {@code inc r8}, {@code inc [hl]} and {@code inc r16}
 */
public class INC_rr extends Instruction {
    private static final int OP_INC_$HL = 0x34;

    private final Register reg;

    public INC_rr(String name, Register reg) {
        super(name);
        this.reg = reg;
    }

    @Override
    public void doOp(IGameBoy gb, int opcode) {
        // 0x34 -> inc [hl]
        if (Registers.isByteRegister(reg) || opcode == OP_INC_$HL) {
            gb.reg().incByte(reg);

            byte value = gb.reg().readByte(reg);

            gb.reg().setFlagConditional(Flags.Z, value == 0);
            gb.reg().resetFlag(Flags.N);
            gb.reg().setFlagConditional(Flags.H, (Byte.toUnsignedInt(value) & 0b1111) == 0);

        } else {
            gb.reg().incShort(reg);
        }
    }

}
