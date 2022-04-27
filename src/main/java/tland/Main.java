package tland;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import tland.emu.Emulator;
import tland.emu.mem.ROM;

public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            // Interpreter
            Emulator emu = new Emulator(null);
            emu.run();
        } else if (args.length == 1) {
            Path path = Paths.get(args[0]);
            byte[] romFile;
            try {
                romFile = Files.readAllBytes(path);
            } catch (Exception e) {
                System.err.printf("Could not read %s: an exception occurred: %s\n", path.getFileName(), e.toString());
                return;
            }

            ROM rom = new ROM(romFile);
            Emulator emu = new Emulator(rom);

            emu.enableDebugger();
            emu.run();
        } else {
            // Invalid
            System.err.println("Invalid arguments.\nUsage: jgbe [file]");
            System.exit(-1);
        }
    }
}
