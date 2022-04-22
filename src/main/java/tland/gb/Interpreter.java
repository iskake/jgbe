package tland.gb;

import java.util.Scanner;

import tland.Bitwise;
import tland.gb.cpu.Opcodes;

public class Interpreter {

    private GameBoy gb;

    public Interpreter(GameBoy gb) {
        this.gb = gb;
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
            int i = 0;
            if (line.equals("run")) {
                return;
            } else if (line.equals("exit")) {
                gb.stop();
            }
            for (String opName : Opcodes.tokens) {
                String[] inParts = line.split(" ");
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
                        // TODO: fix ldh and ld [_N16]
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
                    System.out.println("Found match for " + line + ": " + opName + " (opcode " + Integer.toHexString(i) + ")");
                    int pcVal = gb.pc().get();
                    gb.writeMemoryAddress(gb.pc().inc(), Bitwise.toByte(i));
                    if (value != null) {
                        if (decodedShort) {
                            byte hi = (byte)((value & 0xff00) >> 8);
                            byte lo = (byte)(value & 0xff);
                            gb.writeMemoryAddress(gb.pc().inc(), hi);
                            gb.writeMemoryAddress(gb.pc().inc(), lo);
                        } else {
                            byte lo = (byte)(value & 0xff);
                            gb.writeMemoryAddress(gb.pc().inc(), lo);
                        }
                    }
                    gb.printMemoryRegion(pcVal & 0xfff0, (pcVal + 56) & 0xfff0);
                    break;
                }
                i++;
            }
            if (!match) {
                System.out.println("No match was found.");
            }
        }
    }
    
}
