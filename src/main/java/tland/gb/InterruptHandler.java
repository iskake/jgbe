package tland.gb;

public class InterruptHandler {

    private boolean ime;
    private boolean waitIME = false;
    private final HardwareRegisters hwreg;

    public InterruptHandler(HardwareRegisters hwreg) {
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
     * Check if the interrupts are waiting to be enabled.
     * 
     * @return {@code true} if an {@code ei} instruction has just run.
     */
    public boolean waiting() {
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

}
