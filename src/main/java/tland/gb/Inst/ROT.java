package tland.gb.Inst;

import tland.gb.GameBoy;

/**
 * Rotate and shift instructions.
 * 
 * Implements opcodes: 
 */
public class ROT extends Instruction{

    public ROT() {
        super("ROT_INST");
    }

    @Override
    public void doOp(GameBoy gb, int opcode) {
        /*
         * The prefixed rotate/shift opcodes can be indexed like so:
         * 00-3f => Rotate/shift instructions
         *     00-07 => RLC  r8
         *     08-0f => RRC  r8
         *     10-17 => RL   r8
         *     18-1f => RR   r8
         *     20-27 => SLA  r8
         *     28-2f => SRA  r8
         *     30-37 => SWAP r8
         *     38-3f => SRL  r8
         */
        
        // TODO
    }

}
