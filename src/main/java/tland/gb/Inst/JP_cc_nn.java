package tland.gb.Inst;

import tland.gb.GameBoy;

/**
 * Jump to address (set PC), with optional condition.
 * 
 * <p>
 * Implements opcodes: {@code jp $n16}, {@code jp nz, $n16},
 * {@code jp nc, $n16}, {@code jp z, $n16}, {@code jp c, $n16} and
 * {@code jp hl}
 */
public class JP_cc_nn extends Instruction {

    public JP_cc_nn(String name) {
        super(name);
    }

    @Override
    public void doOp(GameBoy gb, int opcode) {
        switch (opcode) {
            case 0xc3:
                short address = gb.readNextShort();
                gb.setPC(address);
                break;
            default:
                // TODO
                throw new IllegalInstructionException();
        }
    }

}
