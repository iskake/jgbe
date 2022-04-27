package tland.emu;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import tland.Bitwise;
import tland.emu.cpu.Opcodes;
import tland.emu.mem.ROM;
import tland.pair.Pair;
import tland.pair.Pairs;
import tland.pair.SimplePair;
import tland.pair.SimplePairs;

public class Interpreter {

    private boolean finishedInterpreting;
    private Emulator emu;
    private Map<String, Short> labels;
    private Pairs<String, String> commands = new SimplePairs<>();

    public Interpreter(Emulator emu) {
        this.emu = emu;
        finishedInterpreting = false;
        labels = new HashMap<>();

        commands.add(new SimplePair<String, String>("help",
                "Show the help dialog."));
        commands.add(new SimplePair<String, String>("cmds",
                "Show the commands dialog (the one you are currently reading)."));
        commands.add(new SimplePair<String, String>("inst",
                "Print out all instructions."));
        commands.add(new SimplePair<String, String>("run",
                "Run the program written to RAM."));
        commands.add(new SimplePair<String, String>("open",
                "Open a program and run it. The program should have a '.zb' filename extension"));
        commands.add(new SimplePair<String, String>("undo",
                "Delete the byte stored at and and decrement the program counter. (WARNING: Unsafe)"));
        commands.add(new SimplePair<String, String>("setpc",
                "Set the program counter to a specific address or label."));

    }

