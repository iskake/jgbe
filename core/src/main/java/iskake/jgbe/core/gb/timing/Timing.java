package iskake.jgbe.core.gb.timing;

import iskake.jgbe.core.gb.DMAController;
import iskake.jgbe.core.gb.GameBoy;
import iskake.jgbe.core.gb.HardwareRegisters;
import iskake.jgbe.core.gb.interrupt.InterruptHandler;
import iskake.jgbe.core.gb.interrupt.InterruptHandler.InterruptType;
import iskake.jgbe.core.gb.input.IJoypad;
import iskake.jgbe.core.gb.ppu.PPU;
import iskake.jgbe.core.Bitwise;

import static iskake.jgbe.core.gb.HardwareRegisters.HardwareRegister.*;

/**
 * Timing, incrementing and handling of clock cycles.
 */
public class Timing {
    private long cycles;
    private long cyclesSinceLCDEnable;
    private long vblankWaitCycles;

    private final GameBoy gb;
    private final PPU ppu;
    private final HardwareRegisters hwreg;
    private final DMAController dmaControl;
    private final Timers timers;
    private final IJoypad joypad;
    private final InterruptHandler interrupts;

    // Using M-cycle accuracy (set to `false`) should increase performance (in some cases, by a lot)
    private final boolean T_STATE_ACCURATE = false; // FEATURE?: make this optional
    public  final int CYCLE_LEN_DIV = T_STATE_ACCURATE ? 1 : 4;

    private final int MODE2_CYCLES = 80 / CYCLE_LEN_DIV;
    private final int MODE3_CYCLES_MIN = 172 / CYCLE_LEN_DIV;
    private final int MODE3_CYCLES_MAX = 289 / CYCLE_LEN_DIV;   // TODO: 289/4 = 72.25

    private final int MODE0_CYCLES_MIN = 172 / CYCLE_LEN_DIV;
    private final int MODE0_CYCLES_MAX = 289 / CYCLE_LEN_DIV;   // TODO: 289/4 = 72.25

    private final int SCANLINE_CYCLES = 456 / CYCLE_LEN_DIV;
    private final int MODE1_CYCLES = SCANLINE_CYCLES * 10;
    private final int FRAME_CYCLES = SCANLINE_CYCLES * 144 + MODE1_CYCLES;

    private final int MODE2_END = MODE2_CYCLES;
    private final int MODE3_END_MIN = MODE2_END + MODE3_CYCLES_MIN;
    private final int MODE0_END_MAX = MODE3_END_MIN + MODE0_CYCLES_MAX;
    private final int MODE1_START = SCANLINE_CYCLES * 144;

    private boolean hasDoneSTAT = false;
    private boolean lcdJustEnabled = false;

    public Timing(GameBoy gb, HardwareRegisters hwreg, DMAController dmaControl, Timers timers, IJoypad joypad, InterruptHandler interrupts, PPU ppu) {
        this.gb = gb;
        this.hwreg = hwreg;
        this.dmaControl = dmaControl;
        this.timers = timers;
        this.joypad = joypad;
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
        cyclesSinceLCDEnable = 0;
        vblankWaitCycles = 0;
        hasDoneSTAT = false;
        lcdJustEnabled = false;
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
        cycles += (4 / CYCLE_LEN_DIV);
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
            dmaControl.decCycles();
            handleJoypad(cycle);
            handleTimers(cycle);
            handleVideo(cycle);
        }
    }

    private void handleJoypad(long cycles) {
//        TODO: joypad interrupt
    }

    private void handleTimers(long cycle) {
        // TODO (for M-cycle accuracy): check if ticking 4 times in a row
        //  makes anything behave incorrectly (TIMA?)
        for (int i = 0; i < CYCLE_LEN_DIV; i++) {
            timers.tick();
        }

        if (timers.shouldDispatchInterrupt()) {
            interrupts.setWaitingToCall(InterruptType.TIMER);
            timers.shouldNotDispatchInterrupt();
        }
    }

    private void handleVideo(long cycle) {
        boolean doVBlank = false;
        boolean lcdEnabled = Bitwise.isBitSet(hwreg.readAsInt(LCDC), 7);
        if (!lcdEnabled) {
            cyclesSinceLCDEnable = 0;
            hwreg.writeInternal(LY, (byte) 0);

            // TODO? temp. workaround for hanging application (when lcd is never enabled)
            if (vblankWaitCycles == FRAME_CYCLES) {
                ppu.clearFrameBuffer();
                gb.setVBlankJustCalled();
                vblankWaitCycles = 0;
            }
            vblankWaitCycles++;
            lcdJustEnabled = true;
        } else {
            doVBlank = true;
        }

        // STAT handling
        long scanDot = (cyclesSinceLCDEnable % SCANLINE_CYCLES);

        if (!lcdEnabled) {
            hwreg.resetBit(STAT, 0);
            hwreg.resetBit(STAT, 1);
            hwreg.resetBit(STAT, 2);
        } else {
            if (scanDot == 0) {
                hasDoneSTAT = false;
            }

            int oldSTAT = hwreg.readAsInt(STAT);
            if ((cyclesSinceLCDEnable % FRAME_CYCLES) >= MODE1_START) {
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
            } else {
                // Mode 0 (HBlank)
                // For simplicity, assume maximum cycles spend in HBlank
                hwreg.resetBit(STAT, 0);
                hwreg.resetBit(STAT, 1);
            }
            hwreg.setBitConditional(STAT, 2, (hwreg.read(LY) == hwreg.read(LYC)));
            int newSTAT = hwreg.readAsInt(STAT);

            if (oldSTAT != newSTAT && !hasDoneSTAT) {
                boolean STATLY = Bitwise.isBitSet(newSTAT, 2) && Bitwise.isBitSet(newSTAT, 6);
                boolean STATHBL = Bitwise.isBitSet(newSTAT, 3) && ((newSTAT & 0b11) == 0);
                boolean STATVBL = Bitwise.isBitSet(newSTAT, 4) && ((newSTAT & 0b11) == 1);
                boolean STATOAM = Bitwise.isBitSet(newSTAT, 5) && ((newSTAT & 0b11) == 2);
                if (STATLY || STATHBL || STATVBL || STATOAM) {
                    interrupts.setWaitingToCall(InterruptType.STAT);
                    hasDoneSTAT = true;
                }
            }
        }

        // VBlank handling
        if (doVBlank) {
            if (!lcdJustEnabled) {
                if (scanDot == MODE3_END_MIN)
                    ppu.addScanline(0, PPU.LCD_SIZE_X); // TODO: actually use pixel FIFO
            } else {
                if (cyclesSinceLCDEnable % FRAME_CYCLES == 0)
                    ppu.clearFrameBuffer();
            }

            if ((cyclesSinceLCDEnable % SCANLINE_CYCLES) == 0 && cyclesSinceLCDEnable != 0) {
                hwreg.inc(LY);
                int ly_val = hwreg.readAsInt(LY);
                if (ly_val == 0x90) {
                    interrupts.setWaitingToCall(InterruptType.VBLANK);
                    gb.setVBlankJustCalled();
                    vblankWaitCycles = 0;
                    lcdJustEnabled = false;
                } else if (ly_val > 0x99) {
                    hwreg.writeInternal(LY, (byte)0);
                }
            }
            cyclesSinceLCDEnable++;
        }
    }
}
