package tland.emu.cpu.inst;

import java.util.ArrayList;
import java.util.List;

import tland.emu.IEmulator;
import tland.emu.Registers.RegisterIndex;

public class PRINT extends Instruction {
    public PRINT(String name) {
        super(name);
    }

    @Override
    public void doOp(IEmulator emu, int opcode) {
        System.out.println(parseString(emu));
    }

    /**
     * Parse the string in memory, according to the print instruction format.
     * 
     * @param emu The IEmulator object to read memory from.
     * @return The parsed string.
     */
    public String parseString(IEmulator emu) {
        /*
         * The prt instruction format is as follows:
         *     byte 0: opcode
         *     byte 1: length
         *     byte 2..k: string
         *     byte k+1: args length
         *     byte k+2..n: args
         */
        List<Character> buf = new ArrayList<>();
        byte length = emu.readNextByte();
        for (int j = 0; j < length; j++) {
            buf.add((char) emu.readNextByte());
        }

        StringBuilder sb = new StringBuilder();
        for (char c : buf) {
            sb.append(c);
        }

        int argLen = Byte.toUnsignedInt(emu.readNextByte());
        Object[] stringArgs = null;
        if (argLen > 0) {
            stringArgs = new Object[argLen];
            for (int j = 0; j < argLen; j++) {
                switch (Byte.toUnsignedInt(emu.readNextByte())) {
                    case 0x00 -> stringArgs[j] = emu.reg().readRegisterByte(RegisterIndex.A);
                    case 0x01 -> stringArgs[j] = emu.reg().readRegisterByte(RegisterIndex.B);
                    case 0x02 -> stringArgs[j] = emu.reg().readRegisterByte(RegisterIndex.C);
                    case 0x03 -> stringArgs[j] = emu.reg().readRegisterByte(RegisterIndex.D);
                    case 0x04 -> stringArgs[j] = emu.reg().readRegisterByte(RegisterIndex.E);
                    case 0x05 -> stringArgs[j] = emu.reg().readRegisterByte(RegisterIndex.H);
                    case 0x06 -> stringArgs[j] = emu.reg().readRegisterByte(RegisterIndex.L);
                    case 0x07 -> stringArgs[j] = emu.reg().readRegisterShort(RegisterIndex.AF);
                    case 0x08 -> stringArgs[j] = emu.reg().readRegisterShort(RegisterIndex.BC);
                    case 0x09 -> stringArgs[j] = emu.reg().readRegisterShort(RegisterIndex.DE);
                    case 0x0a -> stringArgs[j] = emu.reg().readRegisterShort(RegisterIndex.HL);
                    case 0x0b -> stringArgs[j] = emu.reg().readRegisterShort(RegisterIndex.SP);
                    /* case 0x0c -> {
                        int strLength = Byte.toUnsignedInt(gb.readNextByte());
                        stringArgs[j] = StringHelpers.stringParse(gb, strLength, gb.pc().get());
                    } */
                    default -> throw new IllegalInstructionException("Invalid print format.");
                }
            }
        }

        if (stringArgs == null) {
            return sb.toString();
        } else {
            return sb.toString().formatted(stringArgs);
        }
    }

}
