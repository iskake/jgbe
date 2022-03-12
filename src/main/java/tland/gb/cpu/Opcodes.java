package tland.gb.cpu;

import tland.gb.Registers.RegisterIndex;
import tland.gb.cpu.Inst.*;

public class Opcodes {
    // TODO: Fix pointer instructions (e.g. ld b, [hl])

    private final static Instruction[] opcodes = {
/* 0x00 */ new NOP(),

/* 0x01 */ new LD_rr_nn("ld bc, $%04x", RegisterIndex.BC),

/* 0x02 */ new LD_r8_r8("ld [bc], a", RegisterIndex.BC, RegisterIndex.A),

/* 0x03 */ new INC_rr("inc bc", RegisterIndex.BC),
/* 0x04 */ new INC_rr("inc b", RegisterIndex.B),

/* 0x05 */ new IllegalInst("unimplemented, 0x05"), // dec b

/* 0x06 */ new LD_rr_nn("ld b, $%02x", RegisterIndex.B),

/* 0x07 */ new IllegalInst("unimplemented, 0x07"), // rcla
/* 0x08 */ new IllegalInst("unimplemented, 0x08"), // ld [$n16], sp

/* 0x09 */ new ADD_rr_nn("add hl, bc", RegisterIndex.HL, RegisterIndex.BC),

/* 0x0a */ new LD_r8_r8("ld a, [bc]", RegisterIndex.A, RegisterIndex.BC),

/* 0x0b */ new IllegalInst("unimplemented, 0x0b"), // dec bc

/* 0x0c */ new INC_rr("inc c", RegisterIndex.C),

/* 0x0d */ new IllegalInst("unimplemented, 0x0d"), // dec c

/* 0x0e */ new LD_rr_nn("ld c, $%02x", RegisterIndex.C),

/* 0x0f */ new IllegalInst("unimplemented, 0x0f"), // rrca
/* 0x10 */ new IllegalInst("unimplemented, 0x10"), // stop $n8

/* 0x11 */ new LD_rr_nn("ld de, $%04x", RegisterIndex.DE),

/* 0x12 */ new LD_r8_r8("ld [de], a", RegisterIndex.DE, RegisterIndex.A),

/* 0x13 */ new INC_rr("inc de", RegisterIndex.DE),
/* 0x14 */ new INC_rr("inc d", RegisterIndex.D),

/* 0x15 */ new IllegalInst("unimplemented, 0x15"), // dec d

/* 0x16 */ new LD_rr_nn("ld d, $%02x", RegisterIndex.D),

/* 0x17 */ new IllegalInst("unimplemented, 0x17"), // rla
/* 0x18 */ new IllegalInst("unimplemented, 0x18"), // jr r8

/* 0x19 */ new ADD_rr_nn("add hl, de", RegisterIndex.HL, RegisterIndex.DE),

/* 0x1a */ new LD_r8_r8("ld a, [de]", RegisterIndex.A, RegisterIndex.DE),

/* 0x1b */ new IllegalInst("unimplemented, 0x1b"), // dec de

/* 0x1c */ new INC_rr("inc e", RegisterIndex.E),

/* 0x1d */ new IllegalInst("unimplemented, 0x1d"), // dec e

/* 0x1e */ new LD_rr_nn("ld e, $%02x", RegisterIndex.E),

/* 0x1f */ new IllegalInst("unimplemented, 0x1f"), // rra
/* 0x20 */ new IllegalInst("unimplemented, 0x20"), // jr nz, r8

/* 0x21 */ new LD_rr_nn("ld hl, $%04x", RegisterIndex.HL),

/* 0x22 */ new IllegalInst("unimplemented, 0x22"), // ld [hl+], a

/* 0x23 */ new INC_rr("inc hl", RegisterIndex.HL),
/* 0x24 */ new INC_rr("inc h", RegisterIndex.H),

/* 0x25 */ new IllegalInst("unimplemented, 0x25"), // dec [hl]

/* 0x26 */ new LD_rr_nn("ld h, $%02x", RegisterIndex.H),

/* 0x27 */ new IllegalInst("unimplemented, 0x27"), // daa
/* 0x28 */ new IllegalInst("unimplemented, 0x28"), // jr z, r8

/* 0x29 */ new ADD_rr_nn("add hl, hl", RegisterIndex.HL, RegisterIndex.HL),

/* 0x2a */ new IllegalInst("unimplemented, 0x2a"), // ld a [hl+]
/* 0x2b */ new IllegalInst("unimplemented, 0x2b"), // dec hl

/* 0x2c */ new INC_rr("inc l", RegisterIndex.L),

/* 0x2d */ new IllegalInst("unimplemented, 0x2d"), // dec l

/* 0x2e */ new LD_rr_nn("ld l, $%02x", RegisterIndex.L),

/* 0x2f */ new IllegalInst("unimplemented, 0x2f"), // cpl
/* 0x30 */ new IllegalInst("unimplemented, 0x30"), // jr nc, r8
/* 0x31 */ new IllegalInst("unimplemented, 0x31"), // ld sp, $n16

/* 0x32 */ new IllegalInst("unimplemented, 0x32"), // ld [hl-], a

/* 0x33 */ new INC_rr("inc sp", RegisterIndex.SP), // inc sp
/* 0x34 */ new INC_rr("inc [hl]", RegisterIndex.HL),

/* 0x35 */ new IllegalInst("unimplemented, 0x35"), // dec [hl]

/* 0x36 */ new LD_rr_nn("ld [hl], %02x", RegisterIndex.HL),

/* 0x37 */ new IllegalInst("unimplemented, 0x37"), // scf
/* 0x38 */ new IllegalInst("unimplemented, 0x38"), // jr c, r8
/* 0x39 */ new IllegalInst("unimplemented, 0x39"), // add hl, sp
/* 0x3a */ new IllegalInst("unimplemented, 0x3a"), // ld a, [hl-]
/* 0x3b */ new IllegalInst("unimplemented, 0x3b"), // dec sp

/* 0x3c */ new INC_rr("inc a", RegisterIndex.A),

/* 0x3d */ new IllegalInst("unimplemented, 0x3d"), // dec a

/* 0x3e */ new LD_rr_nn("ld a, $%02x", RegisterIndex.A),

/* 0x3f */ new IllegalInst("unimplemented, 0x3f"), // ccf

/* 0x40 */ new LD_r8_r8("ld b, b", RegisterIndex.B, RegisterIndex.B),
/* 0x41 */ new LD_r8_r8("ld b, c", RegisterIndex.B, RegisterIndex.C),
/* 0x42 */ new LD_r8_r8("ld b, d", RegisterIndex.B, RegisterIndex.D),
/* 0x43 */ new LD_r8_r8("ld b, e", RegisterIndex.B, RegisterIndex.E),
/* 0x44 */ new LD_r8_r8("ld b, h", RegisterIndex.B, RegisterIndex.H),
/* 0x45 */ new LD_r8_r8("ld b, l", RegisterIndex.B, RegisterIndex.L),
/* 0x46 */ new LD_r8_r8("ld b, [hl]", RegisterIndex.B, RegisterIndex.HL),
/* 0x47 */ new LD_r8_r8("ld b, a", RegisterIndex.B, RegisterIndex.A),

/* 0x48 */ new LD_r8_r8("ld c, b", RegisterIndex.C, RegisterIndex.B),
/* 0x49 */ new LD_r8_r8("ld c, c", RegisterIndex.C, RegisterIndex.C),
/* 0x4a */ new LD_r8_r8("ld c, d", RegisterIndex.C, RegisterIndex.D),
/* 0x4b */ new LD_r8_r8("ld c, e", RegisterIndex.C, RegisterIndex.E),
/* 0x4c */ new LD_r8_r8("ld c, h", RegisterIndex.C, RegisterIndex.H),
/* 0x4d */ new LD_r8_r8("ld c, l", RegisterIndex.C, RegisterIndex.L),
/* 0x4e */ new LD_r8_r8("ld c, [hl]", RegisterIndex.C, RegisterIndex.HL),
/* 0x4f */ new LD_r8_r8("ld c, a", RegisterIndex.C, RegisterIndex.A),

/* 0x50 */ new LD_r8_r8("ld d, b", RegisterIndex.D, RegisterIndex.B),
/* 0x51 */ new LD_r8_r8("ld d, c", RegisterIndex.D, RegisterIndex.C),
/* 0x52 */ new LD_r8_r8("ld d, d", RegisterIndex.D, RegisterIndex.D),
/* 0x53 */ new LD_r8_r8("ld d, e", RegisterIndex.D, RegisterIndex.E),
/* 0x54 */ new LD_r8_r8("ld d, h", RegisterIndex.D, RegisterIndex.H),
/* 0x55 */ new LD_r8_r8("ld d, l", RegisterIndex.D, RegisterIndex.L),
/* 0x56 */ new LD_r8_r8("ld d, [hl]", RegisterIndex.D, RegisterIndex.HL),
/* 0x57 */ new LD_r8_r8("ld d, a", RegisterIndex.D, RegisterIndex.A),

/* 0x58 */ new LD_r8_r8("ld e, b", RegisterIndex.E, RegisterIndex.B),
/* 0x59 */ new LD_r8_r8("ld e, c", RegisterIndex.E, RegisterIndex.C),
/* 0x5a */ new LD_r8_r8("ld e, d", RegisterIndex.E, RegisterIndex.D),
/* 0x5b */ new LD_r8_r8("ld e, e", RegisterIndex.E, RegisterIndex.E),
/* 0x5c */ new LD_r8_r8("ld e, h", RegisterIndex.E, RegisterIndex.H),
/* 0x5d */ new LD_r8_r8("ld e, l", RegisterIndex.E, RegisterIndex.L),
/* 0x5e */ new LD_r8_r8("ld e, [hl]", RegisterIndex.E, RegisterIndex.HL),
/* 0x5f */ new LD_r8_r8("ld e, a", RegisterIndex.E, RegisterIndex.A),

/* 0x60 */ new LD_r8_r8("ld h, b", RegisterIndex.H, RegisterIndex.B),
/* 0x61 */ new LD_r8_r8("ld h, c", RegisterIndex.H, RegisterIndex.C),
/* 0x62 */ new LD_r8_r8("ld h, d", RegisterIndex.H, RegisterIndex.D),
/* 0x63 */ new LD_r8_r8("ld h, e", RegisterIndex.H, RegisterIndex.E),
/* 0x64 */ new LD_r8_r8("ld h, h", RegisterIndex.H, RegisterIndex.H),
/* 0x65 */ new LD_r8_r8("ld h, l", RegisterIndex.H, RegisterIndex.L),
/* 0x66 */ new LD_r8_r8("ld h, [hl]", RegisterIndex.H, RegisterIndex.HL),
/* 0x67 */ new LD_r8_r8("ld h, a", RegisterIndex.H, RegisterIndex.A),

/* 0x68 */ new LD_r8_r8("ld l, b", RegisterIndex.L, RegisterIndex.B),
/* 0x69 */ new LD_r8_r8("ld l, c", RegisterIndex.L, RegisterIndex.C),
/* 0x6a */ new LD_r8_r8("ld l, d", RegisterIndex.L, RegisterIndex.D),
/* 0x6b */ new LD_r8_r8("ld l, e", RegisterIndex.L, RegisterIndex.E),
/* 0x6c */ new LD_r8_r8("ld l, h", RegisterIndex.L, RegisterIndex.H),
/* 0x6d */ new LD_r8_r8("ld l, l", RegisterIndex.L, RegisterIndex.L),
/* 0x6e */ new LD_r8_r8("ld l, [hl]", RegisterIndex.L, RegisterIndex.HL),
/* 0x6f */ new LD_r8_r8("ld l, a", RegisterIndex.L, RegisterIndex.A),

/* 0x70 */ new LD_r8_r8("ld [hl], b", RegisterIndex.HL, RegisterIndex.B),
/* 0x71 */ new LD_r8_r8("ld [hl], c", RegisterIndex.HL, RegisterIndex.C),
/* 0x72 */ new LD_r8_r8("ld [hl], d", RegisterIndex.HL, RegisterIndex.D),
/* 0x73 */ new LD_r8_r8("ld [hl], e", RegisterIndex.HL, RegisterIndex.E),
/* 0x74 */ new LD_r8_r8("ld [hl], h", RegisterIndex.HL, RegisterIndex.H),
/* 0x75 */ new LD_r8_r8("ld [hl], l", RegisterIndex.HL, RegisterIndex.L),

/* 0x76 */ new IllegalInst("HALT"), // TODO

/* 0x77 */ new LD_r8_r8("ld [hl], a", RegisterIndex.HL, RegisterIndex.A),

/* 0x78 */ new LD_r8_r8("ld a, b", RegisterIndex.A, RegisterIndex.B),
/* 0x79 */ new LD_r8_r8("ld a, c", RegisterIndex.A, RegisterIndex.C),
/* 0x7a */ new LD_r8_r8("ld a, d", RegisterIndex.A, RegisterIndex.D),
/* 0x7b */ new LD_r8_r8("ld a, e", RegisterIndex.A, RegisterIndex.E),
/* 0x7c */ new LD_r8_r8("ld a, h", RegisterIndex.A, RegisterIndex.H),
/* 0x7d */ new LD_r8_r8("ld a, l", RegisterIndex.A, RegisterIndex.L),
/* 0x7e */ new LD_r8_r8("ld a, [hl]", RegisterIndex.A, RegisterIndex.HL),
/* 0x7f */ new LD_r8_r8("ld a, a", RegisterIndex.A, RegisterIndex.A),

/* 0x80 */ new ADD_rr_nn("add a, b", RegisterIndex.A, RegisterIndex.B, false),
/* 0x81 */ new ADD_rr_nn("add a, c", RegisterIndex.A, RegisterIndex.C, false),
/* 0x82 */ new ADD_rr_nn("add a, d", RegisterIndex.A, RegisterIndex.D, false),
/* 0x83 */ new ADD_rr_nn("add a, e", RegisterIndex.A, RegisterIndex.E, false),
/* 0x84 */ new ADD_rr_nn("add a, h", RegisterIndex.A, RegisterIndex.H, false),
/* 0x85 */ new ADD_rr_nn("add a, l", RegisterIndex.A, RegisterIndex.L, false),
/* 0x86 */ new ADD_rr_nn("add a, [hl]", RegisterIndex.A, RegisterIndex.HL, false),
/* 0x87 */ new ADD_rr_nn("add a, a", RegisterIndex.A, RegisterIndex.A, false),

/* 0x88 */ new ADD_rr_nn("adc a, b", RegisterIndex.A, RegisterIndex.B, true),
/* 0x89 */ new ADD_rr_nn("adc a, c", RegisterIndex.A, RegisterIndex.C, true),
/* 0x8a */ new ADD_rr_nn("adc a, d", RegisterIndex.A, RegisterIndex.D, true),
/* 0x8b */ new ADD_rr_nn("adc a, e", RegisterIndex.A, RegisterIndex.E, true),
/* 0x8c */ new ADD_rr_nn("adc a, h", RegisterIndex.A, RegisterIndex.H, true),
/* 0x8d */ new ADD_rr_nn("adc a, l", RegisterIndex.A, RegisterIndex.L, true),
/* 0x8e */ new ADD_rr_nn("adc a, [hl]", RegisterIndex.A, RegisterIndex.HL, true),
/* 0x8f */ new ADD_rr_nn("adc a, a", RegisterIndex.A, RegisterIndex.A, true),

/* 0x90 */ new SUB_rr_nn("sub b", RegisterIndex.A, RegisterIndex.B, false),
/* 0x91 */ new SUB_rr_nn("sub c", RegisterIndex.A, RegisterIndex.C, false),
/* 0x92 */ new SUB_rr_nn("sub d", RegisterIndex.A, RegisterIndex.D, false),
/* 0x93 */ new SUB_rr_nn("sub e", RegisterIndex.A, RegisterIndex.E, false),
/* 0x94 */ new SUB_rr_nn("sub h", RegisterIndex.A, RegisterIndex.H, false),
/* 0x95 */ new SUB_rr_nn("sub l", RegisterIndex.A, RegisterIndex.L, false),
/* 0x96 */ new SUB_rr_nn("sub [hl]", RegisterIndex.A, RegisterIndex.HL, false),
/* 0x97 */ new SUB_rr_nn("sub a", RegisterIndex.A, RegisterIndex.A, false),

/* 0x98 */ new SUB_rr_nn("sbc a, b", RegisterIndex.A, RegisterIndex.B, true),
/* 0x99 */ new SUB_rr_nn("sbc a, c", RegisterIndex.A, RegisterIndex.C, true),
/* 0x9a */ new SUB_rr_nn("sbc a, d", RegisterIndex.A, RegisterIndex.D, true),
/* 0x9b */ new SUB_rr_nn("sbc a, e", RegisterIndex.A, RegisterIndex.E, true),
/* 0x9c */ new SUB_rr_nn("sbc a, h", RegisterIndex.A, RegisterIndex.H, true),
/* 0x9d */ new SUB_rr_nn("sbc a, l", RegisterIndex.A, RegisterIndex.L, true),
/* 0x9e */ new SUB_rr_nn("sbc a, [hl]", RegisterIndex.A, RegisterIndex.HL, true),
/* 0x9f */ new SUB_rr_nn("sbc a, a", RegisterIndex.A, RegisterIndex.A, true),

/* 0xa0 */ new AND_nn("and b", RegisterIndex.B),
/* 0xa1 */ new AND_nn("and c", RegisterIndex.C),
/* 0xa2 */ new AND_nn("and d", RegisterIndex.D),
/* 0xa3 */ new AND_nn("and e", RegisterIndex.E),
/* 0xa4 */ new AND_nn("and h", RegisterIndex.H),
/* 0xa5 */ new AND_nn("and l", RegisterIndex.L),
/* 0xa6 */ new AND_nn("and [hl]", RegisterIndex.HL),
/* 0xa7 */ new AND_nn("and a", RegisterIndex.A),

/* 0xa8 */ new XOR_nn("xor b", RegisterIndex.B),
/* 0xa9 */ new XOR_nn("xor c", RegisterIndex.C),
/* 0xaa */ new XOR_nn("xor d", RegisterIndex.D),
/* 0xab */ new XOR_nn("xor e", RegisterIndex.E),
/* 0xac */ new XOR_nn("xor h", RegisterIndex.H),
/* 0xad */ new XOR_nn("xor l", RegisterIndex.L),
/* 0xae */ new XOR_nn("xor [hl]", RegisterIndex.HL),
/* 0xaf */ new XOR_nn("xor a", RegisterIndex.A),

/* 0xb0 */ new OR_nn("or b", RegisterIndex.B),
/* 0xb1 */ new OR_nn("or c", RegisterIndex.C),
/* 0xb2 */ new OR_nn("or d", RegisterIndex.D),
/* 0xb3 */ new OR_nn("or e", RegisterIndex.E),
/* 0xb4 */ new OR_nn("or h", RegisterIndex.H),
/* 0xb5 */ new OR_nn("or l", RegisterIndex.L),
/* 0xb6 */ new OR_nn("or [hl]", RegisterIndex.HL),
/* 0xb7 */ new OR_nn("or a", RegisterIndex.A),

/* 0xb8 */ new CP_nn("cp b", RegisterIndex.B),
/* 0xb9 */ new CP_nn("cp c", RegisterIndex.C),
/* 0xba */ new CP_nn("cp d", RegisterIndex.D),
/* 0xbb */ new CP_nn("cp e", RegisterIndex.E),
/* 0xbc */ new CP_nn("cp h", RegisterIndex.H),
/* 0xbd */ new CP_nn("cp l", RegisterIndex.L),
/* 0xbe */ new CP_nn("cp [hl]", RegisterIndex.HL),
/* 0xbf */ new CP_nn("cp a", RegisterIndex.A),

/* 0xc0 */ new IllegalInst("unimplemented, 0xc0"), // ret nz
/* 0xc1 */ new IllegalInst("unimplemented, 0xc1"), // pop bc
/* 0xc2 */ new IllegalInst("unimplemented, 0xc2"), // jp nz, $n16

/* 0xc3 */ new JP_cc_nn("jp $%02x%02x"),

/* 0xc4 */ new IllegalInst("unimplemented, 0xc4"), // call nz, $n16
/* 0xc5 */ new IllegalInst("unimplemented, 0xc5"), // push bc

/* 0xc6 */ new ADD_rr_nn("add a, $%02x", RegisterIndex.A, null, false),

/* 0xc7 */ new IllegalInst("unimplemented, 0xc7"), // rst $00
/* 0xc8 */ new IllegalInst("unimplemented, 0xc8"), // ret z
/* 0xc9 */ new IllegalInst("unimplemented, 0xc9"), // ret
/* 0xca */ new IllegalInst("unimplemented, 0xca"), // jp z $n16

/* 0xcb */ new Prefixed(),

/* 0xcc */ new IllegalInst("unimplemented, 0xcc"), // call z, $n16
/* 0xcd */ new IllegalInst("unimplemented, 0xcd"), // call $n16

/* 0xce */ new ADD_rr_nn("adc a, $%02x", RegisterIndex.A, null, true),

/* 0xcf */ new IllegalInst("unimplemented, 0xcf"), // rst $08
/* 0xd0 */ new IllegalInst("unimplemented, 0xd0"), // ret nc
/* 0xd1 */ new IllegalInst("unimplemented, 0xd1"), // pop de
/* 0xd2 */ new IllegalInst("unimplemented, 0xd2"), // jp nc $n16
/* 0xd3 */ new IllegalInst("illegal $d3"), // illegal
/* 0xd4 */ new IllegalInst("unimplemented, 0xd4"), // call nc, $n16
/* 0xd5 */ new IllegalInst("unimplemented, 0xd5"), // push de

/* 0xd6 */ new SUB_rr_nn("sub $%02x", RegisterIndex.A, null, false),

/* 0xd7 */ new IllegalInst("unimplemented, 0xd7"), // rst $10
/* 0xd8 */ new IllegalInst("unimplemented, 0xd8"), // ret c
/* 0xd9 */ new IllegalInst("unimplemented, 0xd9"), // reti
/* 0xda */ new IllegalInst("unimplemented, 0xda"), // jp c, $n16
/* 0xdb */ new IllegalInst("illegal $db"), // illegal
/* 0xdc */ new IllegalInst("unimplemented, 0xdc"), // call c, $n16
/* 0xdd */ new IllegalInst("illegal $dd"), // illegal

/* 0xde */ new SUB_rr_nn("sbc $%02x", RegisterIndex.A, null, true),

/* 0xdf */ new IllegalInst("unimplemented, 0xdf"), // rst $18

/* 0xe0 */ new LD_ptr_A("ldh a, [$%02x]"),

/* 0xe1 */ new IllegalInst("unimplemented, 0xe1"), // pop

/* 0xe2 */ new LD_ptr_A("ld [c], a"),

/* 0xe3 */ new IllegalInst("illegal $e3"), // illegal
/* 0xe4 */ new IllegalInst("illegal $e4"), // illegal
/* 0xe5 */ new IllegalInst("unimplemented, 0xe5"), // push hl

/* 0xe6 */ new AND_nn("and $%02x", null),

/* 0xe7 */ new IllegalInst("unimplemented, 0xe7"), // rst $20
/* 0xe8 */ new IllegalInst("unimplemented, 0xe8"), // add sp, r8
/* 0xe9 */ new IllegalInst("unimplemented, 0xe9"), // jp hl

/* 0xea */ new LD_ptr_A("ld [$%04x], a"),

/* 0xeb */ new IllegalInst("illegal $eb"), // illegal
/* 0xec */ new IllegalInst("illegal $ec"), // illegal
/* 0xed */ new IllegalInst("illegal $ed"), // illegal

/* 0xee */ new XOR_nn("xor $%02x", null),

/* 0xef */ new IllegalInst("unimplemented, 0xef"), // rst $28

/* 0xf0 */ new LD_A_ptr("ld a, [$%02x]"),

/* 0xf1 */ new IllegalInst("unimplemented, 0xf1"), // pop af

/* 0xf2 */ new LD_A_ptr("ld a, [c]"),

/* 0xf3 */ new IllegalInst("unimplemented, 0xf3"), // di
/* 0xf4 */ new IllegalInst("illegal $f4"), // illegal
/* 0xf5 */ new IllegalInst("unimplemented, 0xf5"), // push af

/* 0xf6 */ new OR_nn("or $%02x", null),

/* 0xf7 */ new IllegalInst("unimplemented, 0xf7"), // rst $30
/* 0xf8 */ new IllegalInst("unimplemented, 0xf8"), // ld hl, sp+$s8 (signed)
/* 0xf9 */ new IllegalInst("unimplemented, 0xf9"), // ld sp, hl

/* 0xfa */ new LD_A_ptr("ld a, [$%04x]"),

/* 0xfb */ new IllegalInst("unimplemented, 0xfb"), // ei
/* 0xfc */ new IllegalInst("illegal $fc"), // illegal
/* 0xfd */ new IllegalInst("illegal $fd"), // illegal

/* 0xfe */ new CP_nn("cp $%02x", null),

/* 0xff */ new IllegalInst("unimplemented, 0xff"), // rst $38
    };

    public static Instruction getOpcode(byte index) {
        int i = Byte.toUnsignedInt(index);
        return opcodes[i];
    }


    public static Instruction getOpcode(int index) {
        return opcodes[index];
    }

}
