package iskake.gb.timing;

import iskake.Bitwise;
import iskake.gb.HardwareRegisters;
import iskake.gb.HardwareRegisters.HardwareRegisterIndex;
import iskake.gb.cpu.CPU;

//? TODO: make class non-static and map these values to hardware registers?
public class Timers {
    /** The DIV register increment rate. */
    public static final int DIV_CLOCK = 0x4000;

    /**
     * Check if the TIMA register is enabled (bit 2 of the TAC register.)
     * 
     * @param hwreg the hardware registers to get the TIMA register from.
     * @return {@code true} if bit 2 is set, {@code false} otherwise.
     */
    public static boolean isTIMAEnabled(HardwareRegisters hwreg) {
        int val = hwreg.readRegisterInt(HardwareRegisterIndex.TAC);
        return Bitwise.isBitSet(val, 2);
    }

    /**
     * Get the rate to increment the TIMA register to
     * (bits 0-1 of the TAC register.)
     * 
     * @param hwreg the hardware registers to get the TIMA register from.
     * @return The frequency specified in the TAC register.
     */
    public static int getTACFrequency(HardwareRegisters hwreg) {
        int val = hwreg.readRegisterInt(HardwareRegisterIndex.TAC);

        return switch (val & 0b11) {
            case 0b00 -> CPU.CLOCK_SPEED / 0x400;
            case 0b01 -> CPU.CLOCK_SPEED / 0x10;
            case 0b10 -> CPU.CLOCK_SPEED / 0x40;
            case 0b11 -> CPU.CLOCK_SPEED / 0x100;
            default -> throw new RuntimeException("Something went very wrong.");
        };
    }
}
