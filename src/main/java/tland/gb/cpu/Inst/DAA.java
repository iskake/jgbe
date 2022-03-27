package tland.gb.cpu.Inst;

import tland.Bitwise;
import tland.gb.GameBoy;
import tland.gb.Registers.Flags;
import tland.gb.Registers.RegisterIndex;

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
    public void doOp(GameBoy gb, int opcode) {
        int value = Byte.toUnsignedInt(gb.reg.readRegisterByte(RegisterIndex.A));
        if (!gb.reg.isFlagSet(Flags.N)) {
            // Adjust value for additions
            if (gb.reg.isFlagSet(Flags.H) || (value & 0b1111) > 0x09) {
                // carry or overflow in lower digit
                value += 0x06;
            }

            if (gb.reg.isFlagSet(Flags.C) || value > 0x99) {
                // carry or overflow in higher digit
                value += 0x60;
            }
        } else {
            // Adjust value for subtractions (note: only carry and half carry)
            if (gb.reg.isFlagSet(Flags.H)) {
                value -= 0x06;
            }

            if (gb.reg.isFlagSet(Flags.C)) {
                value -= 0x60;
            }
        }

        gb.reg.writeRegisterByte(RegisterIndex.A, value);

        if (Bitwise.intAsByte(value) == 0) {
            gb.reg.setFlag(Flags.Z);
        } else {
            gb.reg.resetFlag(Flags.Z);
        }
        gb.reg.resetFlag(Flags.H);

        if (value > 0x99) {
            gb.reg.setFlag(Flags.C);
        } else {
            gb.reg.resetFlag(Flags.C);
        }
    }

}
