package tland.gb.timing;

import static tland.gb.HardwareRegisters.HardwareRegisterIndex.*;

import tland.Bitwise;
import tland.gb.GameBoy;
import tland.gb.HardwareRegisters;
import tland.gb.InterruptHandler;
import tland.gb.InterruptHandler.InterruptType;
import tland.gb.ppu.PPU;

/**
 * Timing, incrementing and handling of clock cycles.
 */
public class Timing {
    private long cycles;

    private final GameBoy gb;
    private final PPU ppu;
    private final HardwareRegisters hwreg;
    private final InterruptHandler interrupts;

    private final int MODE2_CYCLES = 80;
    private final int MODE3_CYCLES_MIN = 172;
    private final int MODE3_CYCLES_MAX = 289;

    private final int MODE0_CYCLES_MIN = 172;
    private final int MODE0_CYCLES_MAX = 289;

    private final int SCANLINE_CYCLES = 456;
    private final int MODE1_CYCLES = SCANLINE_CYCLES * 10;
    private final int FRAME_CYCLES = SCANLINE_CYCLES * 144 + MODE1_CYCLES;

    private final int MODE2_END = MODE2_CYCLES;
    private final int MODE3_END_MIN = MODE2_END + MODE3_CYCLES_MIN;
    private final int MODE0_END_MAX = MODE3_END_MIN + MODE0_CYCLES_MAX;
    private final int MODE1_START = SCANLINE_CYCLES * 144;

    public Timing(GameBoy gb, HardwareRegisters hwreg, InterruptHandler interrupts, PPU ppu) {
        this.gb = gb;
        this.hwreg = hwreg;
        this.interrupts = interrupts;
        this.ppu = ppu;
        init();
    }

    /**
     * Initialise timer.
     * Sets cycles to 0.
     */
    public void init() {
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
            handleTimers(cycle);
            handleVideo(cycle);
        }
    }

    private void handleTimers(long cycle) {
        if (cycle == 0) {
            return;
        }

        if (cycle % Timers.DIV_CLOCK == 0) {
            hwreg.incRegister(DIV);
        }

        if (Timers.isTIMAEnabled(hwreg)) {
            if (cycle % Timers.getTACFrequency(hwreg) == 0) {
                hwreg.incRegister(TIMA);

                if (hwreg.readRegisterInt(TIMA) == 0) {
                    hwreg.writeRegister(TIMA, hwreg.readRegister(TMA)); // ? check TIM write when TIMA overflow
                    interrupts.setWaitingToCall(InterruptType.TIMER);
                }
            }
        }
    }

    private void handleVideo(long cycle) {
        // VBlank
        if ((cycle % SCANLINE_CYCLES) == 0 && cycle != 0) {
            ppu.addScanline();
            hwreg.incRegister(LY);
            int ly_val = hwreg.readRegisterInt(LY);
            if (ly_val == 0x90) {
                interrupts.setWaitingToCall(InterruptType.VBLANK);
            } else if (ly_val > 0x99) {
                hwreg.writeRegister(LY, 0);
            }
        }

        // 0xff40 LCDC
        // 'Automatically' 'handled' in PPUController

        // 0xff41 (STAT)
        long scanDot = (cycle % SCANLINE_CYCLES);

        if ((cycle % FRAME_CYCLES) >= MODE1_START) {
            // Mode 1 (VBlank)
            hwreg.setBit(STAT, 0);
            hwreg.resetBit(STAT, 1);
        } else if (scanDot < MODE2_END) {
            // Mode 2 (OAM search)
            hwreg.resetBit(STAT, 0);
            hwreg.setBit(STAT, 1);
        } else if (scanDot < MODE3_END_MIN) {
            // Mode 3 (Scanline render)
            // For simplicity, assume minimum cycles spent drawing
            hwreg.setBit(STAT, 0);
            hwreg.setBit(STAT, 1);
        } else if (scanDot < MODE0_END_MAX) {
            // Mode 0 (HBlank)
            // For simplicity, assume maximum cycles spend in HBlank
            hwreg.resetBit(STAT, 0);
            hwreg.resetBit(STAT, 1);
        }

        hwreg.setBitConditional(STAT, 2, (hwreg.readRegister(LY) == hwreg.readRegister(LYC)));

        boolean STATLY = Bitwise.isBitSet(hwreg.readRegister(STAT), 2) && Bitwise.isBitSet(hwreg.readRegister(STAT), 6);
        boolean STATHBL = Bitwise.isBitSet(hwreg.readRegister(STAT), 3) && ((hwreg.readRegisterInt(STAT) & 0b11) == 0);
        boolean STATVBL = Bitwise.isBitSet(hwreg.readRegister(STAT), 4) && ((hwreg.readRegisterInt(STAT) & 0b11) == 1);
        boolean STATOAM = Bitwise.isBitSet(hwreg.readRegister(STAT), 5) && ((hwreg.readRegisterInt(STAT) & 0b11) == 2);
        if (STATLY || STATHBL || STATVBL || STATOAM) {
            interrupts.setWaitingToCall(InterruptType.STAT);
        }
    }
}
