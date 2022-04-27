package tland.gb.cpu.Inst;

import java.util.ArrayList;
import java.util.List;

import tland.gb.IGameBoy;
import tland.gb.Registers.RegisterIndex;

public class PRINT extends Instruction {
    public PRINT(String name) {
        super(name);
    }

    @Override
    public void doOp(IGameBoy gb, int opcode) {
        System.out.println(parseString(gb));
    }

    /**
     * Parse the string in memory, according to the print instruction format.
     * 
     * @param gb The IGameBoy object to read memory from.
     * @return The parsed string.
     */
    public String parseString(IGameBoy gb) {
        /*
         * The prt instruction format is as follows:
         *     byte 0: opcode
         *     byte 1: length
         *     byte 2..k: string
         *     byte k+1: args length
         *     byte k+2..n: args
         */
        List<Character> buf = new ArrayList<>();
        byte length = gb.readNextByte();
        for (int j = 0; j < length; j++) {
            buf.add((char) gb.readNextByte());
        }

        StringBuilder sb = new StringBuilder();
        for (char c : buf) {
            sb.append(c);
        }

        int argLen = Byte.toUnsignedInt(gb.readNextByte());
        Object[] stringArgs = null;
        if (argLen > 0) {
            stringArgs = new Object[argLen];
            for (int j = 0; j < argLen; j++) {
                switch (Byte.toUnsignedInt(gb.readNextByte())) {
                    case 0x00 -> stringArgs[j] = gb.reg().readRegisterByte(RegisterIndex.A);
                    case 0x01 -> stringArgs[j] = gb.reg().readRegisterByte(RegisterIndex.B);
                    case 0x02 -> stringArgs[j] = gb.reg().readRegisterByte(RegisterIndex.C);
                    case 0x03 -> stringArgs[j] = gb.reg().readRegisterByte(RegisterIndex.D);
                    case 0x04 -> stringArgs[j] = gb.reg().readRegisterByte(RegisterIndex.E);
                    case 0x05 -> stringArgs[j] = gb.reg().readRegisterByte(RegisterIndex.H);
                    case 0x06 -> stringArgs[j] = gb.reg().readRegisterByte(RegisterIndex.L);
                    case 0x07 -> stringArgs[j] = gb.reg().readRegisterShort(RegisterIndex.AF);
                    case 0x08 -> stringArgs[j] = gb.reg().readRegisterShort(RegisterIndex.BC);
                    case 0x09 -> stringArgs[j] = gb.reg().readRegisterShort(RegisterIndex.DE);
                    case 0x0a -> stringArgs[j] = gb.reg().readRegisterShort(RegisterIndex.HL);
                    case 0x0b -> stringArgs[j] = gb.reg().readRegisterShort(RegisterIndex.SP);
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

    /**
     * Get the fixed name of the instruction.
     * 
     * @param gb
     * @return
     */
    public String getFixedName(IGameBoy gb) {
        return "prt " + "__TODO__"; // + parseString(gb);
    }

}
