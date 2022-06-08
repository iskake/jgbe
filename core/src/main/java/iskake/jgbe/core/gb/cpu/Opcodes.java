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

/* 0x01 */ new LD_rr_nn("ld bc, $_N16", Register.BC),

/* 0x02 */ new LD_r8_r8("ld [bc], a", Register.BC, Register.A),

/* 0x03 */ new INC_rr("inc bc", Register.BC),
/* 0x04 */ new INC_rr("inc b", Register.B),
/* 0x05 */ new DEC_rr("dec b", Register.B),

/* 0x06 */ new LD_rr_nn("ld b, $_N8", Register.B),

/* 0x07 */ new ROT("rlca"),

/* 0x08 */ new LD_ptr_rr("ld [$_N16], sp"),

/* 0x09 */ new ADD_rr_nn("add hl, bc", Register.HL, Register.BC),

/* 0x0a */ new LD_r8_r8("ld a, [bc]", Register.A, Register.BC),

/* 0x0b */ new DEC_rr("dec bc", Register.BC),
/* 0x0c */ new INC_rr("inc c", Register.C),
/* 0x0d */ new DEC_rr("dec c", Register.C),

/* 0x0e */ new LD_rr_nn("ld c, $_N8", Register.C),

/* 0x0f */ new ROT("rrca"),

/* 0x10 */ new Halt("stop"),

/* 0x11 */ new LD_rr_nn("ld de, $_N16", Register.DE),

/* 0x12 */ new LD_r8_r8("ld [de], a", Register.DE, Register.A),

/* 0x13 */ new INC_rr("inc de", Register.DE),
/* 0x14 */ new INC_rr("inc d", Register.D),
/* 0x15 */ new DEC_rr("dec d", Register.D),

/* 0x16 */ new LD_rr_nn("ld d, $_N8", Register.D),

/* 0x17 */ new ROT("rla"),

/* 0x18 */ new JR_cc_e8("jr $_N8", Conditions.NONE),

/* 0x19 */ new ADD_rr_nn("add hl, de", Register.HL, Register.DE),

/* 0x1a */ new LD_r8_r8("ld a, [de]", Register.A, Register.DE),

/* 0x1b */ new DEC_rr("dec de", Register.DE),
/* 0x1c */ new INC_rr("inc e", Register.E),
/* 0x1d */ new DEC_rr("dec e", Register.E),

/* 0x1e */ new LD_rr_nn("ld e, $_N8", Register.E),

/* 0x1f */ new ROT("rra"),

/* 0x20 */ new JR_cc_e8("jr nz, $_N8", Conditions.NZ),

/* 0x21 */ new LD_rr_nn("ld hl, $_N16", Register.HL),

/* 0x22 */ new LD_r8_r8("ld [hli], a", Register.HL, Register.A),

/* 0x23 */ new INC_rr("inc hl", Register.HL),
/* 0x24 */ new INC_rr("inc h", Register.H),
/* 0x25 */ new DEC_rr("dec h", Register.H),

/* 0x26 */ new LD_rr_nn("ld h, $_N8", Register.H),

/* 0x27 */ new DAA(),

/* 0x28 */ new JR_cc_e8("jr z, $_N8", Conditions.Z),

/* 0x29 */ new ADD_rr_nn("add hl, hl", Register.HL, Register.HL),

/* 0x2a */ new LD_r8_r8("ld a, [hli]", Register.A, Register.HL),

/* 0x2b */ new DEC_rr("dec hl", Register.HL),
/* 0x2c */ new INC_rr("inc l", Register.L),
/* 0x2d */ new DEC_rr("dec l", Register.L),

/* 0x2e */ new LD_rr_nn("ld l, $_N8", Register.L),

/* 0x2f */ new CPL(),

/* 0x30 */ new JR_cc_e8("jr nc, $_N8", Conditions.NC),

/* 0x31 */ new LD_rr_nn("ld sp, $_N16", Register.SP),

/* 0x32 */ new LD_r8_r8("ld [hld], a", Register.HL, Register.A),

/* 0x33 */ new INC_rr("inc sp", Register.SP),
/* 0x34 */ new INC_rr("inc [hl]", Register.HL),
/* 0x35 */ new DEC_rr("dec [hl]", Register.HL),

/* 0x36 */ new LD_rr_nn("ld [hl], $_N8", Register.HL),

/* 0x37 */ new SCF(),

