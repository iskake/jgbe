package tland.emu;

import java.util.Scanner;

import tland.Bitwise;
import tland.Header;
import tland.emu.cpu.CPU;
import tland.emu.mem.ROM;
import tland.emu.mem.MemoryMap;

/**
 * Represents the JGBE emulator/interpreter/virtual machine.
 */
public class Emulator implements Runnable, IEmulator {
    private final ProgramCounter pc;
    private final StackPointer sp;
    private final Registers reg;
    private final CPU cpu;
    private final MemoryMap memoryMap;
    private final Scanner sc;

    private ROM rom;
    private Debugger dbg;
    private boolean debuggerEnabled;
    private boolean running;
    private boolean runInterpreter;
    private boolean ignoreGB;
    private Interpreter interpreter;

    public Emulator(ROM rom) {
        this(rom, false);
    }

    public Emulator(ROM rom, boolean ignoreGB) {
        this.sc = new Scanner(System.in);
        debuggerEnabled = true;
        this.rom = rom;
        this.ignoreGB = ignoreGB;

        runInterpreter = (rom == null) ? true : false;

        pc = new ProgramCounter((short) 0x100);
        sp = new StackPointer(this, (short) 0xfffe);
        reg = new Registers(this);
        memoryMap = new MemoryMap(rom);
        cpu = new CPU(this);

        init();
    }

    /**
     * Restart the system with a new ROM file
     * 
     * @param rom The ROM to restart the system with.
     */
    public void restart(ROM rom) {
        this.rom = rom;
        memoryMap.restart(rom);
        runInterpreter = (rom == null) ? true : false;
        init();
    }

    /**
     * Initialize the emulator.
     * This method should be used in the constructor and when the emulator is reset
     * ('powered on').
     */
    private void init() {
        reg.clearAll();

        pc.set((short) 0x0100);
        sp.set((short) 0xfffe);
        memoryMap.init();

        if (!runInterpreter) {
            checkGameBoyHeader();
        }

        dbg = new Debugger(this, cpu);
        running = true;
    }

    /**
     * Check if the provided file is a valid Game Boy ROM file.
     * <p>
     * If it is, show a warning message.
     */
    private void checkGameBoyHeader() {

        boolean validGBHeader = false;
        if (Header.validLogo(rom)) {
            System.out.println("Valid logo!");
            validGBHeader = true;
        }
        if (Header.validHeaderChecksum(rom)) {
            System.out.println("Valid checksum!");
            validGBHeader = true;
        }

        if (validGBHeader) {
            if (ignoreGB) {
                return;
            }
            System.out.println("Warning: The provided file is in the format of a Game Boy ROM.");
            System.out.println("Although this file may run just fine, JGBE is not designed to run Game Boy ROMs.");
            Header header = new Header(rom);
            System.out.println("\nInternal ROM Information:");
            System.out.println("    Name: '" + header.getTitle().strip() + "'");
            System.out.println("    ROM size: " + header.getROMSizeString());
            System.out.println("    RAM size: " + header.getRAMSizeString());
            System.out.print("\nDo you still wish to continue (y/n)?  ");

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

    @Override
    public byte readNextByte() {
        return memoryMap.readAddress(pc.inc());
    }

    @Override
    public short readNextShort() {
        byte lo = readNextByte();
        byte hi = readNextByte();
        return Bitwise.toShort(hi, lo);
    }

    @Override
    public byte readMemoryAddress(short address) {
        return memoryMap.readAddress(address);
    }

    @Override
    public void writeMemoryAddress(short address, byte value) {
        memoryMap.writeAddress(address, value);
    }

    @Override
    public void stop() {
        stopRunning();
    }

    @Override
    public void halt(short millis) {
        try {
            // Max 65.535 seconds
            Thread.sleep(Short.toUnsignedInt(millis));
        } catch (InterruptedException e) {
            System.out.println("Sleep was interrupted.");
        }
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
     * Stop execution.
     */
    public void stopRunning() {
        running = false;
    }

    /**
     * Check if the emulator is running.
     * 
     * @return {@code true} if the emulator is running, {@code false} otherwise.
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * Enable the debugger.
     */
    public void enableDebugger() {
        debuggerEnabled = true;
    }

    /**
     * Disable the debugger.
     */
    public void disableDebugger() {
        debuggerEnabled = false;
    }

    /**
     * Run the emulator
     */
    @Override
    public void run() {
        if (runInterpreter) {
            interpreter = new Interpreter(this);
            interpreter.interpret(sc);
            pc.set((short) 0x0100);
        }

        if (debuggerEnabled) {
            System.out.println("Debugger is enabled. Type \"help\" or \"h\" for help");
        }

        while (running) {
            if (!debuggerEnabled)
                cpu.step();
            else
                dbg.step();
        }
    }

    @Override
    public ProgramCounter pc() {
        return pc;
    }

    @Override
    public StackPointer sp() {
        return sp;
    }

    @Override
    public Registers reg() {
        return reg;
    }
}
