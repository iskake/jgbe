package iskake.gb;

import static iskake.gb.HardwareRegisters.HardwareRegisterIndex.*;

import iskake.Bitwise;
import iskake.gb.pointer.*;

public class InterruptHandler {

    public enum InterruptType {
        VBLANK,
        STAT,
        TIMER,
        SERIAL,
        JOYPAD,
    }

    private boolean ime;
    private boolean waitIME = false;
    private final ProgramCounter pc;
    private final StackPointer sp;
    private final HardwareRegisters hwreg;
    private final GameBoy gb;

    /** Currently waiting interrupts */
    private boolean[] waiting = { false, false, false, false, false };

    public InterruptHandler(GameBoy gb, HardwareRegisters hwreg) {
        this.gb = gb;
        this.pc = gb.pc();
        this.sp = gb.sp();
        this.hwreg = hwreg;
    }

    /**
     * Disable all interrupts.
     */
    public void disable() {
        ime = false;
    }

    /**
     * Enable all interrupts speficied in the {@code ie} hardware register.
     * 
     * @param wait If the instruction should 'wait' for the next instruction to
     *             complete before enabling interrupts ({@code ei} behaviour), or
     *             not ({@code reti} behaviour).
     */
    public void enable(boolean wait) {
        if (!ime) {
            if (!wait) {
                waitIME = false;
                ime = true;
            } else {
                waitIME = true;
            }
        }
    }

    /**
     * Check if the interrupts in {@code ie} are waiting to be enabled.
     * 
     * @return {@code true} if an {@code ei} instruction has just run.
     */
    public boolean waitingIME() {
        return waitIME;
    }

    /**
     * Check if the interrupts are waiting to be enabled.
     * 
     * @return {@code true} if an {@code ei} instruction has run, and .
     */
    public boolean enabled() {
        return ime;
    }

    /**
     * Set the currently waiting interrupt that will run on the next CPU step.
     * 
     * @param it The interrupt to set.
     */
    public void setWaitingToCall(InterruptType it) {
        int bit = it.ordinal();
        waiting[bit] = true;
        hwreg.setBit(IF, bit);
    }

    /**
     * Call the currently waiting interrupt.
     * 
     * @return {@code true} if the call was successful, {@code false} otherwise.
     */
    public boolean callWaiting() {
        if (waiting[0])
            if (VBlankInterrupt())
                return true;
        if (waiting[1])
            if (STATInterrupt())
                return true;
        if (waiting[2])
            if (timerInterrupt())
                return true;
        if (waiting[3])
            if (serialInterrupt())
                return true;
        if (waiting[4])
            if (joypadInterrupt())
                return true;
        return false;
    }

    /**
     * Call the interrupt at the address.
     * 
     * @param address The address to call.
     */
    private boolean callInterrupt(int address, InterruptType it) {
        int bit = it.ordinal();

        if (!enabled()) {
            return false;
        } else {
            if (!(Bitwise.isBitSet(hwreg.readRegister(IE), bit)) || !(Bitwise.isBitSet(hwreg.readRegister(IF), bit))) {
                return false;
            }
        }
        disable();
        gb.timing.incCycles();
        gb.timing.incCycles();
        sp.push(pc.get());
        pc.set(Bitwise.toShort(address));

        waiting[bit] = false;
        hwreg.resetBit(IF, bit);

        return true;
    }

    /**
     * Handle the VBlank interrupt.
     * <p>
     * Disables interrupts and calls the address $40.
     */
    public boolean VBlankInterrupt() {
        return callInterrupt(0x40, InterruptType.VBLANK);
    }

    /**
     * Handle the STAT interrupt.
     * <p>
     * Disables interrupts and calls the address $48.
     */
    public boolean STATInterrupt() {
        return callInterrupt(0x48, InterruptType.STAT);
    }

    /**
     * Handle the Timer interrupt.
     * <p>
     * Disables interrupts and calls the address $50.
     */
    public boolean timerInterrupt() {
        return callInterrupt(0x50, InterruptType.TIMER);
    }

    /**
     * Handle the Serial interrupt.
     * <p>
     * Disables interrupts and calls the address $58.
     */
    public boolean serialInterrupt() {
        return callInterrupt(0x58, InterruptType.SERIAL);
    }

    /**
     * Handle the Joypad interrupt.
     * <p>
     * Disables interrupts and calls the address $60.
     */
    public boolean joypadInterrupt() {
        return callInterrupt(0x60, InterruptType.JOYPAD);
    }

}