/* 0x38 */ new JR_cc_e8("jr c, $_N8", Conditions.C),

/* 0x39 */ new ADD_rr_nn("add hl, sp", Register.HL, Register.SP),

/* 0x3a */ new LD_r8_r8("ld a, [hld]", Register.A, Register.HL),

/* 0x3b */ new DEC_rr("dec sp", Register.SP),
/* 0x3c */ new INC_rr("inc a", Register.A),
/* 0x3d */ new DEC_rr("dec a", Register.A),

/* 0x3e */ new LD_rr_nn("ld a, $_N8", Register.A),

/* 0x3f */ new CCF(),

/* 0x40 */ new LD_r8_r8("ld b, b", Register.B, Register.B),
/* 0x41 */ new LD_r8_r8("ld b, c", Register.B, Register.C),
/* 0x42 */ new LD_r8_r8("ld b, d", Register.B, Register.D),
/* 0x43 */ new LD_r8_r8("ld b, e", Register.B, Register.E),
/* 0x44 */ new LD_r8_r8("ld b, h", Register.B, Register.H),
/* 0x45 */ new LD_r8_r8("ld b, l", Register.B, Register.L),
/* 0x46 */ new LD_r8_r8("ld b, [hl]", Register.B, Register.HL),
/* 0x47 */ new LD_r8_r8("ld b, a", Register.B, Register.A),

/* 0x48 */ new LD_r8_r8("ld c, b", Register.C, Register.B),
/* 0x49 */ new LD_r8_r8("ld c, c", Register.C, Register.C),
/* 0x4a */ new LD_r8_r8("ld c, d", Register.C, Register.D),
/* 0x4b */ new LD_r8_r8("ld c, e", Register.C, Register.E),
/* 0x4c */ new LD_r8_r8("ld c, h", Register.C, Register.H),
/* 0x4d */ new LD_r8_r8("ld c, l", Register.C, Register.L),
/* 0x4e */ new LD_r8_r8("ld c, [hl]", Register.C, Register.HL),
/* 0x4f */ new LD_r8_r8("ld c, a", Register.C, Register.A),

/* 0x50 */ new LD_r8_r8("ld d, b", Register.D, Register.B),
/* 0x51 */ new LD_r8_r8("ld d, c", Register.D, Register.C),
/* 0x52 */ new LD_r8_r8("ld d, d", Register.D, Register.D),
/* 0x53 */ new LD_r8_r8("ld d, e", Register.D, Register.E),
/* 0x54 */ new LD_r8_r8("ld d, h", Register.D, Register.H),
/* 0x55 */ new LD_r8_r8("ld d, l", Register.D, Register.L),
/* 0x56 */ new LD_r8_r8("ld d, [hl]", Register.D, Register.HL),
/* 0x57 */ new LD_r8_r8("ld d, a", Register.D, Register.A),

/* 0x58 */ new LD_r8_r8("ld e, b", Register.E, Register.B),
/* 0x59 */ new LD_r8_r8("ld e, c", Register.E, Register.C),
/* 0x5a */ new LD_r8_r8("ld e, d", Register.E, Register.D),
/* 0x5b */ new LD_r8_r8("ld e, e", Register.E, Register.E),
/* 0x5c */ new LD_r8_r8("ld e, h", Register.E, Register.H),
/* 0x5d */ new LD_r8_r8("ld e, l", Register.E, Register.L),
/* 0x5e */ new LD_r8_r8("ld e, [hl]", Register.E, Register.HL),
/* 0x5f */ new LD_r8_r8("ld e, a", Register.E, Register.A),

/* 0x60 */ new LD_r8_r8("ld h, b", Register.H, Register.B),
/* 0x61 */ new LD_r8_r8("ld h, c", Register.H, Register.C),
/* 0x62 */ new LD_r8_r8("ld h, d", Register.H, Register.D),
/* 0x63 */ new LD_r8_r8("ld h, e", Register.H, Register.E),
/* 0x64 */ new LD_r8_r8("ld h, h", Register.H, Register.H),
/* 0x65 */ new LD_r8_r8("ld h, l", Register.H, Register.L),
/* 0x66 */ new LD_r8_r8("ld h, [hl]", Register.H, Register.HL),
/* 0x67 */ new LD_r8_r8("ld h, a", Register.H, Register.A),

