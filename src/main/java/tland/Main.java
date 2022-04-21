package tland;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import tland.gb.GameBoy;
import tland.gb.mem.CartridgeROM;

public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            // Interpreter
            System.out.print("Enter a file to open: ");
            Scanner sc = new Scanner(System.in);

            Path path = Paths.get(sc.nextLine());
            byte[] romFile;
            try {
                romFile = Files.readAllBytes(path);
            } catch (Exception e) {
                System.err.printf("Could not read %s: an exception occurred: %s\n", path.getFileName(), e.toString());
                sc.close();
                return;
            }

            CartridgeROM rom = new CartridgeROM(romFile);
            GameBoy gb = new GameBoy(rom);

            gb.enableDebugger();
            gb.run();

            sc.close();
        } else if (args.length == 1) {
            Path path = Paths.get(args[0]);
            byte[] romFile;
            try {
                romFile = Files.readAllBytes(path);
            } catch (Exception e) {
                System.err.printf("Could not read %s: an exception occurred: %s\n", path.getFileName(), e.toString());
                return;
            }

            CartridgeROM rom = new CartridgeROM(romFile);
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
