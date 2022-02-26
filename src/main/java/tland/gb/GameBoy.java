package tland.gb;

import tland.Bitwise;
import tland.gb.mem.CartridgeROM;
import tland.gb.mem.MemoryMap;

public class GameBoy {
    private CartridgeROM rom;
    private CPU cpu;
    private int pc;
    private int sp;
    public Registers reg;
    public MemoryMap memoryMap;


    public GameBoy(CartridgeROM rom) {
        this.rom = rom;
        memoryMap = new MemoryMap(rom);
        reg = new Registers();
        cpu = new CPU(this);
        pc = 0;
        sp = 0xffff;
    }

    public void run() {

        while (true) {
            cpu.run();
        }
    }

    public int readNextByte() {
        int val = memoryMap.readByte(++pc);
        return val;
    }

    public int readNextShort() {
        int lo = readNextByte();
        int hi = readNextByte();
        return Bitwise.toShort(hi, lo);
    }

    public void writeMemoryAddress(int address, int value) {
    }
}
