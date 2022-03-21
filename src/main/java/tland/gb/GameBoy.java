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
    private short sp;
    public final Registers reg;
    public final MemoryMap memoryMap;

    public GameBoy(CartridgeROM rom) {
        this.rom = rom;
        memoryMap = new MemoryMap(rom);
        reg = new Registers(this);
        cpu = new CPU(this);

        // TODO: use BootROM instead of hardcoded values, as this may depend on
        // revisions. In this case, the values are for the DMG (NOT the DMG0)
        pc = Bitwise.toShort(0x100);
        sp = Bitwise.toShort(0xfffe);
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
     * 
     * @return The address to set the program counter to.
     */
    public void setPC(short address) {
        pc = address;
    }

    /**
     * Get the memory address currently pointed to by the stack pointer.
     * 
     * @return The address of the stack pointer.
     */
    public short getSP() {
        return sp;
    }

    /**
     * Set the stack pointer to point at the the specified address.
     * 
     * @param address The address to set the stack pointer to.
     */
    public void setSP(short address) {
        sp = address;
    }

    /**
     * Decrement the stack pointer by one.
     */
    public void decSP() {
        sp--;
    }

    /**
     * Increment the stack pointer by one.
     */
    public void incSP() {
        sp++;
    }

    /**
     * 'Push' the value {@code value} onto the stack.
     * <p>
     * Writes the value to the memory location pointed to by the stack pointer and
     * decrements the stack pointer.
     * 
     * @param value The value to push onto the stack.
     */
    public void pushSP(short value) {
        decSP();
        writeMemoryAddress(sp, Bitwise.getHighByte(value));
        decSP();
        writeMemoryAddress(sp, Bitwise.getLowByte(value));
    }

    /**
     * 'Pop' the value at the stack pointer from the stack.
     * <p>
     * Gets the value at the memory location pointed to by the stack pointer and
     * increments the stack pointer.
     * 
     * @return The value 'popped' off the stack.
     */
    public short popSP() {
        byte lo = readMemoryAddress(sp);
        incSP();
        byte hi = readMemoryAddress(sp);
        incSP();
        return Bitwise.toShort(hi, lo);
    }

    public void run() {
        while (true) {
            cpu.step();
        }
    }

    public CartridgeROM getROM() {
        return rom;
    }

    public byte readNextByte() {
        byte val = memoryMap.readByte(pc++);
        return val;
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

    public void incCycles() {
    }
}
