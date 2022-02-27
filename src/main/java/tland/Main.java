package tland;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import tland.gb.GameBoy;
import tland.gb.mem.CartridgeROM;

public class Main 
{
    public static void main(String[] args)
    {
        CartridgeROM rom;
        try {
            Path romFile = Paths.get("rom/test.gb");
            rom = new CartridgeROM(Files.readAllBytes(romFile));
            
        } catch (IOException e) {
            System.out.println("File not found: " + e.getMessage());
            return;
        }

        GameBoy gb = new GameBoy(rom);

        gb.run();
    }
}
