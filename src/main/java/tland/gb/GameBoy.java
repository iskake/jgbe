package tland.gb;

import java.util.Scanner;

import tland.Bitwise;
import tland.gb.Registers.Flags;
import tland.gb.Registers.RegisterIndex;
import tland.gb.cpu.CPU;
import tland.gb.mem.CartridgeROM;
import tland.gb.mem.MemoryMap;

/**
 * Represents a Game Boy (model 'DMG')
 */
public class GameBoy implements Runnable {
    public final ProgramCounter pc;
    public final StackPointer sp;
    public final Registers reg;

    private final CartridgeROM rom;
    private final CPU cpu;
    private final MemoryMap memoryMap;
    private final InterruptHandler interrupts;

    private Debugger dbg;
    private boolean debuggerEnabled;
    private boolean running;
    private boolean runInterpreter;

    public GameBoy(CartridgeROM rom) {
        debuggerEnabled = true;
        this.rom = rom;

        runInterpreter = (rom == null) ? true : false;

        pc = new ProgramCounter((short) 0x100);
        sp = new StackPointer(this, (short) 0xfffe);

        reg = new Registers(this);

        memoryMap = new MemoryMap(rom);

        interrupts = new InterruptHandler(this);
        cpu = new CPU(this);
        init();
    }

    /**
     * Initialize the Game Boy.
     * This method should be used in the constructor and when the Game Boy is reset
     * ('powered on').
     */
    private void init() {
        // ? Suggestion: use BootROM instead of hardcoded values, as this may depend on
        // ? system revisions. In this case, the values are for the DMG (_not_ the DMG0)
        reg.writeRegisterByte(RegisterIndex.A, 0x01);
        reg.setFlag(Flags.Z);
        reg.resetFlag(Flags.N);
        reg.setFlag(Flags.H);
        reg.setFlag(Flags.C);
        reg.writeRegisterShort(RegisterIndex.BC, (short) 0x0013);
        reg.writeRegisterShort(RegisterIndex.DE, (short) 0x00d8);
        reg.writeRegisterShort(RegisterIndex.HL, (short) 0x014d);

        pc.set((short) 0x0100);
        sp.set((short) 0xfffe);
        memoryMap.init();

        if (runInterpreter) {
            System.out.println("interpreter time!");
            System.exit(0);
        } else {
            checkGameBoyHeader();
        }

        dbg = new Debugger(this, cpu);
        running = true;
    }

    /**
     * Check if the provided file is a valid Game Boy ROM file.
     */
    private void checkGameBoyHeader() {
        boolean validGBHeader = false;
        if (Header.validLogo(rom.getROMBank0())) {
            System.out.println("Valid logo!");
            validGBHeader = true;
        }
        if (Header.validHeaderChecksum(rom.getROMBank0())) {
            System.out.println("Valid checksum!");
            validGBHeader = true;
        }

        if (validGBHeader) {
            System.out.println("Warning: The provided file is in the format of a Game Boy ROM.");
            System.out.println("Although this file may run just fine, JGBE is not designed to run Game Boy ROMs.");
            Header header = new Header(rom);
            System.out.println("\nInternal ROM Information:");
            System.out.println("    Name: '" + header.getTitle().strip() + "'");
            System.out.println("    ROM size: " + header.getROMSizeString());
            System.out.println("    RAM size: " + header.getRAMSizeString());
            System.out.print("\nDo you still wish to continue (y/n)?  ");
            Scanner sc = new Scanner(System.in);

            boolean invalidInput = true;
            while (invalidInput) {
                String answer = sc.nextLine();
                if (answer.toLowerCase().equals("y")) {
                    invalidInput = false;
                } else if (answer.toLowerCase().equals("n")) {
                    stopRunning();
                    return;
                } else {
                    System.out.print("\nInvalid input (y/n)  ");
                }
            }
        }
    }

    /**
     * Stop execution.
     */
    public void stopRunning() {
        running = false;
    }

    /**
     * Check if the Game Boy is running.
     * 
     * @return {@code true} if the Game Boy is running, {@code false} otherwise.
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * (Hard) restart the Game Boy.
     */
    public void restart() {
        init();
    }

    public void enableDebugger() {
        debuggerEnabled = true;
    }

    public void disableDebugger() {
        debuggerEnabled = false;
    }

    /**
     * Run the Game Boy
     */
    @Override
    public void run() {
        while (running) {
            if (!debuggerEnabled)
                cpu.step();
            else
                dbg.step();
        }
    }

    public CartridgeROM getROM() {
        return rom;
    }

    public byte readNextByte() {
        return memoryMap.readByte(pc.inc());
    }

    public short readNextShort() {
        byte lo = readNextByte();
        byte hi = readNextByte();
        return Bitwise.toShort(hi, lo);
    }

    public void writeMemoryAddress(short address, byte value) {
        memoryMap.writeByte(address, value);
    }

    public byte readMemoryAddress(short address) {
        return memoryMap.readByte(address);
    }

    /**
     * Print the HRAM ($ff80-$ffff)
     */
    public void printHRAM() {
        printMemoryRegion(0xff80, 0xffff);
    }

    /**
     * Print the contents at the memory region from {@code start} including
     * {@code end}.
     * <p>
     * Each line is formatted into 16 bytes (if applicable).
     * 
     * @param start The starting memory address to read from.
     * @param end   The memory address to read to and from.
     */
    public void printMemoryRegion(int start, int end) {
        int c = 0;
        for (int i = start; i <= end; i++) {
            if (c % 16 == 0) {
                System.out.printf("\n%04x  ", (short) i);
            }
            System.out.printf("%02x ", readMemoryAddress((short) i));
            c++;
        }
        System.out.println("\n");
    }

    /**
     * Halt ('stop') all operations of the Game Boy.
     * <p>
     * Note that the stop instruction is actually 2 bytes long, with the second byte
     * being ignored.
     */
    public void stop() {
        stopRunning();
    }

    /**
     * Halt CPU instruction execution for some time in milliseconds.
     * 
     * @param millis The time (in ms) to halt the CPU for.
     */
    public void halt(short millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            System.out.println("Sleep was interrupted.");
        }
    }

    /**
     * Disables all interrupts by setting the IME flag to 0.
     */
    public void disableInterrupts() {
        interrupts.disable();
    }

    /**
     * Enables all interrupts enabled in the IE hardware register by setting the IME
     * flag to 1.
     * 
     * @param wait If interrupts should be enabled after waiting one M-cycle.
     */
    public void enableInterrupts(boolean wait) {
        interrupts.enable(wait);
    }
}
