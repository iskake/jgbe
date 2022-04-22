package tland.gb;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

import tland.Bitwise;
import tland.gb.cpu.Opcodes;
import tland.gb.mem.CartridgeROM;

public class Interpreter {

    private boolean finished;
    private Stack<Short> writtenBytes;
    private GameBoy gb;

    public Interpreter(GameBoy gb) {
        this.gb = gb;
        finished = false;
    }

    /**
     * REPL. Each instruction is written to memory.
     * 
     * @param sc Scanner to read input from.
     */
    public void interpret(Scanner sc) {
        while (true) {
            boolean match = false;
            System.out.print(">>> ");

            String line = sc.nextLine();
            String[] inParts = line.split(" ");
            handleSpecial(inParts);

            if (finished) {
                return;
            }

            int i = 0;
            for (String opName : Opcodes.tokens) {
                String[] opParts = opName.split(" ");
                if (inParts.length != opParts.length) {
                    i++;
                    continue;
                }
                boolean[] matchingParts = new boolean[inParts.length];
                Integer value = null;
                boolean decodedShort = true;
                for (int k = 0; k < inParts.length; k++) {
                    if (opParts[k].equals(inParts[k])) {
                        matchingParts[k] = true;
                    } else {
                        // TODO: some opcode reading to fix:
                        //   'ldh' opcodes
                        //   'ld a, ptr/ld ptr, a'
                        //   'ei' and 'di' are undefined
                        String fixedName = inParts[k];
                        if (inParts[k].contains("[") && opParts[k].contains("[")) {
                            fixedName = (inParts[k].replaceAll("\\[(.+)\\]", "$1"));
                        } else if (inParts[k].contains("[") || opParts[k].contains("[")) {
                            break;
                        }
                        if (opParts[k].equals("$_N8") && fixedName.contains("$")) {
                            matchingParts[k] = true;
                            decodedShort = false;
                            value = Bitwise.decodeInt(fixedName);
                        } else if (opParts[k].equals("$_N16") && fixedName.contains("$")) {
                            matchingParts[k] = true;
                            decodedShort = true;
                            value = Bitwise.decodeInt(fixedName);
                        }
                    }
                }
                for (boolean validPart : matchingParts) {
                    if (!validPart) {
                        match = false;
                        break;
                    }
                    match = true;
                }
                if (match) {
                    System.out.println(
                            "Found match for " + line + ": " + opName + " (opcode " + Integer.toHexString(i) + ")");
                    int pcVal = gb.pc().get();
                    gb.writeMemoryAddress(gb.pc().inc(), Bitwise.toByte(i));
                    if (value != null) {
                        if (decodedShort) {
                            byte hi = (byte) ((value & 0xff00) >> 8);
                            byte lo = (byte) (value & 0xff);
                            gb.writeMemoryAddress(gb.pc().inc(), hi);
                            gb.writeMemoryAddress(gb.pc().inc(), lo);
                        } else {
                            byte lo = (byte) (value & 0xff);
                            gb.writeMemoryAddress(gb.pc().inc(), lo);
                        }
                    }
                    gb.printMemoryRegion(pcVal & 0xfff0, ((pcVal + 32) & 0xfff0) - 1);
                    break;
                }
                i++;
            }
            if (!match) {
                System.out.println("Invalid symbol '" + line + "'");
            }
        }
    }

    /**
     * Handle special tokens.
     * 
     * @param s The array to read the tokens from.
     */
    private void handleSpecial(String[] s) {
        switch (s[0]) {
            case "run" -> {
                finished = true;
                gb.writeMemoryAddress(gb.pc().get(), (byte)0x10); // stop
            }
            case "open" -> {
                try {
                    Path path = Paths.get(s[1]);
                    byte[] romFile;
                    romFile = Files.readAllBytes(path);
    
                    CartridgeROM rom = new CartridgeROM(romFile);
                    gb.restart(rom);
                    gb.enableDebugger();
                    gb.run();
                } catch (Exception e) {
                    System.err.println("Could not read the file, returning to interpreter.");
                }
            }
            // case "undo" -> 
            case "exit" -> gb.stop();
        }
    }

}
