package iskake.jgbe.core.gb.cpu;

import iskake.jgbe.core.gb.IGameBoy;
import iskake.jgbe.core.gb.Registers.Register;
import iskake.jgbe.core.gb.cpu.inst.*;
import iskake.jgbe.core.Bitwise;

/**
 * Class for accessing opcodes and their names.
 */
public class Opcodes {
    /** 
     * Array containing every (non-prefixed) opcode, with each index of the array
     * being the corresponding opcode.
     */
    private final static Instruction[] opcodes = {
/* 0x00 */ new NOP(),

/* 0x01 */ new LD_rr_nn("ld bc, $_N16"),

/* 0x02 */ new LD_r8_r8("ld [bc], a"),

/* 0x03 */ new INC_rr("inc bc"),
/* 0x04 */ new INC_rr("inc b"),
/* 0x05 */ new DEC_rr("dec b"),

/* 0x06 */ new LD_rr_nn("ld b, $_N8"),

/* 0x07 */ new ROT("rlca"),

/* 0x08 */ new LD_ptr_rr("ld [$_N16], sp"),

/* 0x09 */ new ADD_rr_nn("add hl, bc"),

/* 0x0a */ new LD_r8_r8("ld a, [bc]"),

/* 0x0b */ new DEC_rr("dec bc"),
/* 0x0c */ new INC_rr("inc c"),
/* 0x0d */ new DEC_rr("dec c"),

/* 0x0e */ new LD_rr_nn("ld c, $_N8"),

/* 0x0f */ new ROT("rrca"),

/* 0x10 */ new Halt("stop"),

/* 0x11 */ new LD_rr_nn("ld de, $_N16"),

/* 0x12 */ new LD_r8_r8("ld [de], a"),

/* 0x13 */ new INC_rr("inc de"),
/* 0x14 */ new INC_rr("inc d"),
/* 0x15 */ new DEC_rr("dec d"),

/* 0x16 */ new LD_rr_nn("ld d, $_N8"),

/* 0x17 */ new ROT("rla"),

/* 0x18 */ new JR_cc_e8("jr $_N8"),

/* 0x19 */ new ADD_rr_nn("add hl, de"),

/* 0x1a */ new LD_r8_r8("ld a, [de]"),

/* 0x1b */ new DEC_rr("dec de"),
/* 0x1c */ new INC_rr("inc e"),
/* 0x1d */ new DEC_rr("dec e"),

/* 0x1e */ new LD_rr_nn("ld e, $_N8"),

/* 0x1f */ new ROT("rra"),

/* 0x20 */ new JR_cc_e8("jr nz, $_N8"),

/* 0x21 */ new LD_rr_nn("ld hl, $_N16"),

/* 0x22 */ new LD_r8_r8("ld [hli], a"),

/* 0x23 */ new INC_rr("inc hl"),
/* 0x24 */ new INC_rr("inc h"),
/* 0x25 */ new DEC_rr("dec h"),

/* 0x26 */ new LD_rr_nn("ld h, $_N8"),

/* 0x27 */ new DAA(),

/* 0x28 */ new JR_cc_e8("jr z, $_N8"),

/* 0x29 */ new ADD_rr_nn("add hl, hl"),

/* 0x2a */ new LD_r8_r8("ld a, [hli]"),

/* 0x2b */ new DEC_rr("dec hl"),
/* 0x2c */ new INC_rr("inc l"),
/* 0x2d */ new DEC_rr("dec l"),

/* 0x2e */ new LD_rr_nn("ld l, $_N8"),

/* 0x2f */ new CPL(),

/* 0x30 */ new JR_cc_e8("jr nc, $_N8"),

/* 0x31 */ new LD_rr_nn("ld sp, $_N16"),

/* 0x32 */ new LD_r8_r8("ld [hld], a"),

/* 0x33 */ new INC_rr("inc sp"),
/* 0x34 */ new INC_rr("inc [hl]"),
/* 0x35 */ new DEC_rr("dec [hl]"),

/* 0x36 */ new LD_rr_nn("ld [hl], $_N8"),

/* 0x37 */ new SCF(),

/* 0x38 */ new JR_cc_e8("jr c, $_N8"),

/* 0x39 */ new ADD_rr_nn("add hl, sp"),

/* 0x3a */ new LD_r8_r8("ld a, [hld]"),

/* 0x3b */ new DEC_rr("dec sp"),
/* 0x3c */ new INC_rr("inc a"),
/* 0x3d */ new DEC_rr("dec a"),

/* 0x3e */ new LD_rr_nn("ld a, $_N8"),

/* 0x3f */ new CCF(),

/* 0x40 */ new LD_r8_r8("ld b, b"),
/* 0x41 */ new LD_r8_r8("ld b, c"),
/* 0x42 */ new LD_r8_r8("ld b, d"),
/* 0x43 */ new LD_r8_r8("ld b, e"),
/* 0x44 */ new LD_r8_r8("ld b, h"),
/* 0x45 */ new LD_r8_r8("ld b, l"),
/* 0x46 */ new LD_r8_r8("ld b, [hl]"),
/* 0x47 */ new LD_r8_r8("ld b, a"),

/* 0x48 */ new LD_r8_r8("ld c, b"),
/* 0x49 */ new LD_r8_r8("ld c, c"),
/* 0x4a */ new LD_r8_r8("ld c, d"),
/* 0x4b */ new LD_r8_r8("ld c, e"),
/* 0x4c */ new LD_r8_r8("ld c, h"),
/* 0x4d */ new LD_r8_r8("ld c, l"),
/* 0x4e */ new LD_r8_r8("ld c, [hl]"),
/* 0x4f */ new LD_r8_r8("ld c, a"),

/* 0x50 */ new LD_r8_r8("ld d, b"),
/* 0x51 */ new LD_r8_r8("ld d, c"),
/* 0x52 */ new LD_r8_r8("ld d, d"),
/* 0x53 */ new LD_r8_r8("ld d, e"),
/* 0x54 */ new LD_r8_r8("ld d, h"),
/* 0x55 */ new LD_r8_r8("ld d, l"),
/* 0x56 */ new LD_r8_r8("ld d, [hl]"),
/* 0x57 */ new LD_r8_r8("ld d, a"),

/* 0x58 */ new LD_r8_r8("ld e, b"),
/* 0x59 */ new LD_r8_r8("ld e, c"),
/* 0x5a */ new LD_r8_r8("ld e, d"),
/* 0x5b */ new LD_r8_r8("ld e, e"),
/* 0x5c */ new LD_r8_r8("ld e, h"),
/* 0x5d */ new LD_r8_r8("ld e, l"),
/* 0x5e */ new LD_r8_r8("ld e, [hl]"),
/* 0x5f */ new LD_r8_r8("ld e, a"),

/* 0x60 */ new LD_r8_r8("ld h, b"),
/* 0x61 */ new LD_r8_r8("ld h, c"),
/* 0x62 */ new LD_r8_r8("ld h, d"),
/* 0x63 */ new LD_r8_r8("ld h, e"),
/* 0x64 */ new LD_r8_r8("ld h, h"),
/* 0x65 */ new LD_r8_r8("ld h, l"),
/* 0x66 */ new LD_r8_r8("ld h, [hl]"),
/* 0x67 */ new LD_r8_r8("ld h, a"),

/* 0x68 */ new LD_r8_r8("ld l, b"),
/* 0x69 */ new LD_r8_r8("ld l, c"),
/* 0x6a */ new LD_r8_r8("ld l, d"),
/* 0x6b */ new LD_r8_r8("ld l, e"),
/* 0x6c */ new LD_r8_r8("ld l, h"),
/* 0x6d */ new LD_r8_r8("ld l, l"),
/* 0x6e */ new LD_r8_r8("ld l, [hl]"),
/* 0x6f */ new LD_r8_r8("ld l, a"),

/* 0x70 */ new LD_r8_r8("ld [hl], b"),
/* 0x71 */ new LD_r8_r8("ld [hl], c"),
/* 0x72 */ new LD_r8_r8("ld [hl], d"),
/* 0x73 */ new LD_r8_r8("ld [hl], e"),
/* 0x74 */ new LD_r8_r8("ld [hl], h"),
/* 0x75 */ new LD_r8_r8("ld [hl], l"),

/* 0x76 */ new Halt("halt"),

/* 0x77 */ new LD_r8_r8("ld [hl], a"),

/* 0x78 */ new LD_r8_r8("ld a, b"),
/* 0x79 */ new LD_r8_r8("ld a, c"),
/* 0x7a */ new LD_r8_r8("ld a, d"),
/* 0x7b */ new LD_r8_r8("ld a, e"),
/* 0x7c */ new LD_r8_r8("ld a, h"),
/* 0x7d */ new LD_r8_r8("ld a, l"),
/* 0x7e */ new LD_r8_r8("ld a, [hl]"),
/* 0x7f */ new LD_r8_r8("ld a, a"),

/* 0x80 */ new ADD_rr_nn("add a, b"),
/* 0x81 */ new ADD_rr_nn("add a, c"),
/* 0x82 */ new ADD_rr_nn("add a, d"),
/* 0x83 */ new ADD_rr_nn("add a, e"),
/* 0x84 */ new ADD_rr_nn("add a, h"),
/* 0x85 */ new ADD_rr_nn("add a, l"),
/* 0x86 */ new ADD_rr_nn("add a, [hl]"),
/* 0x87 */ new ADD_rr_nn("add a, a"),

/* 0x88 */ new ADD_rr_nn("adc b"),
/* 0x89 */ new ADD_rr_nn("adc c"),
/* 0x8a */ new ADD_rr_nn("adc d"),
/* 0x8b */ new ADD_rr_nn("adc e"),
/* 0x8c */ new ADD_rr_nn("adc h"),
/* 0x8d */ new ADD_rr_nn("adc l"),
/* 0x8e */ new ADD_rr_nn("adc [hl]"),
/* 0x8f */ new ADD_rr_nn("adc a"),

/* 0x90 */ new SUB_rr_nn("sub b"),
/* 0x91 */ new SUB_rr_nn("sub c"),
/* 0x92 */ new SUB_rr_nn("sub d"),
/* 0x93 */ new SUB_rr_nn("sub e"),
/* 0x94 */ new SUB_rr_nn("sub h"),
/* 0x95 */ new SUB_rr_nn("sub l"),
/* 0x96 */ new SUB_rr_nn("sub [hl]"),
/* 0x97 */ new SUB_rr_nn("sub a"),

/* 0x98 */ new SUB_rr_nn("sbc b"),
/* 0x99 */ new SUB_rr_nn("sbc c"),
/* 0x9a */ new SUB_rr_nn("sbc d"),
/* 0x9b */ new SUB_rr_nn("sbc e"),
/* 0x9c */ new SUB_rr_nn("sbc h"),
/* 0x9d */ new SUB_rr_nn("sbc l"),
/* 0x9e */ new SUB_rr_nn("sbc [hl]"),
/* 0x9f */ new SUB_rr_nn("sbc a"),

/* 0xa0 */ new AND_nn("and b"),
/* 0xa1 */ new AND_nn("and c"),
/* 0xa2 */ new AND_nn("and d"),
/* 0xa3 */ new AND_nn("and e"),
/* 0xa4 */ new AND_nn("and h"),
/* 0xa5 */ new AND_nn("and l"),
/* 0xa6 */ new AND_nn("and [hl]"),
/* 0xa7 */ new AND_nn("and a"),

/* 0xa8 */ new XOR_nn("xor b"),
/* 0xa9 */ new XOR_nn("xor c"),
/* 0xaa */ new XOR_nn("xor d"),
/* 0xab */ new XOR_nn("xor e"),
/* 0xac */ new XOR_nn("xor h"),
/* 0xad */ new XOR_nn("xor l"),
/* 0xae */ new XOR_nn("xor [hl]"),
/* 0xaf */ new XOR_nn("xor a"),

/* 0xb0 */ new OR_nn("or b"),
/* 0xb1 */ new OR_nn("or c"),
/* 0xb2 */ new OR_nn("or d"),
/* 0xb3 */ new OR_nn("or e"),
/* 0xb4 */ new OR_nn("or h"),
/* 0xb5 */ new OR_nn("or l"),
/* 0xb6 */ new OR_nn("or [hl]"),
/* 0xb7 */ new OR_nn("or a"),

/* 0xb8 */ new CP_nn("cp b"),
/* 0xb9 */ new CP_nn("cp c"),
/* 0xba */ new CP_nn("cp d"),
/* 0xbb */ new CP_nn("cp e"),
/* 0xbc */ new CP_nn("cp h"),
/* 0xbd */ new CP_nn("cp l"),
/* 0xbe */ new CP_nn("cp [hl]"),
/* 0xbf */ new CP_nn("cp a"),

/* 0xc0 */ new RET_cc("ret nz"),

/* 0xc1 */ new POP_r16("pop bc"),

/* 0xc2 */ new JP_cc_nn("jp nz, $_N16"),
/* 0xc3 */ new JP_cc_nn("jp $_N16"),
/* 0xc4 */ new CALL_cc_n16("call nz, $_N16"),

/* 0xc5 */ new PUSH_r16("push bc"),

/* 0xc6 */ new ADD_rr_nn("add a, $_N8"),

/* 0xc7 */ new RST_vec("rst $00"),
/* 0xc8 */ new RET_cc("ret z"),
/* 0xc9 */ new RET_cc("ret"),

/* 0xca */ new JP_cc_nn("jp z, $_N16"),

/* 0xcb */ new Prefixed(),

/* 0xcc */ new CALL_cc_n16("call z, $_N16"),
/* 0xcd */ new CALL_cc_n16("call $_N16"),

/* 0xce */ new ADD_rr_nn("adc $_N8"),

/* 0xcf */ new RST_vec("rst $08"),
/* 0xd0 */ new RET_cc("ret nc"),

/* 0xd1 */ new POP_r16("pop de"),

/* 0xd2 */ new JP_cc_nn("jp nc, $_N16"),

/* 0xd3 */ new IllegalInst("illegal $d3"), // illegal

/* 0xd4 */ new CALL_cc_n16("call nc, $_N16"),

/* 0xd5 */ new PUSH_r16("push de"),

/* 0xd6 */ new SUB_rr_nn("sub $_N8"),

/* 0xd7 */ new RST_vec("rst $10"),
/* 0xd8 */ new RET_cc("ret c"),
/* 0xd9 */ new RET_cc("reti"),
/* 0xda */ new JP_cc_nn("jp c, $_N16"),

/* 0xdb */ new IllegalInst("illegal $db"), // illegal

/* 0xdc */ new CALL_cc_n16("call c, $_N16"),

/* 0xdd */ new IllegalInst("illegal $dd"), // illegal

/* 0xde */ new SUB_rr_nn("sbc $_N8"),

/* 0xdf */ new RST_vec("rst $18"),

/* 0xe0 */ new LD_ptr_rr("ldh [$ff_N8], a"),

/* 0xe1 */ new POP_r16("pop hl"),

/* 0xe2 */ new LD_ptr_rr("ldh [c], a"),

/* 0xe3 */ new IllegalInst("illegal $e3"), // illegal
/* 0xe4 */ new IllegalInst("illegal $e4"), // illegal

/* 0xe5 */ new PUSH_r16("push hl"),

/* 0xe6 */ new AND_nn("and $_N8"),

/* 0xe7 */ new RST_vec("rst $20"),

/* 0xe8 */ new ADD_rr_nn("add sp, $_N8"),

/* 0xe9 */ new JP_cc_nn("jp hl"),

/* 0xea */ new LD_ptr_rr("ld [$_N16], a"),

/* 0xeb */ new IllegalInst("illegal $eb"), // illegal
/* 0xec */ new IllegalInst("illegal $ec"), // illegal
/* 0xed */ new IllegalInst("illegal $ed"), // illegal

/* 0xee */ new XOR_nn("xor $_N8"),

/* 0xef */ new RST_vec("rst $28"),

/* 0xf0 */ new LD_A_ptr("ldh a, [$ff_N8]"),

/* 0xf1 */ new POP_r16("pop af"),

/* 0xf2 */ new LD_A_ptr("ldh a, [c]"),

/* 0xf3 */ new Interrupt("di"),

/* 0xf4 */ new IllegalInst("illegal $f4"), // illegal

/* 0xf5 */ new PUSH_r16("push af"),

/* 0xf6 */ new OR_nn("or $_N8"),

/* 0xf7 */ new RST_vec("rst $30"),

/* 0xf8 */ new LD_SP("ld hl, sp + $_N8"),
/* 0xf9 */ new LD_SP("ld sp, hl"),
/* 0xfa */ new LD_A_ptr("ld a, [$_N16]"),

/* 0xfb */ new Interrupt("ei"),

/* 0xfc */ new IllegalInst("illegal $fc"), // illegal
/* 0xfd */ new IllegalInst("illegal $fd"), // illegal

/* 0xfe */ new CP_nn("cp $_N8"),

/* 0xff */ new RST_vec("rst $38"),
    };

