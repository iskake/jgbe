package tland;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import tland.gb.GameBoy;
import tland.gb.ROM;

public class Main 
{
    public static void main(String[] args)
    {
        try {
            Path romFile = Paths.get("rom/test.gb");
            ROM rom = new ROM(Files.readAllBytes(romFile));

            GameBoy gb = new GameBoy(rom);

            gb.run();
        } catch (IOException e) {
            System.out.println("File not found: " + e.getMessage());
        }
    }
}
