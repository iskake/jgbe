package iskake.gb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import iskake.Bitwise;
import iskake.gb.HardwareRegisters.HardwareRegisterIndex;
import iskake.gb.cpu.CPU;
import iskake.gb.cpu.Opcodes;

/**
 * Simple CLI debugger.
 * <p>
 * Note: the debugger is inspired by GDB, so usage is similar.
 */
public class Debugger {

    private final GameBoy gb;
    private final CPU cpu;
    private final HardwareRegisters hwreg;

    private Scanner sc;
    private String[] input;
    private String[] lastInput = { "c" };
    private ArrayList<Integer> breakPoints;
    private boolean print;
    private HashMap<String, String> commands;

    public Debugger(GameBoy gb, CPU cpu, HardwareRegisters hwreg) {
        this.gb = gb;
        this.cpu = cpu;
        this.hwreg = hwreg;

        sc = new Scanner(System.in);
        breakPoints = new ArrayList<>();
        print = false;

        commands = new HashMap<>();
        commands.put("h, help",
                "Print this help information.");
        commands.put("q, quit, exit",
                "Quit JGBE.");
        commands.put("disable",
                "Disable the debugger. The program will automatically start running after issuing this command.");
        commands.put("c, continue",
                "Continue running until the program exits or a breakpoint is hit.");
        commands.put("s, step",
                "'Step into'. Will step a single instruction forward, including through function calls.");
        commands.put("n, next",
                "'Step over'. Will step a single instruction forward, stepping over function calls ('skipping them'.)");
        commands.put("b, break",
                "Create or list all breakpoints. To list all breakpoints, don't include any parameters (simply type `b`.) "
                        + "To create a breakpoint, include a parameter of which address to break at (e.g. type `b $0102` to set a breakpoint at address 0x0102.)");
        commands.put("d, delete",
                "Delete a breakpoint. If no parameter (the address of the breakpoint) is specified, the first breakpoint in the list is deleted.");
        commands.put("x, examine",
                "'Examine' the memory at the specified address (e.g. typing `x $ffd0` will print all bytes 0xffd0-0xffff)"
                        + "If no address is specified, the memory at the PC is printed instead.");
        commands.put("p",
                "Toggle printing for each instruction after continuing to run (after typing `c` or `continue`.)");
        commands.put("<nothing>",
                "Simply pressing enter (no command) will run the last command instead."
                        + "For example, if the last command was `n`, then pressing enter will have the same effect as typing `n` and pressing enter.");
    }

    /**
     * Step the debugger. This method should be invoked in the GameBoy.run()
     * method.
     * 
     * @see GameBoy#run()
     * @see CPU#step()
     */
    public void step() {
        printCPUInfo();

        System.out.print("> ");
        input = sc.nextLine().split(" ");

        if (!gb.isRunning()) {
            return;
        }

        if (input[0].length() == 0) {
            input = lastInput;
        } else {
            lastInput = input;
        }

        switch (input[0]) {
            case "h", "help" -> printHelp();
            case "q", "quit", "exit" -> gb.stopRunning();
            case "disable" -> gb.disableDebugger();
            case "c", "continue" -> continueRunning();
            case "s", "step" -> stepInto();
            case "n", "next" -> stepOver();
            case "b", "break" -> handleBreakpoints(input);
            case "d", "delete" -> handleBreakpointDeletion(input);
            case "x" -> examine(input);
            case "p", "print" -> enablePrinting();
            case "reg" -> printCPUInfo();
            case "_image" -> debugCreateImage(); // Temp!!
            case "_jp" -> debugJump(input); // Temp!!
            case "_ld" -> debugLoad(input); // Temp!!
            default -> System.err.println("Unknown command: " + input[0]);
        }
    }

    private void printHelp() {
        System.out.println("To use the debugger, type a command, optionally followed by a parameter.\n");
        System.out.println("List of commands:");
        for (String s : commands.keySet()) {
            System.out.println(s + ":");
            printStringWithLengthAndLeading(commands.get(s), 80, "   ");
        }

        System.out.println("The information below (`AF: ...`) shows the current values of the registers.");
        System.out.println(
                "For example (if registers are cleared), after running the instruction `ld bc, $34a7` (type: `n` or `s`), the output is:\n");
        System.out.println("    AF: 0000  Flags: ----\n    BC: 34a7\n    DE: 0000\n    ...");
        System.out.println(
                "If then another instruction is run, such as `ld a, $ff` (either type: `n` or `s` or simply press enter), the values will adjust accordingly:\n");
        System.out.println("    AF: ff00  Flags: ----\n    BC: 34a7\n    DE: 0000\n    ...");
        System.out.println(
                "The flags register (F) will also adjust based on the instruction as well. After running `inc a`:\n");
        System.out.println("    AF: 0000  Flags: Z--C\n    BC: 3400\n    DE: 0000\n    ...");
    }

    /**
     * Helper method to print the specified string with the specified length and
     * with teh spcecified leading string.
     * 
     * @param s       The string to print.
     * @param length  The length of each line.
     * @param leading The leading string to print at the start of each line.
     */
    private void printStringWithLengthAndLeading(String s, int length, String leading) {
        String[] stringParts = s.split(" ");
        System.out.print(leading);

        int totalLineChars = leading.length();
        totalLineChars++;
        for (String str : stringParts) {
            totalLineChars += str.length();
            totalLineChars++; // Added to account for spaces
            if (totalLineChars > length) {
                System.out.print("\n" + leading);

                totalLineChars = leading.length();
            }
            System.out.print(str + " ");
        }
        System.out.println();
    }

