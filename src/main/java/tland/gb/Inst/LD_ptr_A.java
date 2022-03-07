package tland.gb.Inst;

import tland.Bitwise;
import tland.gb.GameBoy;
import tland.gb.Registers.RegisterIndex;

/**
 * Load register value into byte at address.
 * 
 * <p>
 * Implements opcodes: {@code ld [$n16], a}, {@code ldh [$ff00+n8], a} and
 * {@code ld [c], a}
 */
public class LD_ptr_A extends Instruction {
    private final RegisterIndex reg;

    public LD_ptr_A(String name) {
        super(name);

        this.reg = RegisterIndex.A;
    }

    @Override
    public void doOp(GameBoy gb, int opcode) {
        byte value = gb.reg.readRegisterByte(reg);

        // Because the three implemented opcodes (see javadoc above class) all load the
        // value in the A-register to an address (hence, `LD_ptr_A`), we can use the
        // same class for all three opcodes.
        short address = switch (opcode) {
            // ldh [$n8], a (a.k.a. `ld [$ff00+n8], a`)
            case 0xE0 -> Bitwise.toShort((byte) 0xff, gb.readNextByte());
            // ld [$c], a (a.k.a. `ld [$ff00+c], a`)
            case 0xE2 -> Bitwise.toShort((byte) 0xff, gb.reg.readRegisterByte(RegisterIndex.C));
            // ld [$n16], a
            default -> gb.readNextShort();
        };

        gb.writeMemoryAddress(address, value);
    }

}
