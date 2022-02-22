package tland;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import tland.gb.GameBoy;

public class Main 
{
    public static void main(String[] args)
    {
        Path romFile = Paths.get("rom/test.gb");

        GameBoy gb = new GameBoy();
        try {
            gb.run(romFile);
        } catch (IOException e) {
            System.out.println("File not found: " + e.getMessage());
        }
    }
}
