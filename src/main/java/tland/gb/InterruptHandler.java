package tland.gb;

import tland.Bitwise;

public class InterruptHandler {

    private boolean ime;
    private boolean waitIME = false;
    private final ProgramCounter pc;
    private final StackPointer sp;

    public InterruptHandler(ProgramCounter pc, StackPointer sp) {
        this.pc = pc;
        this.sp = sp;
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

    /**
     * Call the interrupt at the address.
     * 
     * @param address The address to call.
     */
    private void callInterrupt(int address) {
        disable();
        sp.push(pc.get());
        pc.set(Bitwise.toShort(address));
    }

    /**
     * Handle the VBlank interrupt.
     * <p>
     * Disables interrupts and calls the address $40.
     */
    public void VBlankInterrupt() {
        callInterrupt(0x40);
    }

    /**
     * Handle the STAT interrupt.
     * <p>
     * Disables interrupts and calls the address $48.
     */
    public void STATInterrupt() {
        callInterrupt(0x48);
    }

    /**
     * Handle the Timer interrupt.
     * <p>
     * Disables interrupts and calls the address $50.
     */
    public void timerInterrupt() {
        callInterrupt(0x50);
    }

    /**
     * Handle the Serial interrupt.
     * <p>
     * Disables interrupts and calls the address $58.
     */
    public void serialInterrupt() {
        callInterrupt(0x58);
    }

    /**
     * Handle the Joypad interrupt.
     * <p>
     * Disables interrupts and calls the address $60.
     */
    public void joypadInterrupt() {
        callInterrupt(0x60);
    }

}
