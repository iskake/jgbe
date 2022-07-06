package iskake.jgbe.core.gb.cpu.inst;

import iskake.jgbe.core.gb.IGameBoy;
import iskake.jgbe.core.gb.Registers.Flags;
import iskake.jgbe.core.gb.Registers.Register;

/**
 * Special load instructions for the stack pointer.
 * 
 * <p>
 * Implements opcodes: {@code ld hl, sp+$e8} and {@code ld sp, hl}
 */
public class LD_SP extends Instruction {

    private static final int OP_LD_HL_SP_E8 = 0xf8;

    public LD_SP(String name) {
        super(name);
    }

    @Override
    public void doOp(IGameBoy gb, int opcode) {
        if (opcode == OP_LD_HL_SP_E8) {
            // ld hl, sp+$e8
            short sp = gb.sp().get();
            byte value = gb.readNextByte();

            gb.reg().writeRegisterShort(Register.HL, sp + value);

            gb.reg().resetFlag(Flags.Z);
            gb.reg().resetFlag(Flags.N);
            gb.reg().setFlagConditional(Flags.H,
                    (Short.toUnsignedInt(sp) & 0b111) + (Byte.toUnsignedInt(value) & 0b111) > 0b111);
            gb.reg().setFlagConditional(Flags.C, (Short.toUnsignedInt(sp) & 0xff) + Byte.toUnsignedInt(value) > 0xff);
        } else {
            // ld sp, hl
            gb.sp().set(gb.reg().readRegisterShort(Register.HL));
        }
    }

}