    /**
     * REPL. Each instruction is written to memory.
     * 
     * @param sc Scanner to read input from.
     */
    public void interpret(Scanner sc) {
        System.out.println("Welcome to the JGBE Interpreter.");
        System.out.println(
                "Type \"help\" for help, \"cmds\" for a list of commands, \"inst\" for a list of valid instructions.");
        while (!finishedInterpreting) {
            boolean match = false;
            System.out.print(">>> ");

            String line = sc.nextLine().strip();
            String[] inParts = line.split(" ");
            if (handleCommands(inParts)) {
                continue;
            }

            for (int i = 0; i < 0x100; i++) {
                if (handleSpecialInstruction(line)) {
                    match = true;
                    break;
                }

                String opName = Opcodes.getOpcode(i).getName();

                String[] opParts = opName.split(" ");
                if (inParts.length != opParts.length) {
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
                        // 'ldh' opcodes
                        // 'ld a, ptr/ld ptr, a'
                        // 'ei' and 'di' are undefined
                        String fixedName = inParts[k];
                        if (inParts[k].contains("[") && opParts[k].contains("[")) {
                            fixedName = (inParts[k].replaceAll("\\[(.+)\\]", "$1"));
                        } else if (inParts[k].contains("[") || opParts[k].contains("[")) {
                            break;
                        }

                        if (opParts[k].equals("$_N8")) {
                            if (fixedName.contains("$")) {
                                matchingParts[k] = true;
                                decodedShort = false;
                                value = Bitwise.decodeInt(fixedName);
                            } else if (stringInIterable(fixedName, labels.keySet())) {
                                matchingParts[k] = true;
                                decodedShort = false;
                                if (opParts[0].equals("jr")) {
                                    // TODO?
                                    value = Short.toUnsignedInt(labels.get(fixedName));
                                } else {
                                    value = Short.toUnsignedInt(labels.get(fixedName));
                                }
                            } else {
                                try {
                                    value = Bitwise.decodeInt(fixedName);
                                    matchingParts[k] = true;
                                    decodedShort = false;
                                } catch (Exception e) {
                                    // move on...
                                }
                            }
                        } else if (opParts[k].equals("$_N16")) {
                            if (fixedName.contains("$")) {
                                matchingParts[k] = true;
                                decodedShort = true;
                                value = Bitwise.decodeInt(fixedName);
                            } else if (stringInIterable(fixedName, labels.keySet())) {
                                matchingParts[k] = true;
                                decodedShort = true;
                                value = Short.toUnsignedInt(labels.get(fixedName));
                            } else {
                                try {
                                    value = Bitwise.decodeInt(fixedName);
                                    matchingParts[k] = true;
                                    decodedShort = true;
                                } catch (Exception e) {
                                    // move on...
                                }
                            }
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
                    emu.writeMemoryAddress(emu.pc().inc(), Bitwise.toByte(i));
                    if (value != null) {
                        if (decodedShort) {
                            byte lo = (byte) ((value & 0xff00) >> 8);
                            byte hi = (byte) (value & 0xff);
                            emu.writeMemoryAddress(emu.pc().inc(), hi);
                            emu.writeMemoryAddress(emu.pc().inc(), lo);
                        } else {
                            byte lo = (byte) (value & 0xff);
                            emu.writeMemoryAddress(emu.pc().inc(), lo);
                        }
                    }
                    break;
                }
            }
            if (!match) {
                System.out.println("Invalid input '" + line + "'");
            }
        }
    }

    /**
     * Handle commands.
     * 
     * @param s The array to read the input from.
     * @return {@code true} if a command was run, {@code false} otherwise.
     */
    private boolean handleCommands(String[] s) {
        switch (s[0]) {
            case "help" -> {
                printHelp();
                return true;
            }
            case "cmds" -> {
                printCommands();
                return true;
            }
            case "inst" -> {
                printInstructions();
                return true;
            }
            case "run" -> {
                emu.writeMemoryAddress(emu.pc().get(), (byte) 0x10); // stop
                finishedInterpreting = true;
                return true;
            }
            case "open" -> {
                try {
                    Path path = Paths.get(s[1]);
                    byte[] romFile;
                    romFile = Files.readAllBytes(path);

                    ROM rom = new ROM(romFile);
                    emu.restart(rom);
                    finishedInterpreting = true;
                    return true;
                } catch (Exception e) {
                    System.err.println("Could not read the file! Returning to the interpreter.");
                    return true;
                }
            }
            case "undo" -> {
                short pc = emu.pc().get();
                byte value = emu.readMemoryAddress(pc);
                emu.writeMemoryAddress(emu.pc().dec(), (byte) 0);
                System.out.printf("Undo of value: $%02x at address $%04x\n", value, pc);
                return true;
            }
            case "exit" -> {
                emu.stop();
                return true;
            }
            case "debugger" -> {
                try {
                    if (s[1].equals("enable")) {
                        emu.enableDebugger();
                        return true;
                    } else if (s[1].equals("disable")) {
                        emu.disableDebugger();
                        return true;
                    } else {
                        System.err.println("Invalid syntax: write `debugger [enable/disable]`");
                        return true;
                    }
                } catch (Exception e) {
                    System.err.println("Invalid syntax: write `debugger [enable/disable]`");
                    return true;
                }
            }
            case "setpc" -> {
                try {
                    if (labels.containsKey(s[1])) {
                        emu.pc().set(labels.get(s[1]));
                        emu.enableDebugger();
                        return true;
                    } else {
                        emu.pc().set(Bitwise.toShort(Bitwise.decodeInt(s[1])));
                        return true;
                    }
                } catch (Exception e) {
                    System.err.println("Invalid syntax: write `setpc [$ADDR/0xADDR/label]`");
                    return true;
                }
            }
            case "x", "examine" -> {

                return true;
            }
        }

        return false;
    }

    /**
     * Print the help dialog.
     */
    private void printHelp() {
        System.out.println("Welcome to the JGBE command line interpreter.");
        System.out.println(
                "Type \"help\" for help, \"cmds\" for a list of commands, \"inst\" for a list of valid instructions.");
        System.out.println("\nIn the command line interpreter, instructions typed in will be written to memory");
        System.out.println("and executed after sending the `run` command.");
        System.out.println(
                "\nAn example run of a program (with debugger disabled, running `debugger disable` first):\n");
        System.out.println("    >>> ld a, $10");
        System.out.println("    >>> ld b, $67");
        System.out.println("    >>> add a, b");
        System.out.println("    >>> prt \"The value of a is: %x in hex (%d in decimal)\" a a");
        System.out.println("    >>> run");
        System.out.println("    The value of a is: 77 in hex (119 in decimal)");
        System.out.println(
                "\nTyping the above instructions into the interpreter will write the instructions to memory");
        System.out.println(
                "and run them. Using a debugger (type: `debugger enable`) will show that the instructions");
        System.out.println("have been written to memory and are executing.");
    }

    /**
     * Print all valid commands.
     */
    private void printCommands() {
        System.out.println("List of commands:");
        for (Pair<String, String> simplePair : commands) {
            System.out.println(simplePair.getFirst() + ":");
            System.out.println("    " + simplePair.getSecond());
        }
    }

    /**
     * Print all valid instructions.
     */
    private void printInstructions() {
        for (int i = 0; i < 0x100; i++) {
            if (i == 0xcb) {
                // Prefixed opcodes
            }
            System.out.println("%02x".formatted(i) + ": " + Opcodes.getOpcode(i).getName());
        }
    }

    /**
     * Handle special instructions.
     * 
     * @param str The array to read the tokens from.
     * @return
     */
    private boolean handleSpecialInstruction(String str) {
        String[] strParts = str.split(" ");
        if (strParts[0].charAt(strParts[0].length() - 1) == ':') {
            String fixedLabel = removeLastChar(strParts[0]);
            labels.put(fixedLabel, emu.pc().get());
            // System.out.println("Added label: " + strParts[0] + " at: " +
            // Bitwise.toHexString(emu.pc().get()));
            return true;
        }

        if (strParts[0] == "prt") {
            System.out.println("str: " + str);

            boolean validString = validPRTString(str, 4);
            if (validString) {
                System.out.println("legal string? " + str);
            }

            return true;
            // TODO!!!!
            /*
             * if (strParts.length > 2) {
             * for (int i = 2; i < strParts.length; i++) {
             * strParts[i].contains("yo");
             * }
             * }
             * 
             * int pcVal = Short.toUnsignedInt(emu.pc().get());
             * emu.writeMemoryAddress(emu.pc().inc(), (byte)0xfc); // opcode
             * // emu.writeMemoryAddress(emu.pc().inc(), (byte)strParts[1].charAt(i));
             * emu.writeMemoryAddress(emu.pc().inc(), (byte)0); // null terminated string.
             * emu.writeMemoryAddress(emu.pc().inc(), (byte)1); // arg length
             * emu.writeMemoryAddress(emu.pc().inc(), (byte)0); // arg type (a)
             * 
             * // emu.printMemoryRegion(pcVal & 0xfff0, ((pcVal + 32) & 0xfff0) - 1);
             * 
             * return true;
             */
        }

        return false;
    }

    /**
     * Check if a string is valid for the {@code prt} instruction.
     * 
     * @param str       The string to for valid format.
     * @param startChar The index of the starting char to check
     * @return The 
     */
    public static boolean validPRTString(String str, int startChar) {
        for (int i = startChar; i < str.length(); i++) {
            if (i == startChar) {
                if (str.charAt(i) != '"') {
                    return false;
                }
            } else {
                if (str.charAt(i) == '"' && str.charAt(i - 1) != '\\') {
                    try {
                        char c = str.charAt(i + 1);
                        return (c == ' ') ? true : false;
                    } catch (IndexOutOfBoundsException e) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Helper method to check if a string is in an interable.
     * <p>
     * Supposed to be equivalent to the {@code in} keyword of python
     * (e.g. {@code string in list}).
     * 
     * @param a The string to check if it is in the iterable.
     * @param s The iterable to check if the string is in.
     * @return {@code true} if the string is in the iterable, {@code false}
     *         otherwise.
     */
    private boolean stringInIterable(String a, Iterable<String> s) {
        for (String b : s) {
            if (a.equals(b)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Remove the last char from a string
     * 
     * @param s The string to remove the last char from.
     * @return The string with the last char removed
     */
    private String removeLastChar(String s) {
        return s.substring(0, s.length() - 1);
    }

}
