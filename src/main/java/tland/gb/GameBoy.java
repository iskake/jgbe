package tland.gb;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class GameBoy {
    CPU cpu;

    public GameBoy() {
        cpu = new CPU();
    }

    public void run(Path fp) throws IOException {
        ROM rom = new ROM(Files.readAllBytes(fp));

        cpu.run(rom);
    }
}
