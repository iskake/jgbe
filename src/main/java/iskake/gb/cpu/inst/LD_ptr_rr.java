package iskake.gb.cpu.inst;

import iskake.Bitwise;
import iskake.gb.IGameBoy;
import iskake.gb.Registers.RegisterIndex;

/**
 * Load register value into byte at address (and the next byte for {@code ld [$n16], sp}).
 * 
 * <p>
 * Implements opcodes: {@code ld [$n16], a}, {@code ldh [$n16], a},
 * {@code ldh [c], a} and {@code ld [$n16], sp})
 */
public class LD_ptr_rr extends Instruction {
    private final RegisterIndex reg;

    public LD_ptr_rr(String name) {
        super(name);

        this.reg = RegisterIndex.A;
    }

    @Override
    public void doOp(IGameBoy emu, int opcode) {
        // Because the three implemented opcodes (see javadoc above class) all load the
        // value in the A register to an address (hence, `LD_ptr_A`), we can use the
        // same class for all three opcodes.
        short address = switch (opcode) {
            // ldh [$n16], a (a.k.a. `ld [$ff00+n8], a`)
            case 0xE0 -> Bitwise.toShort((byte) 0xff, emu.readNextByte());
            // ldh [c], a (a.k.a. `ld [$ff00+c], a`)
            case 0xE2 -> Bitwise.toShort((byte) 0xff, emu.reg().readRegisterByte(RegisterIndex.C));
            // ld [$n16], a and ld [$n16], sp
            default -> emu.readNextShort();
        };

        if (opcode == 0x08) {
            // ld [$n16], sp
            short sp = emu.sp().get();
            emu.writeMemoryAddress(address, Bitwise.getLowByte(sp));
            emu.writeMemoryAddress((short)(address + 1), Bitwise.getHighByte(sp));
        } else {
            byte value = emu.reg().readRegisterByte(reg);
            emu.writeMemoryAddress(address, value);
        }
    }

}
