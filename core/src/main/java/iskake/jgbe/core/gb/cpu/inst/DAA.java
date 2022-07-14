package iskake.jgbe.core.gb.cpu.inst;

import iskake.jgbe.core.gb.IGameBoy;
import iskake.jgbe.core.gb.Registers.Flags;
import iskake.jgbe.core.gb.Registers.Register;

/**
 * Decimal Adjust Accumulator, get BCD of accumulator.
 * (E.g. $56 + $27 = $7d = $83 in BCD)
 * 
 * <p>
 * Implements opcode: {@code daa}
 */
public class DAA extends Instruction {

    public DAA() {
        super("daa");
    }

    @Override
    public void doOp(IGameBoy gb, int opcode) {
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
        gb.reg().setFlagConditional(Flags.Z, (value & 0xff) == 0);
        gb.reg().resetFlag(Flags.H);
    }

}
