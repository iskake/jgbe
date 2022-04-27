package tland.emu.cpu.Inst;

import tland.Bitwise;
import tland.emu.IEmulator;

/**
 * Call the address {@code vec}.
 * 
 * <p>
 * Implements opcodes: {@code RST $00}, {@code RST $08}, {@code RST $10},
 * {@code RST $18}, {@code RST $20}, {@code RST $28}, {@code RST $30}, and
 * {@code RST $38}
 */
public class RST_vec extends Instruction {

    public RST_vec(String name) {
        super(name);
    }

    @Override
    public void doOp(IEmulator emu, int opcode) {
        short vector = Bitwise.toShort(opcode & 0b111000);

        emu.sp().push(emu.pc().get());
        emu.pc().set(vector);
    }

}
