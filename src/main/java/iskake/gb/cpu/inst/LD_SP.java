package iskake.gb.cpu.inst;

import iskake.gb.IGameBoy;
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
    public void doOp(IGameBoy emu, int opcode) {
        if (opcode == 0xf8) {
            // ld hl, sp+$e8
            short sp = emu.sp().get();
            byte value = emu.readNextByte();

            emu.reg().writeRegisterShort(RegisterIndex.HL, sp + value);

            emu.reg().resetFlag(Flags.Z);
            emu.reg().resetFlag(Flags.N);
            emu.reg().setFlagConditional(Flags.H,
                    (Short.toUnsignedInt(sp) & 0b111) + (Byte.toUnsignedInt(value) & 0b111) > 0b111);
            emu.reg().setFlagConditional(Flags.C, (Short.toUnsignedInt(sp) & 0xff) + Byte.toUnsignedInt(value) > 0xff);
        } else {
            // ld sp, hl
            emu.sp().set(emu.reg().readRegisterShort(RegisterIndex.HL));
        }
    }

}
