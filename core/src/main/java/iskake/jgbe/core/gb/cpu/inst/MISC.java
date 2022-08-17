package iskake.jgbe.core.gb.cpu.inst;

import iskake.jgbe.core.gb.IGameBoy;
import iskake.jgbe.core.gb.Registers.Flags;
import iskake.jgbe.core.gb.Registers.Register;

/**
 * Misc instructions.
 */
public class MISC {

    public static void nop(IGameBoy gb, int opcode) {}

    public static void cpl(IGameBoy gb, int opcode) {
        gb.reg().writeByte(Register.A, ~gb.reg().readByte(Register.A));
        gb.reg().setFlag(Flags.N);
        gb.reg().setFlag(Flags.H);
    }

    public static void ccf(IGameBoy gb, int opcode) {
        gb.reg().resetFlag(Flags.N);
        gb.reg().resetFlag(Flags.H);
        gb.reg().complementFlag(Flags.C);
    }

    public static void scf(IGameBoy gb, int opcode) {
        gb.reg().resetFlag(Flags.N);
        gb.reg().resetFlag(Flags.H);
        gb.reg().setFlag(Flags.C);
    }

    public static void daa(IGameBoy gb, int opcode) {
        int value = Byte.toUnsignedInt(gb.reg().readByte(Register.A));
        if (!gb.reg().isFlagSet(Flags.N)) {
            // Adjust value for additions
            if (gb.reg().isFlagSet(Flags.H) || (value & 0x0f) > 0x09) {
                // carry or overflow in lower digit
                value += 0x06;
            }

            if (gb.reg().isFlagSet(Flags.C) || value > 0x9f) {
                // carry or overflow in higher digit
                value += 0x60;
                gb.reg().setFlag(Flags.C);
            } else {
                gb.reg().resetFlag(Flags.C);
            }
        } else {
            // Adjust value for subtractions (note: only carry and half carry)
            if (gb.reg().isFlagSet(Flags.H)) {
                value -= 0x06;
            }

            if (gb.reg().isFlagSet(Flags.C)) {
                value -= 0x60;
            }
        }

        gb.reg().writeByte(Register.A, value);
        gb.reg().setFlagIf(Flags.Z, (value & 0xff) == 0);
        gb.reg().resetFlag(Flags.H);
    }

}
