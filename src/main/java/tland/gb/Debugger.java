package tland.gb;

import java.util.ArrayList;
import java.util.Scanner;

import tland.Bitwise;
import tland.gb.cpu.CPU;
import tland.gb.cpu.Opcodes;

/**
 * Simple CLI Game Boy debugger.
 */
public class Debugger {

    private final GameBoy gb;
    private final CPU cpu;

    private Scanner sc;
    private String[] input;
    private String[] lastInput = { "c" };
    private ArrayList<Integer> breakPoints;
    private boolean print;

    public Debugger(GameBoy gb, CPU cpu) {
        this.gb = gb;
        this.cpu = cpu;

        sc = new Scanner(System.in);
        breakPoints = new ArrayList<>();
        print = false;
    }

    /**
     * Step the debugger. This method should be invoked in the GameBoy.run() method.
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
            case "q", "quit", "exit" -> gb.stopRunning();
            case "c", "continue" -> continueRunning();
            case "s", "step" -> stepInto();
            case "n", "next" -> stepOver();
            case "b", "break" -> handleBreakpoints(input);
            case "d", "delete" -> handleBreakpointDeletion(input);
            case "x" -> examine(input);
            case "p", "print" -> enablePrinting();
            case "reg" -> printCPUInfo();
            default -> System.err.println("Unknown command: " + input[0]);
        }
    }

    /**
     * Print information about the CPU and registers.
     */
    private void printCPUInfo() {
        gb.reg().printRegisters();
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
            while (true) {
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
        while (run) {
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
     * Examine data at the specified address.
     * 
     * @param in The input to read the address from.
     */
    private void examine(String[] in) {
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

    /**
     * Enable printing after every instruction (for 'continue').
     */
    private void enablePrinting() {
        print = !print;
        System.out.println("Print after every step: " + print);
    }

}
