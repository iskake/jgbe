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
public class LD_SP {

    public static void ld_hl_sp(IGameBoy gb, int opcode) {
        // ld hl, sp+$e8
        short sp = gb.sp().get();
        byte value = gb.readNextByte();

        gb.reg().writeShort(Register.HL, sp + value);

        gb.reg().resetFlag(Flags.Z);
        gb.reg().resetFlag(Flags.N);
        gb.reg().setFlagIf(Flags.H,
                           (Short.toUnsignedInt(sp) & 0b111) + (Byte.toUnsignedInt(value) & 0b111) > 0b111);
        gb.reg().setFlagIf(Flags.C, (Short.toUnsignedInt(sp) & 0xff) + Byte.toUnsignedInt(value) > 0xff);
    }

    public static void ld_sp_hl(IGameBoy gb, int opcode) {
        // ld sp, hl
        gb.sp().set(gb.reg().readShort(Register.HL));
    }

}
