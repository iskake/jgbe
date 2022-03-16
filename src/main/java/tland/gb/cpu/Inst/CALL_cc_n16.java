package tland.gb.cpu.Inst;

import tland.gb.GameBoy;

/**
 * Call the specified address.
 * 
 * <p>
 * Implements opcodes: {@code call $n16}, {@code call nz, $n16},
 * {@code call nc, $n16}, {@code call z, $n16} and {@code call c, $n16}
 */
public class CALL_cc_n16 extends Instruction {
    private final Conditions condition;

    public CALL_cc_n16(String name, Conditions condition) {
        super(name);
        this.condition = condition;
    }

    @Override
    public void doOp(GameBoy gb, int opcode) {
        short address = gb.readNextShort();

        if (Conditions.conditionSatisfied(gb.reg, condition)) {
            gb.pushSP(gb.getPC());
            gb.setPC(address);
        }
    }

}