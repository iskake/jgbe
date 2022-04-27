package tland.emu.cpu.Inst;

import tland.Bitwise;
import tland.emu.IEmulator;
import tland.emu.Registers.Flags;
import tland.emu.Registers.RegisterIndex;

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
    public void doOp(IEmulator emu, int opcode) {
        int value = Byte.toUnsignedInt(emu.reg().readRegisterByte(RegisterIndex.A));
        if (!emu.reg().isFlagSet(Flags.N)) {
            // Adjust value for additions
            if (emu.reg().isFlagSet(Flags.H) || (value & 0b1111) > 0x09) {
                // carry or overflow in lower digit
                value += 0x06;
            }

            if (emu.reg().isFlagSet(Flags.C) || value > 0x99) {
                // carry or overflow in higher digit
                value += 0x60;
            }
        } else {
            // Adjust value for subtractions (note: only carry and half carry)
            if (emu.reg().isFlagSet(Flags.H)) {
                value -= 0x06;
            }

            if (emu.reg().isFlagSet(Flags.C)) {
                value -= 0x60;
            }
        }

        emu.reg().writeRegisterByte(RegisterIndex.A, value);
        emu.reg().setFlagConditional(Flags.Z, Bitwise.intAsByte(value) == 0);
        emu.reg().resetFlag(Flags.H);
        emu.reg().setFlagConditional(Flags.C, value > 0x99);
    }

}