/* 0x68 */ new LD_r8_r8("ld l, b", Register.L, Register.B),
/* 0x69 */ new LD_r8_r8("ld l, c", Register.L, Register.C),
/* 0x6a */ new LD_r8_r8("ld l, d", Register.L, Register.D),
/* 0x6b */ new LD_r8_r8("ld l, e", Register.L, Register.E),
/* 0x6c */ new LD_r8_r8("ld l, h", Register.L, Register.H),
/* 0x6d */ new LD_r8_r8("ld l, l", Register.L, Register.L),
/* 0x6e */ new LD_r8_r8("ld l, [hl]", Register.L, Register.HL),
/* 0x6f */ new LD_r8_r8("ld l, a", Register.L, Register.A),

/* 0x70 */ new LD_r8_r8("ld [hl], b", Register.HL, Register.B),
/* 0x71 */ new LD_r8_r8("ld [hl], c", Register.HL, Register.C),
/* 0x72 */ new LD_r8_r8("ld [hl], d", Register.HL, Register.D),
/* 0x73 */ new LD_r8_r8("ld [hl], e", Register.HL, Register.E),
/* 0x74 */ new LD_r8_r8("ld [hl], h", Register.HL, Register.H),
/* 0x75 */ new LD_r8_r8("ld [hl], l", Register.HL, Register.L),

/* 0x76 */ new Halt("halt"),

/* 0x77 */ new LD_r8_r8("ld [hl], a", Register.HL, Register.A),

/* 0x78 */ new LD_r8_r8("ld a, b", Register.A, Register.B),
/* 0x79 */ new LD_r8_r8("ld a, c", Register.A, Register.C),
/* 0x7a */ new LD_r8_r8("ld a, d", Register.A, Register.D),
/* 0x7b */ new LD_r8_r8("ld a, e", Register.A, Register.E),
/* 0x7c */ new LD_r8_r8("ld a, h", Register.A, Register.H),
/* 0x7d */ new LD_r8_r8("ld a, l", Register.A, Register.L),
/* 0x7e */ new LD_r8_r8("ld a, [hl]", Register.A, Register.HL),
/* 0x7f */ new LD_r8_r8("ld a, a", Register.A, Register.A),

/* 0x80 */ new ADD_rr_nn("add a, b", Register.A, Register.B, false),
/* 0x81 */ new ADD_rr_nn("add a, c", Register.A, Register.C, false),
/* 0x82 */ new ADD_rr_nn("add a, d", Register.A, Register.D, false),
/* 0x83 */ new ADD_rr_nn("add a, e", Register.A, Register.E, false),
/* 0x84 */ new ADD_rr_nn("add a, h", Register.A, Register.H, false),
/* 0x85 */ new ADD_rr_nn("add a, l", Register.A, Register.L, false),
/* 0x86 */ new ADD_rr_nn("add a, [hl]", Register.A, Register.HL, false),
/* 0x87 */ new ADD_rr_nn("add a, a", Register.A, Register.A, false),

/* 0x88 */ new ADD_rr_nn("adc b", Register.A, Register.B, true),
/* 0x89 */ new ADD_rr_nn("adc c", Register.A, Register.C, true),
/* 0x8a */ new ADD_rr_nn("adc d", Register.A, Register.D, true),
/* 0x8b */ new ADD_rr_nn("adc e", Register.A, Register.E, true),
/* 0x8c */ new ADD_rr_nn("adc h", Register.A, Register.H, true),
/* 0x8d */ new ADD_rr_nn("adc l", Register.A, Register.L, true),
/* 0x8e */ new ADD_rr_nn("adc [hl]", Register.A, Register.HL, true),
/* 0x8f */ new ADD_rr_nn("adc a", Register.A, Register.A, true),

/* 0x90 */ new SUB_rr_nn("sub b", Register.A, Register.B, false),
/* 0x91 */ new SUB_rr_nn("sub c", Register.A, Register.C, false),
/* 0x92 */ new SUB_rr_nn("sub d", Register.A, Register.D, false),
/* 0x93 */ new SUB_rr_nn("sub e", Register.A, Register.E, false),
/* 0x94 */ new SUB_rr_nn("sub h", Register.A, Register.H, false),
/* 0x95 */ new SUB_rr_nn("sub l", Register.A, Register.L, false),
/* 0x96 */ new SUB_rr_nn("sub [hl]", Register.A, Register.HL, false),
/* 0x97 */ new SUB_rr_nn("sub a", Register.A, Register.A, false),

