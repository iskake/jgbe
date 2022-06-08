package iskake.jgbe.core.gb.cpu.inst;

import iskake.jgbe.core.gb.IGameBoy;
import iskake.jgbe.core.gb.Registers.Register;

/**
 * Load byte from register2 into register1.
 * 
 * <p>
 * Implements opcodes: {@code ld r1, r2}, {@code ld [hli], a},
 * {@code ld [hld], a}, {@code ld a, [hli]}, {@code ld a, [hld]} and {@code ld sp, hl}
 */
public final class LD_r8_r8 extends Instruction {
    private final Register r1;
    private final Register r2;

    public LD_r8_r8(String name, Register r1, Register r2) {
        super(name);

        this.r1 = r1;
        this.r2 = r2;
    }

    @Override
    public void doOp(IGameBoy gb, int opcode) {
        gb.reg().writeRegisterByte(r1, gb.reg().readRegisterByte(r2));

        // Handle HLI and HLD opcodes
        if (opcode == 0x22 || opcode == 0x2a) {
            // HLI
            gb.reg().incRegisterShort(Register.HL);
        } else if (opcode == 0x32 || opcode == 0x3a) {
            // HLD
            gb.reg().decRegisterShort(Register.HL);
        }
    }

}
