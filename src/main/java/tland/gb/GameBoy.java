package tland.gb;

import tland.Bitwise;
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
        reg = new Registers();
        cpu = new CPU(this);
        pc = 0;
        sp = Bitwise.toShort(0xffff);
    }

    public void run() {

        while (true) {
            cpu.run();
        }
    }

    public byte readNextByte() {
        byte val = memoryMap.readByte(++pc);
        return val;
    }

    public short readNextShort() {
        byte lo = readNextByte();
        byte hi = readNextByte();
        return Bitwise.toShort(hi, lo);
    }

    public void writeMemoryAddress(short address, byte value) {
    }
}
