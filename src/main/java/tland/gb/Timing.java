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
    private final int VBLANK_START = SCANLINE_CYCLES * 144;
    private final int FRAME_CYCLES = VBLANK_START + VBLANK_CYCLES;

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
        for (long cycle = oldCycles; cycle < cycles; cycle++) {
            handleVideo(cycle);
        }


        // 
    }

    private void handleVideo(long cycle) {
        // vblank
        if ((cycle % SCANLINE_CYCLES) == 0) {
            hwreg.incRegister(LY);
            int ly_val = hwreg.readRegisterInt(LY);
            if (ly_val == 0x90) {
                interrupts.VBlankInterrupt();
            } else if (ly_val > 0x99) {
                hwreg.writeRegister(LY, 0);
            }
        }

        // 0xff40 LCDC
        // TODO

        // 0xff41 (STAT)
        long scanDot = (cycle % SCANLINE_CYCLES);

        if ((cycle % FRAME_CYCLES) >= VBLANK_START) {
            hwreg.setBit(STAT, 0);
            hwreg.resetBit(STAT, 1);
        } else if (scanDot < MODE2_CYCLES) {
            hwreg.resetBit(STAT, 0);
            hwreg.setBit(STAT, 1);
        } else if (scanDot < MODE3_CYCLES_MIN) {
            // Assume minimum cycles spent drawing
            hwreg.setBit(STAT, 0);
            hwreg.setBit(STAT, 1);
        } else if (scanDot < MODE0_CYCLES_MAX) {
            // Assume maximum cycles spend in HBlank
            hwreg.resetBit(STAT, 0);
            hwreg.resetBit(STAT, 1);
        }
        hwreg.setBitConditional(STAT, 2, (hwreg.readRegister(LY) == hwreg.readRegister(LYC)));
    }
}
