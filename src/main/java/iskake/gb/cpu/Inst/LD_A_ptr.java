package iskake.gb.cpu.Inst;

import iskake.Bitwise;
import iskake.gb.GameBoy;
import iskake.gb.Registers.RegisterIndex;

/**
 * Load byte value at address into register.
 * 
 * <p>
 * Implements opcodes: {@code ld a, [$n16]}, {@code ldh a, [$n16]} and
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

        // Because the three implemented opcodes (see javadoc above class) all load a
        // value from an address into the A register (hence, `LD_A_ptr`), we can use the
        // same class for all three opcodes.
        short address = switch (opcode) {
            // ldh a, [$n16] (a.k.a. `ld a, [$ff00+n8]`)
            case 0xF0 -> Bitwise.toShort((byte) 0xff, gb.readNextByte());
            // ldh a, [c] (a.k.a. `ld a, [$ff00+c]`)
            case 0xF2 -> Bitwise.toShort((byte) 0xff, gb.reg.readRegisterByte(RegisterIndex.C));
            // ld a, [$n16]
            default -> gb.readNextShort();
        };

        value = gb.readMemoryAddress(address);
        gb.reg.writeRegisterByte(reg, value);
    }

}
