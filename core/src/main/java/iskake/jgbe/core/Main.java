package iskake.jgbe.core;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import iskake.jgbe.core.gb.GameBoy;
import iskake.jgbe.core.gb.mem.CartridgeROM;
public class Main 
{
    public static void main(String[] args) {
        if (args.length == 0) {
            Scanner sc = new Scanner(System.in);
            System.out.print("ROM file to run: ");
            args = new String[1]; // totally a great idea
            args[0] = sc.nextLine();
        }

        if (args.length <= 1) {
            Path path = Paths.get(args[0]);
            byte[] romFile;
            try {
                romFile = Files.readAllBytes(path);
            } catch (Exception e) {
                System.err.printf("Could not read %s: an exception occurred: %s\n", path.getFileName(), e.toString());
                return;
            }

            CartridgeROM rom = new CartridgeROM(romFile);
            GameBoy emu = new GameBoy(null);

            emu.restart(rom);
            emu.enableDebugger();
            emu.run();
        } else {
            // Invalid
            System.err.println("Invalid arguments.\nUsage: jgbe [file]");
            System.exit(-1);
        }
    }
}
