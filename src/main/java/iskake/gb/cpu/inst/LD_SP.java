package iskake.gb.cpu.inst;

import iskake.gb.GameBoy;
import iskake.gb.Registers.Flags;
import iskake.gb.Registers.RegisterIndex;

/**
 * Special load instructions for the stack pointer.
 * 
 * <p>
 * Implements opcodes: {@code ld hl, sp+$e8} and {@code ld sp, hl}
 */
public class LD_SP extends Instruction {

    public LD_SP(String name) {
        super(name);
    }

    @Override
    public void doOp(GameBoy gb, int opcode) {
        if (opcode == 0xf8) {
            // ld hl, sp+$e8
            short sp = gb.sp.get();
            byte value = gb.readNextByte();

            gb.reg.writeRegisterShort(RegisterIndex.HL, sp + value);

            gb.reg.resetFlag(Flags.Z);
            gb.reg.resetFlag(Flags.N);
            gb.reg.setFlagConditional(Flags.H,
                    (Short.toUnsignedInt(sp) & 0b111) + (Byte.toUnsignedInt(value) & 0b111) > 0b111);
            gb.reg.setFlagConditional(Flags.C, (Short.toUnsignedInt(sp) & 0xff) + Byte.toUnsignedInt(value) > 0xff);
        } else {
            // ld sp, hl
            gb.sp.set(gb.reg.readRegisterShort(RegisterIndex.HL));
        }
    }

}
