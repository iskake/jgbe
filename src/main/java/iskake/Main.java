package iskake;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import iskake.gb.GameBoy;
import iskake.gb.mem.ROM;

/**
 * Main class
 */
public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            // Interpreter
            GameBoy gb = new GameBoy(null);
            gb.run();
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
            GameBoy gb = new GameBoy(rom);

            gb.enableDebugger();
            gb.run();
        } else {
            // Invalid
            System.err.println("Invalid arguments.\nUsage: jgbe [file]");
            System.exit(-1);
        }
    }
}
