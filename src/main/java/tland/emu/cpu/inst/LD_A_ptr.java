package tland.emu.cpu.inst;

import tland.Bitwise;
import tland.emu.IEmulator;
import tland.emu.Registers.RegisterIndex;

/**
 * Load byte value at address into register.
 * 
 * <p>
 * Implements opcodes: {@code ld a, [$n16]}, {@code ldh a, [$n16]} and
 * {@code ldh a, [c]}
 * 
 * @author Tarjei Landøy
 */
public class LD_A_ptr extends Instruction {
    private final RegisterIndex reg;

    public LD_A_ptr(String name) {
        super(name);

        this.reg = RegisterIndex.A;
    }

    @Override
    public void doOp(IEmulator emu, int opcode) {
        byte value;

        // Because the three implemented opcodes (see javadoc above class) all load a
        // value from an address into the A register (hence, `LD_A_ptr`), we can use the
        // same class for all three opcodes.
        short address = switch (opcode) {
            // ldh a, [$n16] (a.k.a. `ld a, [$ff00+n8]`)
            case 0xF0 -> Bitwise.toShort((byte) 0xff, emu.readNextByte());
            // ldh a, [c] (a.k.a. `ld a, [$ff00+c]`)
            case 0xF2 -> Bitwise.toShort((byte) 0xff, emu.reg().readRegisterByte(RegisterIndex.C));
            // ld a, [$n16]
            default -> emu.readNextShort();
        };

        value = emu.readMemoryAddress(address);
        emu.reg().writeRegisterByte(reg, value);
    }

}
