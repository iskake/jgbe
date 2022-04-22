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
        List<Character> buf = new ArrayList<>();
        char c;
        int i = 0;
        do {
            c = (char)gb.readNextByte();
            buf.add(c);
            i++;
        } while (c != (char)0 && i < 0x100);

        StringBuilder sb = new StringBuilder();
        for (char ch : buf) {
            sb.append(ch);
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
                    default -> throw new IllegalInstructionException("Invalid print format.");
                }
            }
        }

        if (stringArgs == null) {
            System.out.println(sb.toString());
        } else {
            System.out.printf(sb.toString(), stringArgs);
        }
    }
    
}
