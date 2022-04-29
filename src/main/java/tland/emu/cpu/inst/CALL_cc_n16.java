package tland.emu.cpu.inst;

import tland.emu.IEmulator;

/**
 * Call the specified address.
 * 
 * <p>
 * Implements opcodes: {@code call $n16}, {@code call nz, $n16},
 * {@code call nc, $n16}, {@code call z, $n16} and {@code call c, $n16}
 * 
 * @author Tarjei Landøy
 */
public class CALL_cc_n16 extends Instruction {
    private final Conditions condition;

    public CALL_cc_n16(String name, Conditions condition) {
        super(name);
        this.condition = condition;
    }

    @Override
    public void doOp(IEmulator emu, int opcode) {
        short address = emu.readNextShort();

        if (Conditions.conditionSatisfied(emu.reg(), condition)) {
            emu.sp().push(emu.pc().get());
            emu.pc().set(address);
        }
    }

}
