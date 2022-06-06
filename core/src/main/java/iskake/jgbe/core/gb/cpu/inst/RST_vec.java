package iskake.jgbe.core.gb.cpu.inst;

import iskake.jgbe.core.gb.IGameBoy;
import iskake.jgbe.core.Bitwise;

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
    public void doOp(IGameBoy gb, int opcode) {
        short vector = Bitwise.toShort(opcode & 0b111000);

        gb.sp().push(gb.pc().get());
        gb.pc().set(vector);
    }

}
