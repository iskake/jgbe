package iskake.gb;

import java.util.stream.Stream;

import javax.swing.ImageIcon;

import iskake.Bitwise;
import iskake.gb.HardwareRegisters.HardwareRegisterIndex;
import iskake.gb.Registers.Flags;
import iskake.gb.Registers.RegisterIndex;
import iskake.gb.controller.GameBoyJoypad;
import iskake.gb.cpu.CPU;
import iskake.gb.joypad.IJoypad;
import iskake.gb.mem.CartridgeROM;
import iskake.gb.mem.MemoryMap;
import iskake.gb.mem.OAM;
import iskake.gb.mem.VRAM;
import iskake.gb.ppu.PPU;
import iskake.gb.ppu.PPUController;
import iskake.gb.timing.Timing;
import iskake.gb.view.GameBoyViewable;

/**
 * Represents a Game Boy (model 'DMG')
 */
public class GameBoy implements Runnable, GameBoyViewable {
    // TODO: joypad handling+dma+video
    public final ProgramCounter pc;
    public final StackPointer sp;
    public final Registers reg;
    public final Timing timing; // TODO: fix having to pass GameBoy to constructors just to access timing?

    private CartridgeROM rom;
    private final CPU cpu;
    private final PPU ppu;
    private final HardwareRegisters hwreg;
    private MemoryMap memoryMap;
    private final InterruptHandler interrupts;

    private Debugger dbg;
    private boolean debuggerEnabled;
    private boolean running;

    public GameBoy(IJoypad joypad) {
        debuggerEnabled = true;

        pc = new ProgramCounter(this, (short) 0x100);
        sp = new StackPointer(this, (short) 0xfffe);

        DMAController dmaControl = new DMAController(this);
        reg = new Registers(this);
        hwreg = new HardwareRegisters(dmaControl, joypad);

        PPUController ppuControl = new PPUController(hwreg);
        VRAM vram = new VRAM(0x2000, ppuControl);
        OAM oam = new OAM(40 * 4, ppuControl);
        ppu = new PPU(vram, oam, hwreg, ppuControl);

        memoryMap = new MemoryMap(hwreg, vram, oam);

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
        reg.writeRegisterByte(RegisterIndex.A, 0x01);
        reg.setFlag(Flags.Z);
        reg.resetFlag(Flags.N);
        reg.setFlag(Flags.H);
        reg.setFlag(Flags.C);
        reg.writeRegisterShort(RegisterIndex.BC, (short) 0x0013);
        reg.writeRegisterShort(RegisterIndex.DE, (short) 0x00d8);
        reg.writeRegisterShort(RegisterIndex.HL, (short) 0x014d);

        pc.init((short) 0x100);
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
    public void restart(CartridgeROM rom) {
        init(rom);
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
        timing.incCycles();
        return memoryMap.readByte(pc.inc());
    }

    public short readNextShort() {
        byte lo = readNextByte();
        byte hi = readNextByte();
        return Bitwise.toShort(hi, lo);
    }

    public void writeMemoryAddress(short address, byte value) {
        timing.incCycles();
        memoryMap.writeByte(address, value);
    }

    public void writeMemoryNoCycle(short address, byte value) {
        memoryMap.writeByte(address, value);
    }

    public byte readMemoryNoCycle(short address) {
        return memoryMap.readByte(address);
    }

    public byte readMemoryAddress(short address) {
        timing.incCycles();
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
            System.out.printf("%02x ", readMemoryNoCycle((short) i));
            c++;
        }
        System.out.println("\n");
    }

    /**
     * Halt ('stop') all operations of the Game Boy until the system is reset or any
     * input is pressed.
     * <p>
     * Note that the stop instruction is actually 2 bytes long, with the second byte
     * being ignored.
     */
    public void stop() {
        // TODO
        pc.inc(); // stop ignores the next instruction, so

    }

    /**
     * Halt CPU instruction execution
     */
    public void halt() {
        // TODO
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

    // Temporary: print the current frame (each dot as a byte).
    public void printFrame() {
        byte[][] scanlines = ppu.getFrame();
        for (int i = 0; i < scanlines.length; i++) {
            for (int j = 0; j < scanlines[i].length; j++) {
                System.out.printf("%02x", scanlines[i][j]);
            }
            System.out.println();
        }
    }

    @Override
    public byte[] getFrame() {
        byte[][] in = ppu.getFrame();
        byte[] out = new byte[in.length * in[0].length];
        for(int i = 0; i < in.length; i ++) {
            for(int j = 0; j < in[0].length; j ++) {
                out[(i * in.length) + j] = in[i][j];
            }
        }
        return out;
    }

    private int[] mappedColors = {
        0xffffff,
        0xaaaaaa,
        0x555555,
        0x000000,
    };

    @Override
    public int[] getFrameInt() {
        byte[][] in = ppu.getFrame();
        int[] out = new int[in.length * in[0].length];
        for(int i = 0; i < in.length; i ++) {
            for(int j = 0; j < in[0].length; j ++) {
                out[(i * in.length) + j] = mappedColors[in[i][j]];
            }
        }
        return out;
    }

	@Override
	public boolean canGetFrame() {
		return hwreg.readRegisterInt(HardwareRegisterIndex.LY) >= 144;
	}
}
