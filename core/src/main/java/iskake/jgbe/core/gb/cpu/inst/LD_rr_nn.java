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
public class LD_rr_nn implements Instruction {

    @Override
    public void doOp(IGameBoy gb, int opcode) {
        boolean load8 = (opcode & 0b111) == 6;
        Register reg = load8 ? Register.tableByte[(opcode & 0b111000) >> 3] : Register.tableShortSP[(opcode & 0b110000) >> 4];
        if (load8) {
            gb.reg().writeByte(reg, gb.readNextByte());
        } else /*if (Registers.isShortRegister(reg))*/ {
            gb.reg().writeShort(reg, gb.readNextShort());
        }
    }

}
