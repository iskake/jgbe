package tland.gb;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class GameBoy {
    ROM rom;
    CPU cpu;

    public GameBoy(ROM rom) {
        this.rom = rom;
        cpu = new CPU(this);
    }

    public void run() {
        cpu.run();
    }
}
