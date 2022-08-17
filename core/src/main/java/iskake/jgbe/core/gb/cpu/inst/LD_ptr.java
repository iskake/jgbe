package iskake.jgbe.core.gb.cpu.inst;

import iskake.jgbe.core.gb.IGameBoy;
import iskake.jgbe.core.gb.Registers.Register;
import iskake.jgbe.core.Bitwise;

/**
 * Pointer load instructions.
 */
public class LD_ptr {
    private static final int OP_LDH_A_$N16 = 0xF0;
    private static final int OP_LDH_A_$C = 0xF2;

    private static final int OP_LDH_$N16_A = 0xE0;
    private static final int OP_LDH_$C_SP = 0xE2;
    private static final int OP_LD_$N16_SP = 0x08;

    public static void ld_a_ptr(IGameBoy gb, int opcode) {
        // Because the three implemented opcodes (see javadoc above class) all load a
        // value from an address into the A register (hence, `LD_A_ptr`), we can use the
        // same class for all three opcodes.
        short address = switch (opcode) {
            // ldh a, [$n16] (a.k.a. `ld a, [$ff00+n8]`)
            case OP_LDH_A_$N16 -> Bitwise.toShort((byte) 0xff, gb.readNextByte());
            // ldh a, [c] (a.k.a. `ld a, [$ff00+c]`)
            case OP_LDH_A_$C -> Bitwise.toShort((byte) 0xff, gb.reg().readByte(Register.C));
            // ld a, [$n16]
            default -> gb.readNextShort();
        };

        byte value = gb.readAddress(address);
        gb.reg().writeByte(Register.A, value);
    }

    public static void ld_ptr_rr(IGameBoy gb, int opcode) {
        // Because the three implemented opcodes (see javadoc above class) all load the
        // value in the A register to an address (hence, `LD_ptr_A`), we can use the
        // same class for all three opcodes.
        short address = switch (opcode) {
            // ldh [$n16], a (a.k.a. `ld [$ff00+n8], a`)
            case OP_LDH_$N16_A -> Bitwise.toShort((byte) 0xff, gb.readNextByte());
            // ldh [c], a (a.k.a. `ld [$ff00+c], a`)
            case OP_LDH_$C_SP -> Bitwise.toShort((byte) 0xff, gb.reg().readByte(Register.C));
            // ld [$n16], a and ld [$n16], sp
            default -> gb.readNextShort();
        };

        if (opcode == OP_LD_$N16_SP) {
            // ld [$n16], sp
            short sp = gb.sp().get();
            gb.writeAddress(address, Bitwise.getLowByte(sp));
            gb.writeAddress((short)(address + 1), Bitwise.getHighByte(sp));
        } else {
            byte value = gb.reg().readByte(Register.A);
            gb.writeAddress(address, value);
        }
    }

}
