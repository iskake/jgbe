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

    public Debugger(GameBoy gb, CPU cpu) {
        this.gb = gb;
        this.cpu = cpu;
    }

    /**
     * Run the debugger
     */
    public void run() {
        gb.reg.printRegisters();
        cpu.printNextInstruction();

        Scanner sc = new Scanner(System.in);
        boolean running = true;
        String[] input;
        String[] lastInput = { "c" };
        ArrayList<Integer> breakPoints = new ArrayList<>();
        boolean print = false;

        while (running) {
            System.out.print("> ");
            input = sc.nextLine().split(" ");

            if (input[0].length() == 0) {
                input = lastInput;
            }
            lastInput = input;

            switch (input[0]) {
                case "q":
                    running = false;
                    break;
                case "s":
                    cpu.step();
                    gb.reg.printRegisters();
                    cpu.printNextInstruction();
                    break;
                case "n":
                    byte opcode = gb.readMemoryAddress(gb.getPC());
                    String name = Opcodes.getOpcode(opcode).getName();
                    cpu.step();
                    if (name.startsWith("call") || name.startsWith("rst")) {
                        while (true) {
                            opcode = gb.readMemoryAddress(gb.getPC());
                            name = Opcodes.getOpcode(opcode).getName();
                            if (name.startsWith("ret")) {
                                short oldPC = gb.getPC();
                                cpu.step();
                                short newPC = gb.getPC();

                                if ((short) (oldPC + 1) != newPC) {
                                    break;
                                }
                            } else {
                                cpu.step();
                            }
                        }
                    }
                    gb.reg.printRegisters();
                    cpu.printNextInstruction();
                    break;
                case "c":
                    cpu.step();

                    boolean run = true;
                    while (run) {
                        for (Integer breakPoint : breakPoints) {
                            if (gb.getPC() == Bitwise.toShort(breakPoint)) {
                                System.out.printf("Hit breakpoint at $%04x\n", breakPoint);
                                run = false;
                            }
                        }
                        if (run) {
                            cpu.step();
                            if (print) {
                                gb.reg.printRegisters();
                                cpu.printNextInstruction();
                            }
                        }
                    }
                    gb.reg.printRegisters();
                    cpu.printNextInstruction();
                    break;
                case "b":
                    if (input.length == 1) {
                        System.out.println("Breakpoints: ");
                        int i = 1;
                        for (Integer b : breakPoints) {
                            System.out.printf("%d: %04x\n", i, b);
                            i++;
                        }
                        break;
                    }
                    try {
                        if (input[1].charAt(0) == '$') {
                            input[1] = input[1].replace("$", "0x");
                        } else if (input[1].charAt(0) == '%') {
                            input[1] = input[1].replace("%", "0b");
                        }
                        breakPoints.add(Integer.decode(input[1]));
                        System.out.printf("Set breakpoint at $%04x\n", breakPoints.get(breakPoints.size() - 1));
                    } catch (Exception e) {
                        System.err.println("Invalid syntax. Usage: `b [ |$|0x|%|0b]{MEM_ADDR}`");
                    }
                    break;
                case "d":
                    try {
                        if (input[1].charAt(0) == '$') {
                            input[1] = input[1].replace("$", "0x");
                        } else if (input[1].charAt(0) == '%') {
                            input[1] = input[1].replace("%", "0b");
                        }
                        int addr = Integer.decode(input[1]);
                        addr &= 0xfff0;
                        gb.printMemoryRegion(addr, addr + 47);
                    } catch (Exception e) {
                        System.err.println("Invalid syntax. Usage: `d [ |$|0x|%|0b]{MEM_ADDR}`");
                    }
                    break;
                case "p":
                    print = !print;
                    System.out.println("Print after every step: " + print);
                    break;
                default:
                    System.err.println("Unknown command: " + input[0]);
                    break;
            }
        }
        sc.close();
    }

}
