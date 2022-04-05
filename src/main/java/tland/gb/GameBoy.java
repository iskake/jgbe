package tland.gb;

import tland.Bitwise;
import tland.gb.Registers.Flags;
import tland.gb.Registers.RegisterIndex;
import tland.gb.cpu.CPU;
import tland.gb.mem.CartridgeROM;
import tland.gb.mem.MemoryMap;

public class GameBoy {
    private CartridgeROM rom;
    private CPU cpu;
    private short pc;
    public final StackPointer sp;
    public final Registers reg;
    public final HardwareRegisters hwReg;
    public final MemoryMap memoryMap;

    private boolean debuggerEnabled;

    public GameBoy(CartridgeROM rom) {
        debuggerEnabled = true;
        this.rom = rom;
        reg = new Registers(this);
        hwReg = new HardwareRegisters();
        cpu = new CPU(this);
        memoryMap = new MemoryMap(rom, hwReg);

        // TODO: use BootROM instead of hardcoded values, as this may depend on
        // revisions. In this case, the values are for the DMG (NOT the DMG0)
        pc = Bitwise.toShort(0x100);
        sp = new StackPointer(this, (short) 0xfffe);
        reg.writeRegisterByte(RegisterIndex.A, 0x01);
        reg.setFlag(Flags.Z);
        reg.resetFlag(Flags.N);
        reg.setFlag(Flags.H);
        reg.setFlag(Flags.C);
        reg.writeRegisterShort(RegisterIndex.BC, (short) 0x0013);
        reg.writeRegisterShort(RegisterIndex.DE, (short) 0x00d8);
        reg.writeRegisterShort(RegisterIndex.HL, (short) 0x014d);
    }

    /**
     * Get the memory address currently pointed to by the program counter.
     * <p>
     * Used for jump instructions.
     * 
     * @return The address of the program counter.
     */
    public short getPC() {
        return pc;
    }

    /**
     * Set the program counter to point at the specified address.
     * <p>
     * Used for jump instructions.
     */
    public void setPC(short address) {
        pc = address;
    }

    public void enableDebugger() {
        debuggerEnabled = true;
    }

    public void disableDebugger() {
        debuggerEnabled = false;
    }

    public void run() {
        while (true) {
            if (!debuggerEnabled)
                cpu.step();
            else
                new Debugger(this, cpu).run();
        }
    }

    public CartridgeROM getROM() {
        return rom;
    }

    public byte readNextByte() {
        return memoryMap.readByte(pc++);
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
     * Halt ('stop') all operations of the Game Boy until the system is reset or any
     * input is pressed.
     * <p>
     * Note that the stop instruction is actually 2 bytes long, with the second byte
     * being ignored.
     */
    public void stop() {
        // TODO
        pc++; // stop ignores the next instruction, so

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
        // TODO
    }

    /**
     * Enables all interrupts enabled in the IE hardware register by setting the IME
     * flag to 1.
     */
    public void enableInterrupts() {
        // TODO
    }
}