/* 0x98 */ new SUB_rr_nn("sbc b", Register.A, Register.B, true),
/* 0x99 */ new SUB_rr_nn("sbc c", Register.A, Register.C, true),
/* 0x9a */ new SUB_rr_nn("sbc d", Register.A, Register.D, true),
/* 0x9b */ new SUB_rr_nn("sbc e", Register.A, Register.E, true),
/* 0x9c */ new SUB_rr_nn("sbc h", Register.A, Register.H, true),
/* 0x9d */ new SUB_rr_nn("sbc l", Register.A, Register.L, true),
/* 0x9e */ new SUB_rr_nn("sbc [hl]", Register.A, Register.HL, true),
/* 0x9f */ new SUB_rr_nn("sbc a", Register.A, Register.A, true),

/* 0xa0 */ new AND_nn("and b", Register.B),
/* 0xa1 */ new AND_nn("and c", Register.C),
/* 0xa2 */ new AND_nn("and d", Register.D),
/* 0xa3 */ new AND_nn("and e", Register.E),
/* 0xa4 */ new AND_nn("and h", Register.H),
/* 0xa5 */ new AND_nn("and l", Register.L),
/* 0xa6 */ new AND_nn("and [hl]", Register.HL),
/* 0xa7 */ new AND_nn("and a", Register.A),

/* 0xa8 */ new XOR_nn("xor b", Register.B),
/* 0xa9 */ new XOR_nn("xor c", Register.C),
/* 0xaa */ new XOR_nn("xor d", Register.D),
/* 0xab */ new XOR_nn("xor e", Register.E),
/* 0xac */ new XOR_nn("xor h", Register.H),
/* 0xad */ new XOR_nn("xor l", Register.L),
/* 0xae */ new XOR_nn("xor [hl]", Register.HL),
/* 0xaf */ new XOR_nn("xor a", Register.A),

/* 0xb0 */ new OR_nn("or b", Register.B),
/* 0xb1 */ new OR_nn("or c", Register.C),
/* 0xb2 */ new OR_nn("or d", Register.D),
/* 0xb3 */ new OR_nn("or e", Register.E),
/* 0xb4 */ new OR_nn("or h", Register.H),
/* 0xb5 */ new OR_nn("or l", Register.L),
/* 0xb6 */ new OR_nn("or [hl]", Register.HL),
/* 0xb7 */ new OR_nn("or a", Register.A),

/* 0xb8 */ new CP_nn("cp b", Register.B),
/* 0xb9 */ new CP_nn("cp c", Register.C),
/* 0xba */ new CP_nn("cp d", Register.D),
/* 0xbb */ new CP_nn("cp e", Register.E),
/* 0xbc */ new CP_nn("cp h", Register.H),
/* 0xbd */ new CP_nn("cp l", Register.L),
/* 0xbe */ new CP_nn("cp [hl]", Register.HL),
/* 0xbf */ new CP_nn("cp a", Register.A),

/* 0xc0 */ new RET_cc("ret nz", Conditions.NZ),

/* 0xc1 */ new POP_r16("pop bc", Register.BC),

/* 0xc2 */ new JP_cc_nn("jp nz, $_N16", Conditions.NZ),
/* 0xc3 */ new JP_cc_nn("jp $_N16", Conditions.NONE),
/* 0xc4 */ new CALL_cc_n16("call nz, $_N16", Conditions.NZ),

/* 0xc5 */ new PUSH_r16("push bc", Register.BC),

/* 0xc6 */ new ADD_rr_nn("add a, $_N8", Register.A, null, false),

/* 0xc7 */ new RST_vec("rst $00"),
/* 0xc8 */ new RET_cc("ret z", Conditions.Z),
/* 0xc9 */ new RET_cc("ret", Conditions.NONE),

/* 0xca */ new JP_cc_nn("jp z, $_N16", Conditions.Z),

/* 0xcb */ new Prefixed(),

/* 0xcc */ new CALL_cc_n16("call z, $_N16", Conditions.Z),
/* 0xcd */ new CALL_cc_n16("call $_N16", Conditions.NONE),

/* 0xce */ new ADD_rr_nn("adc $_N8", Register.A, null, true),

/* 0xcf */ new RST_vec("rst $08"),
/* 0xd0 */ new RET_cc("ret nc", Conditions.NC),

