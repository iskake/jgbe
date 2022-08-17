package iskake.jgbe.core.gb;

import iskake.jgbe.core.gb.cpu.Opcodes;
import iskake.jgbe.core.gb.cpu.inst.BIT;
import iskake.jgbe.core.gb.cpu.inst.ROT;
import iskake.jgbe.core.gb.mem.CartridgeROM;
import iskake.jgbe.core.Bitwise;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Command line interpreter.
 */
public class Interpreter {
    // TODO: Remove this class / rework into a minimal assembler.
    private boolean finishedInterpreting;
    private GameBoy gb;
    private Map<String, Short> labels;
    private HashMap<String, String> commands = new HashMap<>();

    public Interpreter(GameBoy gb) {
        this.gb = gb;
        finishedInterpreting = false;
        labels = new HashMap<>();

        commands.put("help",
                "Show the help dialog.");
        commands.put("cmds",
                "Show the commands dialog (the one you are currently reading).");
        commands.put("inst",
                "Print out all instructions.");
        commands.put("run",
                "Run the program written to RAM. Note that this will automatically write a `stop` instruction to memory.");
        commands.put("open",
                "Open a program and run it. The program should have a '.zb' filename extension.\nNote: opening a file will automatically enable the debugger.");
        commands.put("undo",
                "Delete the byte stored at the pc and and decrement the pc. (WARNING: Unsafe)");
        commands.put("debugger [enable/disable]",
                "Enable or disable the debugger.");
        commands.put("setpc",
                "Set the program counter to a specific address or label.");
        commands.put("getpc",
                "Print out the current value of the program counter (to get back from setpc).");
        commands.put("x, examine",
                "Examine the memory at the program counter; prints the memory at the program counter.");

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

                String opName = Opcodes.opcodeNames[i];

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

                            // A short was decoded only if the opcode name contains '_N16' according to the
                            // rules below.
                            decodedShort = (opParts[k].equals("$_N16")
                                    || (opParts[k].matches("\\[.?_N16\\].?") && brackets));
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
                if (match) {
                    gb.writeAddress(gb.pc().inc(), Bitwise.toByte(i));
                    if (value != null) {
                        if (decodedShort) {
                            byte lo = (byte) ((value & 0xff00) >> 8);
                            byte hi = (byte) (value & 0xff);
                            gb.writeAddress(gb.pc().inc(), hi);
                            gb.writeAddress(gb.pc().inc(), lo);
                        } else {
                            byte lo = (byte) (value & 0xff);
                            gb.writeAddress(gb.pc().inc(), lo);
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
                gb.writeAddress(gb.pc().get(), (byte) 0x10); // stop
                finishedInterpreting = true;
                return true;
            }
            case "open" -> {
                try {
                    Path path = Paths.get(s[1]);
                    byte[] romFile;
                    romFile = Files.readAllBytes(path);

                    CartridgeROM rom = null;//new CartridgeROM(romFile);
                    gb.enableDebugger();
                    gb.restart(rom);
                    finishedInterpreting = true;
                    return true;
                } catch (Exception e) {
                    System.err
                            .println("Could not read the file because an exception occurred: "
                                    + e.toString()
                                    + "\nPossible solution if a file could not be read: try writing the filename without quotes."
                                    + "\nFor example, write"
                                    + "\n    open src/test/asm/bin/test_alu.zb"
                                    + "\ninstead of"
                                    + "\n    open \"src/test/asm/bin/test_alu.zb\""
                                    + "\n\nReturning to the interpreter.");
                    return true;
                }
            }
            case "undo" -> {
                short pc = gb.pc().get();
                byte value = gb.readAddress(pc);
                gb.writeAddress(gb.pc().dec(), (byte) 0);
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
                        gb.enableDebugger();
                        System.out.println("The debugger has been enabled.");
                        return true;
                    } else if (s[1].equals("disable")) {
                        gb.disableDebugger();
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
                        gb.pc().set(labels.get(s[1]));
                        gb.enableDebugger();
                        return true;
                    } else {
                        gb.pc().set(Bitwise.toShort(Bitwise.decodeInt(s[1])));
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
                                + Integer.toHexString(gb.pc().get()));
                return true;
            }
            case "x", "examine" -> {
                short pcVal = gb.pc().get();
                gb.printMemoryRegion(pcVal & 0xfff0, ((pcVal + 56) & 0xfff0) - 1);
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
        for (String s : commands.keySet()) {
            System.out.println(s + ":");
            System.out.println("    " + commands.get(s));
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
                System.out.println("$%02x".formatted(i) + ": " + Opcodes.opcodeNames[i]);
            }
        }

        System.out.println(
                "\nFor writing values in the instructions, replace `_N8` and `_N16` with a 8-bit and 16-bit value respectively."
                        + "\nTo write the instruction `ld hl, $_N16` with the short value $1234, write: `ld hl, $1234`"
                        + "(note: does not have to be a hex value (starting with `$` or `0x`),"
                        + "you can also just type a normal decimal number, like so: `ld b, 23`"
                        + "(or, you can use a binary number with the prefix `%` or `0b` such as: `xor 0b10110101` ).");
    }

    /**
     * Handle 'special' instructions.
     * 
     * @param str The array to read the tokens from.
     * @return {@code true} if a special instruction was handled,
     *         {@code false} otherwise.
     */
    private boolean handleSpecialInstruction(String str) {
        String[] strParts = str.split(" ");
        if (strParts[0].charAt(strParts[0].length() - 1) == ':') {
            String fixedLabel = removeLastChar(strParts[0]);
            labels.put(fixedLabel, gb.pc().get());
            return true;
        }

        switch (strParts[0].toLowerCase()) {
            case "jr" -> {
                // Implementing the `jr` instruction would be needlessly complex for an
                // instruction that is basically just a worse version of `jp`.
                // So, the simplest solution is to just ban it from the CLI interpreter
                // (in assembled (binary) files, this is already handled for us.)
                System.err.println("The `jr` instructions are not supported by the CLI interpreter. Use `jp` instead.");
                return true;
            }
            case "prefixed" -> {
                // PREFIXED is the 'name' of the prefixed instructions.
                System.err.println(
                        "To use prefixed instructions, type any of the instructions prefixed with a $cb byte (see `inst` for a list of instructions.)");
                return true;
            }
            case "illegal" -> {
                System.err
                        .println("Writing illegal opcodes to memory is not allowed, they are illegal for a reason...");
                return true;
            }
            case "rlc", "rrc", "rl", "rr", "sla", "sra", "swap", "srl" -> {
                // Encoding the bit shift/rotate instructions can be done by reversing the
                // procedure used for decoding (see the the ROT class.)
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
                    gb.writeAddress(gb.pc().inc(), (byte) 0xcb);
                    gb.writeAddress(gb.pc().inc(), (byte) value);
                } catch (Exception e) {
                    if (e instanceof IndexOutOfBoundsException ex) {
                        System.err.println("Invalid rotation instruction format!");
                    }
                    System.err.println(e.getMessage());
                }
                return true;
            }
            case "bit", "res", "set" -> {
                // Encoding the bitwise instructions can be done by reversing the procedure
                // used for decoding (see the the BIT class.)
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
                    gb.writeAddress(gb.pc().inc(), (byte) 0xcb);
                    gb.writeAddress(gb.pc().inc(), (byte) value);
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

                // Find the amount of expected args so we can compare after.
                int expectedArgs = 0;
                for (int j = "prt \"".length(); j < "prt \"".length() + strLen; j++) {
                    if (str.charAt(j) == '%' && str.charAt(j - 1) != '\\') {
                        expectedArgs++;
                    }
                }

                // String arguments handling
                byte[] byteArgs = {};
                try {
                    String[] strArgs = str.substring("prt ".length() + strLen + "\"\"".length() + " ".length())
                            .split(" ");
                    byteArgs = new byte[strArgs.length];

                    for (int i = 0; i < strArgs.length; i++) {
                        byteArgs[i] = switch (strArgs[i].toLowerCase()) {
                            case "a", "a," -> (byte) 0x00;
                            case "b", "b," -> (byte) 0x01;
                            case "c", "c," -> (byte) 0x02;
                            case "d", "d," -> (byte) 0x03;
                            case "e", "e," -> (byte) 0x04;
                            case "h", "h," -> (byte) 0x05;
                            case "l", "l," -> (byte) 0x06;
                            case "af", "af," -> (byte) 0x07;
                            case "bc", "bc," -> (byte) 0x08;
                            case "de", "de," -> (byte) 0x09;
                            case "hl", "hl," -> (byte) 0x0a;
                            case "sp", "sp," -> (byte) 0x0b;
                            default ->
                                throw new IllegalArgumentException("Invalid PRT argument format: " + strParts[0]);
                        };
                    }
                } catch (Exception e) {
                    if (!(e instanceof StringIndexOutOfBoundsException)) {
                        System.err.println("Invalid arguments/format: " + e.getMessage());
                        return true;
                    }
                }

                // Check if the amount of args line up.
                if (expectedArgs > byteArgs.length) {
                    System.err.println("Invalid PRT argument format: too few arguments. "
                            + "(expected: " + expectedArgs + ", got: " + byteArgs.length + ")");
                    return true;
                }

                gb.writeAddress(gb.pc().inc(), (byte) 0xfc);
                gb.writeAddress(gb.pc().inc(), (byte) strLen);
                if (strLen > 0) {
                    for (int i = "prt \"".length(); i < "prt \"".length() + strLen; i++) {
                        gb.writeAddress(gb.pc().inc(), (byte) str.charAt(i));
                    }
                }
                gb.writeAddress(gb.pc().inc(), (byte) byteArgs.length);
                if (byteArgs.length > 0) {
                    for (byte b : byteArgs) {
                        gb.writeAddress(gb.pc().inc(), b);
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
