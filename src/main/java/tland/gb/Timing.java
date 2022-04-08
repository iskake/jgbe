package tland.gb;

import static tland.gb.HardwareRegisters.HardwareRegisterIndex.*;

/**
 * Timing, incrementing and handling of clock cycles.
 */
public class Timing {
    private long cycles;

    private final GameBoy gb;
    private final HardwareRegisters hwreg;
    private final InterruptHandler interrupts;

    private final int MODE2_CYCLES = 80;
    private final int MODE3_CYCLES_MIN = 172;
    private final int MODE3_CYCLES_MAX = 289;
    private final int MODE0_CYCLES_MIN = 172;
    private final int MODE0_CYCLES_MAX = 289;
    private final int SCANLINE_CYCLES = 456;
    private final int VBLANK_CYCLES = SCANLINE_CYCLES * 10;
    private final int FRAME_CYCLES = SCANLINE_CYCLES * 144 + VBLANK_CYCLES;

    public Timing(GameBoy gb, HardwareRegisters hwreg, InterruptHandler interrupts) {
        this.gb = gb;
        this.hwreg = hwreg;
        this.interrupts = interrupts;
        cycles = 0;
    }

    /**
     * Get the amount of T-cycles in total.
     * 
     * @return All T-cycles.
     */
    public long getCycles() {
        return cycles;
    }

    /**
     * Increment the amount of clock cycles.
     */
    public void incCycles() {
        long oldCycles = cycles;
        cycles += 4;
        handleCycles(oldCycles);
    }

    /**
     * Handle clock cycle increases. 
     * Should be ran after incrementing clock cycles.
     * 
     * @param oldCycles The clock cycles from before the incrementing.
     */
    private void handleCycles(long oldCycles) {
        // vblank
        boolean ly_inc = false;
        for (long i = oldCycles; i < cycles; i++) {
            if (cycles % (SCANLINE_CYCLES) == 0) {
                ly_inc = true;
                break;
            }
        }

        if (ly_inc) {
            hwreg.incRegister(LY);
            int ly_val = hwreg.readRegisterInt(LY);
            if (ly_val == 0x90) {
                interrupts.VBlankInterrupt();
            } else if (ly_val > 0x99) {
                hwreg.writeRegister(LY, 0);
            }
        }
    }
}
