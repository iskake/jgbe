package tland.gb;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class GameBoy {
    ROM rom;
    CPU cpu;
    int pc;
    int sp;
    public Registers reg;

    public GameBoy(ROM rom) {
        this.rom = rom;
        reg = new Registers();
        cpu = new CPU(this);
    }

    public void run() {
        cpu.run();
    }

    public int readNextByte() {
        return cpu.readNextByte();
    }
}
