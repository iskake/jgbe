package iskake.gb.cpu.inst;

import iskake.gb.IGameBoy;
import iskake.gb.Registers.*;

/**
 * Load byte from register2 into register1.
 * 
 * <p>
 * Implements opcodes: {@code ld r1, r2}, {@code ld [hli], a},
 * {@code ld [hld], a}, {@code ld a, [hli]}, {@code ld a, [hld]} and {@code ld sp, hl}
 */
public final class LD_r8_r8 extends Instruction {
    private final RegisterIndex r1;
    private final RegisterIndex r2;

    public LD_r8_r8(String name, RegisterIndex r1, RegisterIndex r2) {
        super(name);

        this.r1 = r1;
        this.r2 = r2;
    }

    @Override
    public void doOp(IGameBoy emu, int opcode) {
        emu.reg().writeRegisterByte(r1, emu.reg().readRegisterByte(r2));

        // Handle HLI and HLD opcodes
        if (opcode == 0x22 || opcode == 0x2a) {
            // HLI
            emu.reg().incRegisterShort(RegisterIndex.HL);
        } else if (opcode == 0x32 || opcode == 0x3a) {
            // HLD
            emu.reg().decRegisterShort(RegisterIndex.HL);
        }
    }

}
