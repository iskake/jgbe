package tland.emu;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import tland.Bitwise;
import tland.emu.cpu.Opcodes;
import tland.emu.cpu.inst.BIT;
import tland.emu.cpu.inst.ROT;
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
                "Run the program written to RAM. Note that this will automatically write a `stop` instruction to memory."));
        commands.add(new SimplePair<String, String>("open",
                "Open a program and run it. The program should have a '.zb' filename extension"));
        commands.add(new SimplePair<String, String>("undo",
                "Delete the byte stored at the pc and and decrement the pc. (WARNING: Unsafe)"));
        commands.add(new SimplePair<String, String>("debugger",
                "Delete the byte stored at the pc and and decrement the pc. (WARNING: Unsafe)"));
        commands.add(new SimplePair<String, String>("setpc",
                "Set the program counter to a specific address or label."));
        commands.add(new SimplePair<String, String>("getpc",
                "Print out the current value of the program counter (to get back from setpc)."));
        commands.add(new SimplePair<String, String>("x, examine",
                "Examine the memory at the program counter; prints the memory at the program counter."));

    }

    /**
     * Command line interpreter 'REPL'
     * 
     * @param sc Scanner to read input from.
     */
    public void interpret(Scanner sc) {
        System.out.println("Welcome to the JGBE Interpreter.");
        System.out.println(
                "Type \"help\" for help, \"cmds\" for a list of commands, \"inst\" for a list of valid instructions.");
        while (!finishedInterpreting) {
            System.out.print(">>> ");

            String line = sc.nextLine().strip();
            String[] inParts = line.split(" ");

            // Check for commands or 'special' instructions (prt / prefixed)
            if (line == "" || handleCommands(inParts) || handleSpecialInstruction(line)) {
                continue;
            }

            boolean match = false;
            for (int i = 0; i < 0x100; i++) {

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
                        // If parts match exactly
                        matchingParts[k] = true;
                    } else {
                        // TODO: some opcode reading to fix:
                        // 'ldh' opcodes
                        String fixedName = inParts[k];
                        boolean brackets = false;
                        if (inParts[k].startsWith("[")
                                && (inParts[k].endsWith("]") || inParts[k].endsWith("],"))
                                && opParts[k].startsWith("[")
                                && (opParts[k].endsWith("]") || opParts[k].endsWith("],"))) {
                            fixedName = (inParts[k].replaceAll("\\[(.+)\\].?", "$1"));
                            brackets = true;
                        } else if (inParts[k].startsWith("[")
                                || (inParts[k].endsWith("]") || inParts[k].endsWith("],"))
                                || opParts[k].startsWith("[")
                                || (opParts[k].endsWith("]") || opParts[k].endsWith("],"))) {
                            // If only one has a '[' or ']' (with optional comma), then they must not match.
                            break;
                        }

                        // Byte decoding handle
                        if (opParts[k].equals("$_N8")
                                || (opParts[k].matches("\\[.+?_N8\\].?") && brackets)
                                || opParts[k].equals("$_N16")
                                || (opParts[k].matches("\\[.?_N16\\].?") && brackets)) {
                            if ((fixedName.charAt(0) == '$') || (fixedName.charAt(0) == '%')) {
                                matchingParts[k] = true;
                                value = Bitwise.decodeInt(fixedName);
                            } else if (stringInIterable(fixedName, labels.keySet())) {
                                // Label usage (e.g. `jp label`)
                                // Note: lower byte is used in byte instructions (`_N8`)
                                matchingParts[k] = true;
                                value = Short.toUnsignedInt(labels.get(fixedName));
                            } else {
                                // If the input does NOT have a `$` or `%` (so, possibly using decimal vs.
                                // binary or hex), but the checked instruction DOES have $_N8 (so, the
                                // instruction expects a number), then try to decode the input as a number.
                                try {
                                    value = Bitwise.decodeInt(fixedName);
                                    matchingParts[k] = true;
                                } catch (NumberFormatException e) {
                                    // move on...
                                }
                            }

                            decodedShort = (opParts[k].equals("$_N16")
                                    || (opParts[k].matches("\\[.?_N16\\].?") && brackets))
                                            ? true
                                            : false;
                        }
                    }
                }
                // Check if the strings (after decoding) match
                for (boolean validPart : matchingParts) {
                    if (!validPart) {
                        match = false;
                        break;
                    }
                    match = true;
                }
                // If they do not match
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
                System.out.println("Invalid input: '" + line + "'");
                System.out.println("Type \"help\" for help.");
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
                    System.err
                            .println("Could not read the file: " + e.getMessage() + "\nReturning to the interpreter.");
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
                System.exit(0);
                return true;
            }
            case "debugger" -> {
                try {
                    if (s[1].equals("enable")) {
                        emu.enableDebugger();
                        System.out.println("The debugger has been enabled.");
                        return true;
                    } else if (s[1].equals("disable")) {
                        emu.disableDebugger();
                        System.out.println("The debugger has been disabled.");
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
            case "getpc" -> {
                System.out.println(
                        "The program counter is currently pointing to the address: $"
                                + Integer.toHexString(emu.pc().get()));
                return true;
            }
            case "x", "examine" -> {
                short pcVal = emu.pc().get();
                emu.printMemoryRegion(pcVal & 0xfff0, ((pcVal + 56) & 0xfff0) - 1);
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
        System.out.println("There are 512 total different instructions, most being variations of another instruction.");
        System.out.println(
                "Certain instructions are also 'Prefixed', and are either bitwise instructions or bit shift instructions.");
        for (int i = 0; i < 0x100; i++) {
            if (i == 0xcb) {
                System.out.println("$cb: Prefixed instructions:");
                for (int j = 0; j < 0x100; j++) {
                    String opName = "";
                    if (j < 0x40) {
                        opName = ROT.getFixedName(j);
                    } else {
                        opName = BIT.getFixedName(j);
                    }
                    System.out.println("    $cb $%02x".formatted(j) + ": " + opName);
                }
            } else {
                System.out.println("$%02x".formatted(i) + ": " + Opcodes.getOpcode(i).getName());
            }
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
            return true;
        }

        switch (strParts[0].toLowerCase()) {
            case "jr" -> {
                // Not worth implementing, since jp is just a better jr anyway.
                System.err.println("The `jr` instructions are not supported by the interpreter. Use `jp` instead.");
                return true;
            }
            case "prefixed" -> {
                // PREFIXED is the 'name' of the prefixed instructions.
                System.err.println(
                        "To use prefixed instructions, type any of the instructions prefixed with a $cb byte (see `inst` for a list of instructions.)");
                return true;
            }
            case "illegal" -> {
                System.err.println("Cannot write illegal opcodes, they are illegal for a reason...");
                return true;
            }
            case "rlc", "rrc", "rl", "rr", "sla", "sra", "swap", "srl" -> {
                try {
                    int opcodeType = switch (strParts[0].toLowerCase()) {
                        case "rlc" -> 0b0000;
                        case "rrc" -> 0b0001;
                        case "rl" -> 0b0010;
                        case "rr" -> 0b0011;
                        case "sla" -> 0b0100;
                        case "sra" -> 0b0101;
                        case "swap" -> 0b0110;
                        case "srl" -> 0b0111;
                        default ->
                            throw new IllegalArgumentException("Invalid rotation instruction format: " + strParts[0]);
                    };
                    opcodeType <<= 3;

                    int regNum = switch (strParts[1].toLowerCase()) {
                        case "b" -> 0b000;
                        case "c" -> 0b001;
                        case "d" -> 0b010;
                        case "e" -> 0b011;
                        case "h" -> 0b100;
                        case "l" -> 0b101;
                        case "[hl]" -> 0b110;
                        case "a" -> 0b111;
                        default -> throw new IllegalArgumentException(
                                "Invalid " + strParts[0] + " format: '" + strParts[1] + "' is not a valid register.");
                    };
                    int value = opcodeType | regNum;
                    emu.writeMemoryAddress(emu.pc().inc(), (byte) 0xcb);
                    emu.writeMemoryAddress(emu.pc().inc(), (byte) value);
                } catch (Exception e) {
                    if (e instanceof IndexOutOfBoundsException ex) {
                        System.err.println("Invalid rotation instruction format!");
                    }
                    System.err.println(e.getMessage());
                }
                return true;
            }
            case "bit", "res", "set" -> {
                try {
                    int opcodeType = switch (strParts[0].toLowerCase()) {
                        case "bit" -> 0b01;
                        case "res" -> 0b10;
                        case "set" -> 0b11;
                        default ->
                            throw new IllegalArgumentException("Invalid bitwise instruction format: " + strParts[0]);
                    };
                    opcodeType <<= 6;
                    int bitNum = switch (strParts[1].toLowerCase()) {
                        case "0," -> 0;
                        case "1," -> 1;
                        case "2," -> 2;
                        case "3," -> 3;
                        case "4," -> 4;
                        case "5," -> 5;
                        case "6," -> 6;
                        case "7," -> 7;
                        default -> throw new IllegalArgumentException(
                                "Invalid " + strParts[0] + " format: '" + strParts[1]
                                        + "' is invalid: bit must be in range 0-7 (with trailing comma.)");
                    };
                    bitNum <<= 3;
                    int regNum = switch (strParts[2].toLowerCase()) {
                        case "b" -> 0b000;
                        case "c" -> 0b001;
                        case "d" -> 0b010;
                        case "e" -> 0b011;
                        case "h" -> 0b100;
                        case "l" -> 0b101;
                        case "[hl]" -> 0b110;
                        case "a" -> 0b111;
                        default -> throw new IllegalArgumentException(
                                "Invalid " + strParts[0] + " format: '" + strParts[2] + "' is not a valid register.");
                    };
                    int value = opcodeType | bitNum | regNum;
                    emu.writeMemoryAddress(emu.pc().inc(), (byte) 0xcb);
                    emu.writeMemoryAddress(emu.pc().inc(), (byte) value);
                } catch (Exception e) {
                    if (e instanceof IndexOutOfBoundsException ex) {
                        System.err.println("Invalid rotation instruction format!");
                    }
                    System.err.println(e.getMessage());
                }
                return true;
            }
            case "prt" -> {
                int strLen = PRTStringLength(str, "prt ".length());
                if (strLen == -1) {
                    System.err.println("Invalid print statement: the provided string is incorrectly formatted.");
                    return true;
                }

                String[] strArgs = {};
                byte[] byteArgs = {};

                try {
                    strArgs = str.substring("prt ".length() + strLen + "\"\"".length() + " ".length()).split(" ");
                    byteArgs = new byte[strArgs.length];

                    for (int i = 0; i < strArgs.length; i++) {
                        byteArgs[i] = switch (strArgs[i].toLowerCase()) {
                            case "a" -> (byte) 0x00;
                            case "b" -> (byte) 0x01;
                            case "c" -> (byte) 0x02;
                            case "d" -> (byte) 0x03;
                            case "e" -> (byte) 0x04;
                            case "h" -> (byte) 0x05;
                            case "l" -> (byte) 0x06;
                            case "af" -> (byte) 0x07;
                            case "bc" -> (byte) 0x08;
                            case "de" -> (byte) 0x09;
                            case "hl" -> (byte) 0x0a;
                            case "sp" -> (byte) 0x0b;
                            default ->
                                throw new IllegalArgumentException("Invalid prt argument format: " + strParts[0]);
                        };
                    }
                } catch (StringIndexOutOfBoundsException e) {
                    if (!(e instanceof StringIndexOutOfBoundsException)) {
                        System.err.println("Invalid arguments/format: " + e.getMessage());
                        return true;
                    }
                }

                emu.writeMemoryAddress(emu.pc().inc(), (byte) 0xfc);
                emu.writeMemoryAddress(emu.pc().inc(), (byte) strLen);
                if (strLen > 0) {
                    for (int i = "prt \"".length(); i < "prt \"".length() + strLen; i++) {
                        emu.writeMemoryAddress(emu.pc().inc(), (byte) str.charAt(i));
                    }
                }
                emu.writeMemoryAddress(emu.pc().inc(), (byte) byteArgs.length);
                if (byteArgs.length > 0) {
                    for (byte b : byteArgs) {
                        emu.writeMemoryAddress(emu.pc().inc(), b);
                    }
                }

                return true;
            }
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
    private int PRTStringLength(String str, int startChar) {
        for (int i = startChar; i < str.length(); i++) {
            if (i == startChar) {
                if (str.charAt(i) != '"') {
                    return -1;
                }
            } else {
                if (str.charAt(i) == '"' && str.charAt(i - 1) != '\\') {
                    try {
                        char c = str.charAt(i + 1);
                        return (c == ' ') ? i - startChar - 1 : -1;
                    } catch (IndexOutOfBoundsException e) {
                        return i - startChar - 1;
                    }
                }
            }
        }
        return -1;
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
