package tland.gb;

import tland.Bitwise;
import tland.gb.Registers.Flags;
import tland.gb.Registers.RegisterIndex;
import tland.gb.mem.CartridgeROM;
import tland.gb.mem.MemoryMap;

public class GameBoy {
    private CartridgeROM rom;
    private CPU cpu;
    private short pc;
    private short sp;
    public Registers reg;
    public MemoryMap memoryMap;


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
        reg.writeRegisterShort(RegisterIndex.BC, (short)0x0013);
        reg.writeRegisterShort(RegisterIndex.DE, (short)0x00d8);
        reg.writeRegisterShort(RegisterIndex.HL, (short)0x014d);
    }

    public short getPC() {
        return pc;
    }

    public void setPC(short address) {
        pc = address;
    }

    public short getSP() {
        return sp;
    }

    public void setSP(short value) {
        sp = value;
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
}
