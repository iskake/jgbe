package iskake.jgbe.core.gb.cpu.inst;

import iskake.jgbe.core.gb.IGameBoy;
import iskake.jgbe.core.gb.Registers.Register;
import iskake.jgbe.core.Bitwise;

/**
 * Load register value into byte at address (and the next byte for {@code ld [$n16], sp}).
 * 
 * <p>
 * Implements opcodes: {@code ld [$n16], a}, {@code ldh [$n16], a},
 * {@code ldh [c], a} and {@code ld [$n16], sp})
 */
public class LD_ptr_rr implements Instruction {
    private static final int OP_LDH_$N16_A = 0xE0;
    private static final int OP_LDH_$C_SP = 0xE2;
    private static final int OP_LD_$N16_SP = 0x08;

    @Override
    public void doOp(IGameBoy gb, int opcode) {
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
