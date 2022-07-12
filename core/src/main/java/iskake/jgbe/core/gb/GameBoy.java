package iskake.jgbe.core.gb;

import iskake.jgbe.core.gb.HardwareRegisters.HardwareRegister;
import iskake.jgbe.core.gb.Registers.Flags;
import iskake.jgbe.core.gb.Registers.Register;
import iskake.jgbe.core.gb.cpu.CPU;
import iskake.jgbe.core.gb.interrupt.InterruptHandler;
import iskake.jgbe.core.gb.joypad.IJoypad;
import iskake.jgbe.core.gb.mem.CartridgeROM;
import iskake.jgbe.core.gb.mem.MemoryMap;
import iskake.jgbe.core.gb.mem.OAM;
import iskake.jgbe.core.gb.mem.VRAM;
import iskake.jgbe.core.gb.pointer.ProgramCounter;
import iskake.jgbe.core.gb.pointer.StackPointer;
import iskake.jgbe.core.gb.ppu.PPU;
import iskake.jgbe.core.gb.ppu.PPUController;
import iskake.jgbe.core.gb.timing.Timing;
import iskake.jgbe.core.Bitwise;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Represents a Game Boy (model 'DMG')
 */
public class GameBoy implements IGameBoy, GameBoyDisplayable, Runnable {
    // TODO: joypad handling+dma+video
    public final ProgramCounter pc;
    public final StackPointer sp;
    public final Registers reg;
    public final Timing timing; // TODO: fix having to pass GameBoy to constructors just to access timing?

    private CartridgeROM rom;
    private final CPU cpu;
    private final PPU ppu;
    private final HardwareRegisters hwreg;
    private final MemoryMap memoryMap;
    private final InterruptHandler interrupts;

    private Debugger dbg;
    private boolean debuggerEnabled;
    private boolean running;
    private boolean vblankJustCalled;

    public GameBoy(IJoypad joypad) {
        debuggerEnabled = false;
        vblankJustCalled = false;

        DMAController dmaControl = new DMAController(this);
        reg = new Registers(this);
        hwreg = new HardwareRegisters(dmaControl, joypad);

        PPUController ppuControl = new PPUController(hwreg);
        VRAM vram = new VRAM(0x2000, ppuControl);
        OAM oam = new OAM(40 * 4, ppuControl);
        ppu = new PPU(vram, oam, hwreg, ppuControl);

        memoryMap = new MemoryMap(hwreg, vram, oam);

        pc = new ProgramCounter(this, (short) 0x100);
        sp = new StackPointer(this, (short) 0xfffe);

        interrupts = new InterruptHandler(this, hwreg);
        cpu = new CPU(this, interrupts);
        timing = new Timing(this, hwreg, dmaControl, interrupts, ppu);
    }

