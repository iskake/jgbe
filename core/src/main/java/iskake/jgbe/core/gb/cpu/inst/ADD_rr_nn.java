package iskake.jgbe.core.gb.cpu.inst;

import iskake.jgbe.core.gb.IGameBoy;
import iskake.jgbe.core.gb.Registers;
import iskake.jgbe.core.gb.Registers.Flags;
import iskake.jgbe.core.gb.Registers.Register;

/**
 * Addition instruction.
 * 
 * <p>
 * Implements opcodes: {@code add a, r8}, {@code add a, $n8},
 * {@code add hl, r16}, {@code add sp, $e8}, {@code adc r8} and
 * {@code adc $n8}
 */
public class ADD_rr_nn implements Instruction {
    private static final int OP_ADD_SP_E8 = 0xe8;

    @Override
    public void doOp(IGameBoy gb, int opcode) {
        int x = (opcode & 0b1100_0000) >> 6;
        if (opcode == OP_ADD_SP_E8) {
            add_sp_e8(gb, opcode);
        } else if (x >= 2) {
            add_a_r8(gb, opcode);
        } else {
            add_hl_r16(gb, opcode);
        }
    }

    private void add_sp_e8(IGameBoy gb, int opcode) {
        // add sp, $e8
        byte value = gb.readNextByte();
        short regVal = gb.sp().get();
        gb.sp().set((short) (regVal + value));

        gb.reg().resetFlag(Flags.Z);
        gb.reg().resetFlag(Flags.N);
        gb.reg().setFlagIf(Flags.H,
                           ((Short.toUnsignedInt(regVal) & 0b1111) + (Byte.toUnsignedInt(value) & 0b1111)) > 0b1111);
        gb.reg().setFlagIf(Flags.C, (Short.toUnsignedInt(regVal) & 0xff) + Byte.toUnsignedInt(value) > 0xff);
    }

    private void add_a_r8(IGameBoy gb, int opcode) {
        Register r1 = Register.A;
        Register r2 = ((opcode & 0b1100_0000) >> 6) == 3 ? null : Register.tableByte[(opcode & 0b111)];
        byte value = r2 == null ? gb.readNextByte() : gb.reg().readByte(r2);

        byte c = 0;
        boolean carry = (opcode & 0b111000) == 0b1000;
        if (carry) {
            // adc rr, nn
            c += gb.reg().isFlagSet(Flags.C) ? 1 : 0;
        }

        byte regVal = gb.reg().readByte(r1);

        gb.reg().writeByte(r1, regVal + value + c);

        gb.reg().setFlagIf(Flags.Z, (byte) (regVal + value + c) == 0);
        gb.reg().resetFlag(Flags.N);
        gb.reg().setFlagIf(Flags.H,
                           (((Byte.toUnsignedInt(regVal) & 0b1111) + (Byte.toUnsignedInt(value) & 0b1111) + c) > 0b1111));

        gb.reg().setFlagIf(Flags.C, ((Byte.toUnsignedInt(regVal) + Byte.toUnsignedInt(value) + c) > 0xff));
    }
    
    private void add_hl_r16(IGameBoy gb, int opcode) {
        Register r1 = Register.HL;
        Register r2 = Register.tableShortSP[(opcode & 0b110000) >> 4];
        short r1Val = gb.reg().readShort(r1);
        short r2Val = gb.reg().readShort(r2);

        gb.reg().writeShort(r1, r1Val + r2Val);
        gb.reg().resetFlag(Flags.N);
        gb.reg().setFlagIf(Flags.H,
                           ((Short.toUnsignedInt(r1Val) & 0xfff) + (Short.toUnsignedInt(r2Val) & 0xfff) > 0xfff));
        gb.reg().setFlagIf(Flags.C, Short.toUnsignedInt(r1Val) + Short.toUnsignedInt(r2Val) > 0xffff);
    }

}
