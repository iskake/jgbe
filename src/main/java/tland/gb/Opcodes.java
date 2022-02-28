package tland.gb;

import tland.gb.Inst.*;
import static tland.gb.Registers.RegisterIndex;

public class Opcodes {
    // TODO: Fix pointer instructions (e.g. ld b, [hl])

    private final static Instruction[] opcodes = {
/* 0x00 */ new NOP(),

/* 0x01 */ new LD_rr_nn("ld bc, $%04x", RegisterIndex.BC),

/* 0x02 */ new IllegalInst("illegal, 0x02"),
/* 0x03 */ new IllegalInst("illegal, 0x03"),
/* 0x04 */ new IllegalInst("illegal, 0x04"),
/* 0x05 */ new IllegalInst("illegal, 0x05"),

/* 0x06 */ new LD_rr_nn("ld b, $%02x", RegisterIndex.B),

/* 0x07 */ new IllegalInst("illegal, 0x07"),
/* 0x08 */ new IllegalInst("illegal, 0x08"),
/* 0x09 */ new IllegalInst("illegal, 0x09"),

/* 0x0a */ new IllegalInst("illegal, 0x0a"),

/* 0x0b */ new IllegalInst("illegal, 0x0b"),
/* 0x0c */ new IllegalInst("illegal, 0x0c"),
/* 0x0d */ new IllegalInst("illegal, 0x0d"),

/* 0x0e */ new LD_rr_nn("ld c, $%02x", RegisterIndex.C),

/* 0x0f */ new IllegalInst("illegal, 0x0f"),
/* 0x10 */ new IllegalInst("illegal, 0x10"),

/* 0x11 */ new LD_rr_nn("ld de, $%04x", RegisterIndex.DE),

/* 0x12 */ new IllegalInst("illegal, 0x12"),
/* 0x13 */ new IllegalInst("illegal, 0x13"),
/* 0x14 */ new IllegalInst("illegal, 0x14"),
/* 0x15 */ new IllegalInst("illegal, 0x15"),

/* 0x16 */ new LD_rr_nn("ld d, $%02x", RegisterIndex.D),

/* 0x17 */ new IllegalInst("illegal, 0x17"),
/* 0x18 */ new IllegalInst("illegal, 0x18"),
/* 0x19 */ new IllegalInst("illegal, 0x19"),
/* 0x1a */ new IllegalInst("illegal, 0x1a"),
/* 0x1b */ new IllegalInst("illegal, 0x1b"),
/* 0x1c */ new IllegalInst("illegal, 0x1c"),
/* 0x1d */ new IllegalInst("illegal, 0x1d"),

/* 0x1e */ new LD_rr_nn("ld e, $%02x", RegisterIndex.E),

/* 0x1f */ new IllegalInst("illegal, 0x1f"),
/* 0x20 */ new IllegalInst("illegal, 0x20"),

/* 0x21 */ new LD_rr_nn("ld hl, $%04x", RegisterIndex.HL),

/* 0x22 */ new IllegalInst("illegal, 0x22"),
/* 0x23 */ new IllegalInst("illegal, 0x23"),
/* 0x24 */ new IllegalInst("illegal, 0x24"),
/* 0x25 */ new IllegalInst("illegal, 0x25"),

/* 0x26 */ new LD_rr_nn("ld h, $%02x", RegisterIndex.H),

/* 0x27 */ new IllegalInst("illegal, 0x27"),
/* 0x28 */ new IllegalInst("illegal, 0x28"),
/* 0x29 */ new IllegalInst("illegal, 0x29"),
/* 0x2a */ new IllegalInst("illegal, 0x2a"),
/* 0x2b */ new IllegalInst("illegal, 0x2b"),
/* 0x2c */ new IllegalInst("illegal, 0x2c"),
/* 0x2d */ new IllegalInst("illegal, 0x2d"),

/* 0x2e */ new LD_rr_nn("ld l, $%02x", RegisterIndex.L),

/* 0x2f */ new IllegalInst("illegal, 0x2f"),
/* 0x30 */ new IllegalInst("illegal, 0x30"),
/* 0x31 */ new IllegalInst("illegal, 0x31"),
/* 0x32 */ new IllegalInst("illegal, 0x32"),
/* 0x33 */ new IllegalInst("illegal, 0x33"),
/* 0x34 */ new IllegalInst("illegal, 0x34"),
/* 0x35 */ new IllegalInst("illegal, 0x35"),

/* 0x36 */ new IllegalInst("illegal, 0x36"), // ld [hl], n8

/* 0x37 */ new IllegalInst("illegal, 0x37"),
/* 0x38 */ new IllegalInst("illegal, 0x38"),
/* 0x39 */ new IllegalInst("illegal, 0x39"),
/* 0x3a */ new IllegalInst("illegal, 0x3a"),
/* 0x3b */ new IllegalInst("illegal, 0x3b"),
/* 0x3c */ new IllegalInst("illegal, 0x3c"),
/* 0x3d */ new IllegalInst("illegal, 0x3d"),

/* 0x3e */ new LD_rr_nn("ld a, $%02x", RegisterIndex.A),

/* 0x3f */ new IllegalInst("illegal, 0x3f"),

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

/* 0x77 */ new IllegalInst("illegal, 0x77"),

/* 0x78 */ new LD_r8_r8("ld a, b", RegisterIndex.A, RegisterIndex.B),
/* 0x79 */ new LD_r8_r8("ld a, c", RegisterIndex.A, RegisterIndex.C),
/* 0x7a */ new LD_r8_r8("ld a, d", RegisterIndex.A, RegisterIndex.D),
/* 0x7b */ new LD_r8_r8("ld a, e", RegisterIndex.A, RegisterIndex.E),
/* 0x7c */ new LD_r8_r8("ld a, h", RegisterIndex.A, RegisterIndex.H),
/* 0x7d */ new LD_r8_r8("ld a, l", RegisterIndex.A, RegisterIndex.L),
/* 0x7e */ new LD_r8_r8("ld a, [hl]", RegisterIndex.A, RegisterIndex.HL),
/* 0x7f */ new LD_r8_r8("ld a, a", RegisterIndex.A, RegisterIndex.A),

/* 0x80 */ new IllegalInst("illegal, 0x80"),
/* 0x81 */ new IllegalInst("illegal, 0x81"),
/* 0x82 */ new IllegalInst("illegal, 0x82"),
/* 0x83 */ new IllegalInst("illegal, 0x83"),
/* 0x84 */ new IllegalInst("illegal, 0x84"),
/* 0x85 */ new IllegalInst("illegal, 0x85"),
/* 0x86 */ new IllegalInst("illegal, 0x86"),
/* 0x87 */ new IllegalInst("illegal, 0x87"),
/* 0x88 */ new IllegalInst("illegal, 0x88"),
/* 0x89 */ new IllegalInst("illegal, 0x89"),
/* 0x8a */ new IllegalInst("illegal, 0x8a"),
/* 0x8b */ new IllegalInst("illegal, 0x8b"),
/* 0x8c */ new IllegalInst("illegal, 0x8c"),
/* 0x8d */ new IllegalInst("illegal, 0x8d"),
/* 0x8e */ new IllegalInst("illegal, 0x8e"),
/* 0x8f */ new IllegalInst("illegal, 0x8f"),
/* 0x90 */ new IllegalInst("illegal, 0x90"),
/* 0x91 */ new IllegalInst("illegal, 0x91"),
/* 0x92 */ new IllegalInst("illegal, 0x92"),
/* 0x93 */ new IllegalInst("illegal, 0x93"),
/* 0x94 */ new IllegalInst("illegal, 0x94"),
/* 0x95 */ new IllegalInst("illegal, 0x95"),
/* 0x96 */ new IllegalInst("illegal, 0x96"),
/* 0x97 */ new IllegalInst("illegal, 0x97"),
/* 0x98 */ new IllegalInst("illegal, 0x98"),
/* 0x99 */ new IllegalInst("illegal, 0x99"),
/* 0x9a */ new IllegalInst("illegal, 0x9a"),
/* 0x9b */ new IllegalInst("illegal, 0x9b"),
/* 0x9c */ new IllegalInst("illegal, 0x9c"),
/* 0x9d */ new IllegalInst("illegal, 0x9d"),
/* 0x9e */ new IllegalInst("illegal, 0x9e"),
/* 0x9f */ new IllegalInst("illegal, 0x9f"),
/* 0xa0 */ new IllegalInst("illegal, 0xa0"),
/* 0xa1 */ new IllegalInst("illegal, 0xa1"),
/* 0xa2 */ new IllegalInst("illegal, 0xa2"),
/* 0xa3 */ new IllegalInst("illegal, 0xa3"),
/* 0xa4 */ new IllegalInst("illegal, 0xa4"),
/* 0xa5 */ new IllegalInst("illegal, 0xa5"),
/* 0xa6 */ new IllegalInst("illegal, 0xa6"),
/* 0xa7 */ new IllegalInst("illegal, 0xa7"),
/* 0xa8 */ new IllegalInst("illegal, 0xa8"),
/* 0xa9 */ new IllegalInst("illegal, 0xa9"),
/* 0xaa */ new IllegalInst("illegal, 0xaa"),
/* 0xab */ new IllegalInst("illegal, 0xab"),
/* 0xac */ new IllegalInst("illegal, 0xac"),
/* 0xad */ new IllegalInst("illegal, 0xad"),
/* 0xae */ new IllegalInst("illegal, 0xae"),
/* 0xaf */ new IllegalInst("illegal, 0xaf"),
/* 0xb0 */ new IllegalInst("illegal, 0xb0"),
/* 0xb1 */ new IllegalInst("illegal, 0xb1"),
/* 0xb2 */ new IllegalInst("illegal, 0xb2"),
/* 0xb3 */ new IllegalInst("illegal, 0xb3"),
/* 0xb4 */ new IllegalInst("illegal, 0xb4"),
/* 0xb5 */ new IllegalInst("illegal, 0xb5"),
/* 0xb6 */ new IllegalInst("illegal, 0xb6"),
/* 0xb7 */ new IllegalInst("illegal, 0xb7"),
/* 0xb8 */ new IllegalInst("illegal, 0xb8"),
/* 0xb9 */ new IllegalInst("illegal, 0xb9"),
/* 0xba */ new IllegalInst("illegal, 0xba"),
/* 0xbb */ new IllegalInst("illegal, 0xbb"),
/* 0xbc */ new IllegalInst("illegal, 0xbc"),
/* 0xbd */ new IllegalInst("illegal, 0xbd"),
/* 0xbe */ new IllegalInst("illegal, 0xbe"),
/* 0xbf */ new IllegalInst("illegal, 0xbf"),
/* 0xc0 */ new IllegalInst("illegal, 0xc0"),
/* 0xc1 */ new IllegalInst("illegal, 0xc1"),
/* 0xc2 */ new IllegalInst("illegal, 0xc2"),
/* 0xc3 */ new IllegalInst("illegal, 0xc3"),
/* 0xc4 */ new IllegalInst("illegal, 0xc4"),
/* 0xc5 */ new IllegalInst("illegal, 0xc5"),
/* 0xc6 */ new IllegalInst("illegal, 0xc6"),
/* 0xc7 */ new IllegalInst("illegal, 0xc7"),
/* 0xc8 */ new IllegalInst("illegal, 0xc8"),
/* 0xc9 */ new IllegalInst("illegal, 0xc9"),
/* 0xca */ new IllegalInst("illegal, 0xca"),

/* 0xcb */ new IllegalInst("illegal, 0xcb"), // Prefix

/* 0xcc */ new IllegalInst("illegal, 0xcc"),
/* 0xcd */ new IllegalInst("illegal, 0xcd"),
/* 0xce */ new IllegalInst("illegal, 0xce"),
/* 0xcf */ new IllegalInst("illegal, 0xcf"),
/* 0xd0 */ new IllegalInst("illegal, 0xd0"),
/* 0xd1 */ new IllegalInst("illegal, 0xd1"),
/* 0xd2 */ new IllegalInst("illegal, 0xd2"),
/* 0xd3 */ new IllegalInst("illegal, 0xd3"),
/* 0xd4 */ new IllegalInst("illegal, 0xd4"),
/* 0xd5 */ new IllegalInst("illegal, 0xd5"),
/* 0xd6 */ new IllegalInst("illegal, 0xd6"),
/* 0xd7 */ new IllegalInst("illegal, 0xd7"),
/* 0xd8 */ new IllegalInst("illegal, 0xd8"),
/* 0xd9 */ new IllegalInst("illegal, 0xd9"),
/* 0xda */ new IllegalInst("illegal, 0xda"),
/* 0xdb */ new IllegalInst("illegal, 0xdb"),
/* 0xdc */ new IllegalInst("illegal, 0xdc"),
/* 0xdd */ new IllegalInst("illegal, 0xdd"),
/* 0xde */ new IllegalInst("illegal, 0xde"),
/* 0xdf */ new IllegalInst("illegal, 0xdf"),

/* 0xe0 */ new LD_ptr_A("ldh a, [$%02x]"),

/* 0xe1 */ new IllegalInst("illegal, 0xe1"),

/* 0xe2 */ new LD_ptr_A("ld [c], a"),

/* 0xe3 */ new IllegalInst("illegal, 0xe3"),
/* 0xe4 */ new IllegalInst("illegal, 0xe4"),
/* 0xe5 */ new IllegalInst("illegal, 0xe5"),
/* 0xe6 */ new IllegalInst("illegal, 0xe6"),
/* 0xe7 */ new IllegalInst("illegal, 0xe7"),
/* 0xe8 */ new IllegalInst("illegal, 0xe8"),
/* 0xe9 */ new IllegalInst("illegal, 0xe9"),

/* 0xea */ new LD_ptr_A("ld [$%04x], a"),

/* 0xeb */ new IllegalInst("illegal, 0xeb"),
/* 0xec */ new IllegalInst("illegal, 0xec"),
/* 0xed */ new IllegalInst("illegal, 0xed"),
/* 0xee */ new IllegalInst("illegal, 0xee"),
/* 0xef */ new IllegalInst("illegal, 0xef"),

/* 0xf0 */ new LD_A_ptr("ld a, [$%02x]"),

/* 0xf1 */ new IllegalInst("illegal, 0xf1"),

/* 0xf2 */ new LD_A_ptr("ld a, [c]"),

/* 0xf3 */ new IllegalInst("illegal, 0xf3"),
/* 0xf4 */ new IllegalInst("illegal, 0xf4"),
/* 0xf5 */ new IllegalInst("illegal, 0xf5"),
/* 0xf6 */ new IllegalInst("illegal, 0xf6"),
/* 0xf7 */ new IllegalInst("illegal, 0xf7"),
/* 0xf8 */ new IllegalInst("illegal, 0xf8"),
/* 0xf9 */ new IllegalInst("illegal, 0xf9"),

/* 0xfa */ new LD_A_ptr("ld a, [$%04x]"),

/* 0xfb */ new IllegalInst("illegal, 0xfb"),
/* 0xfc */ new IllegalInst("illegal, 0xfc"),
/* 0xfd */ new IllegalInst("illegal, 0xfd"),
/* 0xfe */ new IllegalInst("illegal, 0xfe"),
/* 0xff */ new IllegalInst("illegal, 0xff"),
    };

    public static Instruction getOpcode(byte index) {
        int i = Byte.toUnsignedInt(index);
        return opcodes[i];
    }


    public static Instruction getOpcode(int index) {
        return opcodes[index];
    }

}
