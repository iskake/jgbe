package tland.gb.Inst;

import tland.Bitwise;
import tland.gb.GameBoy;
import tland.gb.Registers.RegisterIndex;

/**
 * Load register value into byte at address.
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
        int value;

        switch (opcode) {
            // ldh a, [$n8] (a.k.a. `ld a, [$ff00+n8]`)
            case 0xF0:
                value = Bitwise.toShort(0xff, gb.readNextByte());
                break;
            // ld a, [c] (a.k.a. `ld a, [$ff00+c]`)
            case 0xF2:
                value = Bitwise.toShort(0xff, gb.reg.readRegisterByte(RegisterIndex.C));
                break;
            // ld a, [$n16]
            default:
                value = gb.memoryMap.readShort(gb.readNextShort());
                break;
        }

        gb.reg.writeRegisterByte(reg, value);
    }

}
