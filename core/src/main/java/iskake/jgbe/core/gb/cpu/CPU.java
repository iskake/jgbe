package iskake.jgbe.core.gb.cpu;

import iskake.jgbe.core.gb.IGameBoy;
import iskake.jgbe.core.gb.interrupt.InterruptHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CPU {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final IGameBoy gb;
    private final InterruptHandler interrupts;

    /** The CPU clock speed, measured in Hz. */
    public static final int CLOCK_SPEED = 0x400000;

    public CPU(IGameBoy gameBoy, InterruptHandler interrupts) {
        this.gb = gameBoy;
        this.interrupts = interrupts;
    }

    /**
     * 'Step' the CPU forward by a single instruction.
     */
    public void step() {
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

        // TODO: Do something about this, for example: show an alert, don't just exit.
        if (oldPC == newPC && !interrupts.enabled()) {
            gb.reg().printValues();
            printNextInstruction();
            log.error("JGBE has encountered an infinite loop. Exiting...");
            System.exit(0);
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

}
