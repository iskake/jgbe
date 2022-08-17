package iskake.jgbe.core.gb.cpu.inst;

import iskake.jgbe.core.gb.IGameBoy;
import iskake.jgbe.core.gb.Registers.Register;
import iskake.jgbe.core.Bitwise;

/**
 * Load byte value at address into register.
 * 
 * <p>
 * Implements opcodes: {@code ld a, [$n16]}, {@code ldh a, [$n16]} and
 * {@code ldh a, [c]}
 */
public class LD_A_ptr implements Instruction {
    private static final int OP_LDH_A_$N16 = 0xF0;
    private static final int OP_LDH_A_$C = 0xF2;

    @Override
    public void doOp(IGameBoy gb, int opcode) {
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

}
