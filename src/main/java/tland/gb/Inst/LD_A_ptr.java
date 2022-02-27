package tland.gb.Inst;

import tland.Bitwise;
import tland.gb.GameBoy;
import tland.gb.Registers.RegisterIndex;

/**
 * Load byte value at address into register.
 * Implements opcodes: {@code ld a, [$n16]}, {@code ldh a, [$ff00+n8]} and
 * {@code ldh a, [c]}
 */
public class LD_A_ptr extends Instruction {
    private final RegisterIndex reg;

    public LD_A_ptr(String name) {
        super(name);

        this.reg = RegisterIndex.A;
    }

    @Override
    public void doOp(GameBoy gb, int opcode) {
        byte value;
        short address;

        switch (opcode) {
            // ldh a, [$n8] (a.k.a. `ld a, [$ff00+n8]`)
            case 0xF0:
                address = Bitwise.toShort((byte)0xff, gb.readNextByte());
                break;
            // ld a, [c] (a.k.a. `ld a, [$ff00+c]`)
            case 0xF2:
                address = Bitwise.toShort((byte)0xff, gb.reg.readRegisterByte(RegisterIndex.C));
                break;
            // ld a, [$n16]
            default:
                address = gb.readNextShort();
                break;
        }

        value = gb.readMemoryAddress(address);
        gb.reg.writeRegisterByte(reg, value);
    }

}
