package iskake.jgbe.core.gb.cpu.inst;

import iskake.jgbe.core.gb.IGameBoy;
import iskake.jgbe.core.gb.Registers;
import iskake.jgbe.core.gb.Registers.Register;

/**
 * Load byte/short into register / address pointed to by register (with [HL]).
 * 
 * <p>
 * Implements opcodes: {@code ld r8, $n8}, {@code ld r16, $n16} and {@code ld [hl], $n8}
 */
public class LD_rr_nn extends Instruction {
    private static final int OP_LD_$HL_N8 = 0x36;

    private final Register reg;

    public LD_rr_nn(String name, Register reg) {
        super(name);
        this.reg = reg;
    }

    @Override
    public void doOp(IGameBoy gb, int opcode) {
        // 0x36 -> ld [hl], $n8
        if (Registers.isByteRegister(reg) || opcode == OP_LD_$HL_N8) {
            gb.reg().writeByte(reg, gb.readNextByte());
        } else if (Registers.isShortRegister(reg)) {
            gb.reg().writeShort(reg, gb.readNextShort());
        }
    }

}