    private void debugLoad(String[] in) {
        gb.writeMemoryNoCycle(Bitwise.toShort(Bitwise.decodeInt(in[1])), Bitwise.toByte(Bitwise.decodeInt(in[2])));
    }

    private void debugJump(String[] in) {
        gb.pc().setNoCycle(Bitwise.toShort(Bitwise.decodeInt(in[1])));
    }

    // Temp: print frame.
    private void debugCreateImage() {
        gb.printFrame();
    }

    /**
     * Print information about the CPU and registers.
     */
    private void printCPUInfo() {
        gb.reg.printRegisters();
        System.out.println("Mode: " + (hwreg.readRegister(HardwareRegisterIndex.STAT) & 0b11)
                + " LY: " + hwreg.readRegisterInt(HardwareRegisterIndex.LY));
        System.out.println("Cycles: " + gb.timing.getCycles());
        cpu.printNextInstruction();
    }

    /**
     * Breakpoint handling. Handles creation of new breakpoints and
     * 
     * @param in The input to read the address from.
     */
    private void handleBreakpoints(String[] in) {
        if (in.length == 1) {
            System.out.println("Breakpoints: ");
            int i = 1;
            for (Integer b : breakPoints) {
                System.out.printf("%d: $%04x\n", i, b);
                i++;
            }
            return;
        }
        try {
            breakPoints.add(Bitwise.decodeInt(in[1]));
            System.out.printf("Set breakpoint at $%04x\n", breakPoints.get(breakPoints.size() - 1));
        } catch (NumberFormatException e) {
            System.err.println("Invalid syntax. Usage: `b [ |$|0x|%|0b]{MEM_ADDR}`");
        }
    }

    /**
     * Handle deletion of breakpoints.
     * 
     * @param in The input to read the address from.
     */
    private void handleBreakpointDeletion(String[] in) {
        if (in.length == 1) {
            try {
                int b = breakPoints.get(0);
                breakPoints.remove(0);
                System.out.printf("Deleted breakpoint at: $%04x\n", b);
                return;
            } catch (Exception e) {
                System.err.println("No breakpoints are defined. (use `b` to list brekpoints.)");
                return;
            }
        }
        try {
            int b = Bitwise.decodeInt(in[1]);
            breakPoints.remove(breakPoints.indexOf(b));
            System.out.printf("Deleted breakpoint at: $%04x\n", b);
        } catch (IndexOutOfBoundsException e) {
            System.err.println("No such breakpoint defined. (use `b` to list brekpoints.)");
        } catch (NumberFormatException e) {
            System.err.println("Invalid syntax. Usage: `d [ |$|0x|%|0b]{MEM_ADDR}`");
        }
    }

    /**
     * Step the program one instruction forward, including stepping into 'function'
     * calls.
     */
    private void stepInto() {
        cpu.step();
    }

    /**
     * Check if a breakpoint has been hit.
     * 
     * @return {@code true} if a breakpoint has been hit, {@code false} otherwise.
     */
    private boolean checkBreakpointHit() {
        for (Integer breakPoint : breakPoints) {
            if (gb.pc().get() == Bitwise.toShort(breakPoint)) {
                System.out.printf("Hit breakpoint at $%04x\n", breakPoint);
                return true;
            }
        }

        return false;
    }

    /**
     * Step the program one instruction forward, stepping over 'function' calls
     * (until a {@code ret} instruction is ran)
     * <p>
     * Note: if a breakpoint is hit within the function call (before a {@code ret}
     * is executed), then the debugger will stop stepping over.
     */
    private void stepOver() {
        byte opcode = gb.readMemoryAddress(gb.pc().get());
        String name = Opcodes.getOpcode(opcode).getName();
        cpu.step();
        if (name.startsWith("call") || name.startsWith("rst")) {
            while (true && gb.isRunning()) {
                if (checkBreakpointHit()) {
                    break;
                }

                opcode = gb.readMemoryAddress(gb.pc().get());
                name = Opcodes.getOpcode(opcode).getName();

                if (name.startsWith("ret")) {
                    short oldPC = gb.pc().get();
                    cpu.step();
                    short newPC = gb.pc().get();

                    if ((short) (oldPC + 1) != newPC) {
                        break;
                    }
                } else {
                    cpu.step();
                }
            }
        }
    }

    /**
     * Continue the execution of the program until a breakpoint is hit.
     */
    private void continueRunning() {
        cpu.step();

        boolean run = true;
        while (run && gb.isRunning()) {
            run = !checkBreakpointHit();
            if (run) {
                cpu.step();
                if (print) {
                    printCPUInfo();
                }
            }
        }
    }

    /**
     * Examine data at the specified address, or the program counter.
     * 
     * @param in The input to read the address from.
     */
    private void examine(String[] in) {
        if (in.length < 2) {
            gb.printMemoryRegion(gb.pc().get(), gb.pc().get() + 47);
        } else {
            try {
                if (in[1].charAt(0) == '$') {
                    in[1] = in[1].replace("$", "0x");
                } else if (in[1].charAt(0) == '%') {
                    in[1] = in[1].replace("%", "0b");
                }
                int addr = Integer.decode(in[1]);
                addr &= 0xfff0;
                gb.printMemoryRegion(addr, addr + 47);
            } catch (Exception e) {
                System.err.println("Invalid syntax. Usage: `x [ |$|0x|%|0b]{MEM_ADDR}`");
            }
        }
    }

    /**
     * Enable printing after every instruction (for 'continue').
     */
    private void enablePrinting() {
        print = !print;
        System.out.println("Print after every step: " + print);
    }

}