    /**
     * Initialize the Game Boy.
     * This method should be used in the constructor and when the Game Boy is reset
     * ('powered on').
     */
    private void init(CartridgeROM rom) {
        this.rom = rom;

        // ? Suggestion: use BootROM instead of hardcoded values, as this may depend on
        // ? system revisions. In this case, the values are for the DMG (_not_ the DMG0)
        reg.writeRegisterByte(Register.A, 0x01);
        reg.setFlag(Flags.Z);
        reg.resetFlag(Flags.N);
        reg.setFlag(Flags.H);
        reg.setFlag(Flags.C);
        reg.writeRegisterShort(Register.BC, (short) 0x0013);
        reg.writeRegisterShort(Register.DE, (short) 0x00d8);
        reg.writeRegisterShort(Register.HL, (short) 0x014d);

        pc.setNoCycle((short) 0x100);
        sp.set((short) 0xfffe);
        memoryMap.init(rom);
        hwreg.init();
        timing.init();

        boolean willStop = false;
        try {
            if (!ROMHeader.validLogo(rom.getROMBank0())) {
                System.out.println("Invalid logo!");
                willStop = true;
            }
            if (!ROMHeader.validHeaderChecksum(rom.getROMBank0())) {
                System.out.println("Invalid checksum!");
                willStop = true;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println(e);
            willStop = true;
        }

        if (willStop) {
            System.out.println("The provided ROM file is invalid or corrupted.");
            stopRunning();
            return;
        }

        dbg = new Debugger(this, cpu, hwreg);
        running = true;
    }

    /**
     * Hard reset the Game Boy, starts initialization over again.
     * 
     * @param rom
     */
    public void restart(CartridgeROM rom) {
        init(rom);
    }

    /**
     * Get the currently loaded ROM file, unless one is loaded.
     * 
     * @return The currently loaded ROM (or {@code null}, if none is loaded)
     */
    public CartridgeROM getROM() {
        return rom;
    }

    @Override
    public byte readNextByte() {
        timing.incCycles();
        return memoryMap.readByte(pc().inc());
    }

    @Override
    public short readNextShort() {
        byte lo = readNextByte();
        byte hi = readNextByte();
        return Bitwise.toShort(hi, lo);
    }

    @Override
    public void writeMemoryAddress(short address, byte value) {
        timing.incCycles();
        memoryMap.writeByte(address, value);
    }

    @Override
    public void writeMemoryNoCycle(short address, byte value) {
        memoryMap.writeByte(address, value);
    }

    @Override
    public byte readMemoryAddress(short address) {
        timing.incCycles();
        return memoryMap.readByte(address);
    }

    @Override
    public byte readMemoryNoCycle(short address) {
        return memoryMap.readByte(address);
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
            System.out.printf("%02x ", readMemoryNoCycle((short) i));
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
     * Check if the GameBoy is running.
     * 
     * @return {@code true} if the GameBoy is running, {@code false} otherwise.
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
     * Run the Game Boy
     */
    public void run() {
        if (debuggerEnabled) {
            System.out.println("Debugger is enabled. Type \"help\" or \"h\" for help, \"disable\" to disable.");
        }

        while (running) {
            if (!debuggerEnabled)
                cpu.step();
            else
                dbg.step();
        }
    }

    public void setVBlankJustCalled() {
        vblankJustCalled = true;
    }

    public void runUntilVBlank() {
        while (running && !vblankJustCalled) {
            if (!debuggerEnabled)
                cpu.step();
            else
                dbg.step();
        }

        vblankJustCalled = false;
    }

    @Override
    public void stop() {
        // TODO
        pc.inc(); // stop ignores the next instruction, so

    }

    @Override
    public void halt() {
        // TODO
    }

    @Override
    public void disableInterrupts() {
        interrupts.disable();
    }

    @Override
    public void enableInterrupts(boolean wait) {
        interrupts.enable(wait);
    }

    // Temporary: save the current frame as a file
    // (each dot as a number: {00,01,02,03})
    public void printFrame() {
        byte[][] scanlines = ppu.getFrame();
        try (FileWriter writer = new FileWriter("image")) {
            for (int i = 0; i < scanlines.length; i++) {
                for (int j = 0; j < scanlines[i].length; j++) {
                    writer.write("%02x".formatted(scanlines[i][j]));
                }
            }
            System.out.println("Saved screenshot to file.");
        } catch (IOException e) {
            System.err.println("Could not save screenshot to file.");
        }
    }

    // TODO: replace arraylist with screen buffer
    @Override
    public byte[] getFrame() {
        byte[][] in = ppu.getFrame();
        ArrayList<Byte> list = new ArrayList<>(in.length * in[0].length);
        for (int i = 0; i < in.length; i++) {
            for (int j = 0; j < in[0].length; j++) {
                list.add(/* out[(i * in.length) + j] = */ in[i][j]);
            }
        }

        byte[] out = new byte[in.length * in[0].length];
        for (int i = 0; i < list.size(); i++) {
            out[i] = list.get(i);
        }
        return out;
    }

    // ? TODO: This should probably be handled by the gui instead...
    private final byte[] COLORS_MAP = {
            (byte)0xff,
            (byte)0xaa,
            (byte)0x55,
            (byte)0x00,
    };

    @Override
    public byte[] getFrameMapped() {
        byte[] in = getFrame();
        byte[] out = new byte[in.length * 3];
        for (int i = 0; i < in.length * 3; i++) {
            // TODO: needs to change based on values in rBGP, rOBP0 and rOBP1
            out[i] = COLORS_MAP[in[i / 3]];
        }
        return out;
    }

    @Override
    public boolean canGetFrame() {
        return hwreg.readRegisterInt(HardwareRegister.LY) >= 144;
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
