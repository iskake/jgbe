package iskake.jgbe.core.gb.interrupt;

import iskake.jgbe.core.gb.GameBoy;
import iskake.jgbe.core.gb.HardwareRegisters;
import iskake.jgbe.core.gb.pointer.ProgramCounter;
import iskake.jgbe.core.gb.pointer.StackPointer;
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

        int address;

        private InterruptType(int address) {
            this.address = address;
        }
    }

    private boolean ime;
    private boolean waitIME = false;
    private final ProgramCounter pc;
    private final StackPointer sp;
    private final HardwareRegisters hwreg;
    private final GameBoy gb;

    /** Currently waiting interrupts */
    private final boolean[] waiting = { false, false, false, false, false };

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
     * Set the interrupts waiting to be enabled.
     */
    public void waitForIME() {
        waitIME = true;
        ime = false;
    }

    /**
     * Enable all interrupts speficied in the {@code ie} hardware register.
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
        waiting[bit] = true;
        hwreg.setBit(IF, bit);
    }

    /**
     * Call the currently waiting interrupt.
     * 
     * @return {@code true} if the call was successful, {@code false} otherwise.
     */
    public boolean callWaiting() {
        for (int i = 0; i < waiting.length; i++) {
            if (waiting[i]) {
                InterruptType interruptToCall = InterruptType.values()[i];
                if (callInterrupt(interruptToCall)) {
                    return true;
                }
            }
        }
        return false;
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
            if (!(Bitwise.isBitSet(hwreg.read(IE), bit)) || !(Bitwise.isBitSet(hwreg.read(IF), bit))) {
                return false;
            }
        }
        disable();
        gb.timing.incCycles();
        gb.timing.incCycles();
        sp.push(pc.get());

        pc.set(Bitwise.toShort(it.address));

        waiting[bit] = false;
        hwreg.resetBit(IF, bit);

        return true;
    }

}