    /**
     * Get the opcode with the corresponding value.
     * 
     * @param index The index (value) of the opcode in the table.
     * @return The opcode at the index.
     */
    public static Instruction getOpcode(byte index) {
        int i = Byte.toUnsignedInt(index);
        return opcodes[i];
    }

    /**
     * Get the opcode with the corresponding value.
     * 
     * @param index The index (value) of the opcode in the table.
     * @return The opcode at the index.
     */
    public static Instruction getOpcode(int index) {
        return opcodes[index];
    }

    /**
     * Get the name of the instruction based on the opcode at the specified memory
     * address.
     * 
     * @param address The address of the instruction.
     * @return The name of the instruction.
     */
    public static String getInstructionName(IGameBoy gb, short address) {
        byte opcode = gb.readAddress(address);

        String opcodeName;

        Instruction inst = Opcodes.getOpcode(opcode);

        if (inst instanceof Prefixed o) {
            opcode = gb.readAddress(Bitwise.toShort(address + 1));
            opcodeName = o.getPrefixedName(Byte.toUnsignedInt(opcode));
        } else {
            opcodeName = Opcodes.getOpcode(opcode).getName();
        }

        opcodeName = opcodeName.replace("_N8", "%02x");
        opcodeName = opcodeName.replace("_N16", "%2$02x%1$02x");

        byte[] operands = {
                gb.readAddress(Bitwise.toShort(address + 1)),
                gb.readAddress(Bitwise.toShort(address + 2))
        };

        return String.format(opcodeName, operands[0], operands[1]);
    }

}
