package iskake.jgbe.core.gb.timing;

import iskake.jgbe.core.gb.HardwareRegisters;
import iskake.jgbe.core.gb.HardwareRegisters.HardwareRegister;
import iskake.jgbe.core.Bitwise;

public class Timers {
    private long divTimer = 0;
    private byte tima = 0;
    private byte tma = 0;
    private byte tac = 0;
    private boolean shouldDispatchInterrupt = false;

    public void init() {
        divTimer = 0;
        tima = 0;
        tma = 0;
        tac = 0;
        shouldDispatchInterrupt = false;
    }

    public void tick() {
        divTimer++;
        tryIncTIMA();
    }

    private void tryIncTIMA() {
        if (isTIMAEnabled() && divTimer % getTACFrequency() == 0)
            incTIMA();
    }

    public void incTIMA() {
        tima++;
        if (tima == 0) {
            tima = tma;
            shouldDispatchInterrupt = true;
        }
    }

    public boolean shouldDispatchInterrupt() {
        return shouldDispatchInterrupt;
    }

    public void shouldNotDispatchInterrupt() {
        shouldDispatchInterrupt = false;
    }

    public byte readDIV() {
        return (byte)((divTimer & 0xff00) >> 8);
    }

    public void writeDIV() {
        divTimer = 0;
    }

    public void writeDIVInternal(int value) {
        divTimer = value & 0xffff; // sanity
    }

    public byte readTIMA() {
        return tima;
    }

    public void writeTIMA() {
        tima = 0;
    }

    public byte readTMA() {
        return tma;
    }

    public void writeTMA(byte value) {
        tma = value;
    }

    public byte readTAC() {
        return (byte)(Byte.toUnsignedInt(tac) | 0b1111_1000);
    }

    public void writeTAC(byte value) {
        tac = (byte)(value & 0b111);
    }

    /**
     * Check if the TIMA register is enabled (bit 2 of the TAC register.)
     *
     * @return {@code true} if bit 2 is set, {@code false} otherwise.
     */
    public boolean isTIMAEnabled() {
        return Bitwise.isBitSet(tac, 2);
    }

    /**
     * Get the rate to increment the TIMA register to
     * (bits 0-1 of the TAC register.)
     *
     * @return The frequency specified in the TAC register.
     */
    public int getTACFrequency() {
        return switch (tac & 0b11) {
            case 0b00 -> 0x400;
            case 0b01 -> 0x10;
            case 0b10 -> 0x40;
            case 0b11 -> 0x100;
            default -> throw new RuntimeException("Something went very wrong.");
        };
    }

    /**
     * Get the rate to increment the TIMA register to
     * (bits 0-1 of the TAC register.)
     * 
     * @param hwreg the hardware registers to get the TIMA register from.
     * @return The frequency specified in the TAC register.
     */
    public static int getTACFrequency(HardwareRegisters hwreg) {
        int val = hwreg.readAsInt(HardwareRegister.TAC);

        return switch (val & 0b11) {
            case 0b00 -> 0x400;
            case 0b01 -> 0x10;
            case 0b10 -> 0x40;
            case 0b11 -> 0x100;
            default -> throw new RuntimeException("Something went very wrong.");
        };
    }
}
