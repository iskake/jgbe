package iskake.jgbe.core.gb.cpu;

import iskake.jgbe.core.gb.GameBoy;
import iskake.jgbe.core.gb.interrupt.InterruptHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CPU  {
    private static final Logger log = LoggerFactory.getLogger(CPU.class);
    private final GameBoy gb;
    private final InterruptHandler interrupts;

    /** The CPU clock speed, measured in Hz. */
    public static final int CLOCK_SPEED = 0x400000;
    private boolean halted = false;

    public CPU(GameBoy gameBoy, InterruptHandler interrupts) {
        this.gb = gameBoy;
        this.interrupts = interrupts;
    }

    public void init() {
        halted = false;
    }

    /**
     * 'Step' the CPU forward by a single instruction.
     */
    public void step() {
        if (halted) {
            gb.incCycles();
            if (interrupts.enabled()) {
                if (interrupts.callWaiting()) {
                    halted = false;
                }
            } else {
                if (interrupts.anyIFSet()) {
                    halted = false;
                }
            }
        } else {
            short oldPC = gb.pc().get();
            boolean shouldEnableIME = interrupts.waitingIME();

            // Call waiting interrupts (if any)
            if (interrupts.callWaiting()) {
                return;
            }

            byte opcode = gb.readNextByte();
            Opcodes.getOpcode(opcode).doOp(gb, Byte.toUnsignedInt(opcode));

            if (shouldEnableIME) {
                interrupts.enable();
            }

            short newPC = gb.pc().get();

            // ?Should something be done about this? show an alert? (only in debug mode?) do nothing?
            if (oldPC == newPC && !interrupts.enabled()) {
                log.warn("JGBE has encountered an infinite loop.");
            }
        }
    }

    /**
     * Print the name of the next instruction to be extecuted.
     */
    public void printNextInstruction() {
        short address = gb.pc().get();
        byte opcode = gb.readAddress(address);
        System.out.printf("%02x -> %s\n", opcode, Opcodes.getInstructionName(gb, address));
    }

    public void halt() {
        halted = true;
    }
}
