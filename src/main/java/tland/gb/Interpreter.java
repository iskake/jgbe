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
                for (int k = 0; k < inParts.length; k++) {
                    if (opParts[k].equals(inParts[k])) {
                        matchingParts[k] = true;
                    } else {
                        if (opParts[k].equals("$_N8") && inParts[k].contains("$")) {
                            matchingParts[k] = true;
                        } else if (opName.contains("$_N16") && inParts[k].contains("$")) {
                            matchingParts[k] = true;
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
                    System.out.println("Found match for " + line + ": " + opName + "(opcode " + Integer.toHexString(i) + ")");
                    int pcVal = gb.pc().get();
                    gb.writeMemoryAddress(gb.pc().inc(), Bitwise.toByte(i));
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
