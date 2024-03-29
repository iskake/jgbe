package iskake.jgbe.core.gb.interrupt;

import iskake.jgbe.core.gb.GameBoy;
import iskake.jgbe.core.Bitwise;

import static iskake.jgbe.core.gb.HardwareRegisters.HardwareRegister.IE;
import static iskake.jgbe.core.gb.HardwareRegisters.HardwareRegister.IF;

public class InterruptHandler {

    public enum InterruptType {
        VBLANK(0x40),
        STAT(0x48),
        TIMER(0x50),
        SERIAL(0x58),
        JOYPAD(0x60);

        final int address;

        InterruptType(int address) {
            this.address = address;
        }

        public static final int NUM_INTERRUPTS = 5;
        public static final InterruptType[] fromBit = { VBLANK, STAT, TIMER, SERIAL, JOYPAD };
    }

    private boolean ime;
    private boolean waitIME = false;
    private final GameBoy gb;

    public InterruptHandler(GameBoy gb) {
        this.gb = gb;
    }

    public void init() {
        waitIME = false;
        ime = false;
    }

    /**
     * Disable all interrupts.
     */
    public void disable() {
        waitIME = false;
        ime = false;
    }

    /**
     * Set the interrupts waiting to be enabled.
     */
    public void waitForIME() {
        if (!ime) {
            waitIME = true;
        }
    }

    /**
     * Enable all interrupts specified in the {@code ie} hardware register.
     * <p>
     * Should only be called if either {@code waitForIME()} has been called or when
     * using the {@code reti} instruction.
     */
    public void enable() {
        if (!ime) {
            waitIME = false;
            ime = true;
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
        gb.hwreg().setBit(IF, bit);
    }

    /**
     * Call the first currently waiting interrupt (VBlank -> ... -> Joypad).
     * 
     * @return {@code true} if the call was successful, {@code false} otherwise.
     */
    public boolean callWaiting() {
        for (int bit = 0; bit < InterruptType.NUM_INTERRUPTS; bit++) {
            if (Bitwise.isBitSet(gb.hwreg().read(IF), bit)) {
                InterruptType interruptToCall = InterruptType.fromBit[bit];
                if (callInterrupt(interruptToCall)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean anyIFSet() {
        return (gb.hwreg().readAsInt(IF) & 0b0001_1111) != 0;
    }

    /**
     * Call the interrupt at the address.
     * 
     * @param it The interrupt to call.
     */
    private boolean callInterrupt(InterruptType it) {
        int bit = it.ordinal();

        if (!enabled()) {
            return false;
        } else {
            if (!(Bitwise.isBitSet(gb.hwreg().read(IE), bit)) || !(Bitwise.isBitSet(gb.hwreg().read(IF), bit))) {
                return false;
            }
        }
        disable();
        gb.timing().incCycles();
        gb.timing().incCycles();
        gb.sp().push(gb.pc().get());

        gb.pc().set(Bitwise.toShort(it.address));

        gb.hwreg().resetBit(IF, bit);

        return true;
    }

}