/* 0xd1 */ new POP_r16("pop de", Register.DE),

/* 0xd2 */ new JP_cc_nn("jp nc, $_N16", Conditions.NC),

/* 0xd3 */ new IllegalInst("illegal $d3"), // illegal

/* 0xd4 */ new CALL_cc_n16("call nc, $_N16", Conditions.NC),

/* 0xd5 */ new PUSH_r16("push de", Register.DE),

/* 0xd6 */ new SUB_rr_nn("sub $_N8", Register.A, null, false),

/* 0xd7 */ new RST_vec("rst $10"),
/* 0xd8 */ new RET_cc("ret c", Conditions.C),
/* 0xd9 */ new RET_cc("reti", Conditions.NONE),
/* 0xda */ new JP_cc_nn("jp c, $_N16", Conditions.C),

/* 0xdb */ new IllegalInst("illegal $db"), // illegal

/* 0xdc */ new CALL_cc_n16("call c, $_N16", Conditions.C),

/* 0xdd */ new IllegalInst("illegal $dd"), // illegal

/* 0xde */ new SUB_rr_nn("sbc $_N8", Register.A, null, true),

/* 0xdf */ new RST_vec("rst $18"),

/* 0xe0 */ new LD_ptr_rr("ldh [$ff_N8], a"),

/* 0xe1 */ new POP_r16("pop hl", Register.HL),

/* 0xe2 */ new LD_ptr_rr("ldh [c], a"),

/* 0xe3 */ new IllegalInst("illegal $e3"), // illegal
/* 0xe4 */ new IllegalInst("illegal $e4"), // illegal

/* 0xe5 */ new PUSH_r16("push hl", Register.HL),

/* 0xe6 */ new AND_nn("and $_N8", null),

/* 0xe7 */ new RST_vec("rst $20"),

/* 0xe8 */ new ADD_rr_nn("add sp, $_N8", Register.SP, null),

/* 0xe9 */ new JP_cc_nn("jp hl", Conditions.NONE),

/* 0xea */ new LD_ptr_rr("ld [$_N16], a"),

/* 0xeb */ new IllegalInst("illegal $eb"), // illegal
/* 0xec */ new IllegalInst("illegal $ec"), // illegal
/* 0xed */ new IllegalInst("illegal $ed"), // illegal

/* 0xee */ new XOR_nn("xor $_N8", null),

/* 0xef */ new RST_vec("rst $28"),

/* 0xf0 */ new LD_A_ptr("ldh a, [$ff_N8]"),

/* 0xf1 */ new POP_r16("pop af", Register.AF),

/* 0xf2 */ new LD_A_ptr("ldh a, [c]"),

/* 0xf3 */ new Interrupt("di"),

/* 0xf4 */ new IllegalInst("illegal $f4"), // illegal

/* 0xf5 */ new PUSH_r16("push af", Register.AF),

/* 0xf6 */ new OR_nn("or $_N8", null),

/* 0xf7 */ new RST_vec("rst $30"),

/* 0xf8 */ new LD_SP("ld hl, sp + $_N8"),
/* 0xf9 */ new LD_SP("ld sp, hl"),
/* 0xfa */ new LD_A_ptr("ld a, [$_N16]"),

/* 0xfb */ new Interrupt("ei"),

/* 0xfc */ new IllegalInst("illegal $fc"), // illegal
/* 0xfd */ new IllegalInst("illegal $fd"), // illegal

/* 0xfe */ new CP_nn("cp $_N8", null),

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
        byte opcode = gb.readMemoryAddress(address);

        String opcodeName;

        Instruction inst = Opcodes.getOpcode(opcode);

        if (inst instanceof Prefixed o) {
            opcode = gb.readMemoryAddress(Bitwise.toShort(address + 1));
            opcodeName = o.getPrefixedName(Byte.toUnsignedInt(opcode));
        } else {
            opcodeName = Opcodes.getOpcode(opcode).getName();
        }

        opcodeName = opcodeName.replace("_N8", "%02x");
        opcodeName = opcodeName.replace("_N16", "%2$02x%1$02x");

        byte[] operands = {
                gb.readMemoryAddress(Bitwise.toShort(address + 1)),
                gb.readMemoryAddress(Bitwise.toShort(address + 2))
        };

        return String.format(opcodeName, operands[0], operands[1]);
    }

}
