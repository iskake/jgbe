package tland.gb.Inst;

import tland.Bitwise;
import tland.gb.GameBoy;
import tland.gb.Registers.RegisterIndex;

/**
 * Load register value into byte at address.
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
        short address;

        switch (opcode) {
            // ldh [$n8], a (a.k.a. `ld [$ff00+n8], a`)
            case 0xF0:
                address = Bitwise.toShort((byte)0xff, gb.readNextByte());
                break;
            // ld [$c], a (a.k.a. `ld [$ff00+c], a`)
            case 0xF2:
                address = Bitwise.toShort((byte)0xff, gb.reg.readRegisterByte(RegisterIndex.C));
                break;
            // ld [$n16], a
            default:
                address = gb.readNextShort();
                break;
        }

        gb.writeMemoryAddress(address, value);
    }

}
