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
    private static final Instruction[] opcodes = {
/* 0x00 */ new NOP(),

/* 0x01 */ new LD_rr_nn(),

/* 0x02 */ new LD_r8_r8(),

/* 0x03 */ new INC_rr(),
/* 0x04 */ new INC_rr(),
/* 0x05 */ new DEC_rr(),

/* 0x06 */ new LD_rr_nn(),

/* 0x07 */ new ROT(),

/* 0x08 */ new LD_ptr_rr(),

/* 0x09 */ new ADD_rr_nn(),

/* 0x0a */ new LD_r8_r8(),

/* 0x0b */ new DEC_rr(),
/* 0x0c */ new INC_rr(),
/* 0x0d */ new DEC_rr(),

/* 0x0e */ new LD_rr_nn(),

/* 0x0f */ new ROT(),

/* 0x10 */ new Halt(),

/* 0x11 */ new LD_rr_nn(),

/* 0x12 */ new LD_r8_r8(),

/* 0x13 */ new INC_rr(),
/* 0x14 */ new INC_rr(),
/* 0x15 */ new DEC_rr(),

/* 0x16 */ new LD_rr_nn(),

/* 0x17 */ new ROT(),

/* 0x18 */ new JR_cc_e8(),

/* 0x19 */ new ADD_rr_nn(),

/* 0x1a */ new LD_r8_r8(),

/* 0x1b */ new DEC_rr(),
/* 0x1c */ new INC_rr(),
/* 0x1d */ new DEC_rr(),

/* 0x1e */ new LD_rr_nn(),

/* 0x1f */ new ROT(),

/* 0x20 */ new JR_cc_e8(),

/* 0x21 */ new LD_rr_nn(),

/* 0x22 */ new LD_r8_r8(),

/* 0x23 */ new INC_rr(),
/* 0x24 */ new INC_rr(),
/* 0x25 */ new DEC_rr(),

/* 0x26 */ new LD_rr_nn(),

/* 0x27 */ new DAA(),

/* 0x28 */ new JR_cc_e8(),

/* 0x29 */ new ADD_rr_nn(),

/* 0x2a */ new LD_r8_r8(),

/* 0x2b */ new DEC_rr(),
/* 0x2c */ new INC_rr(),
/* 0x2d */ new DEC_rr(),

/* 0x2e */ new LD_rr_nn(),

/* 0x2f */ new CPL(),

/* 0x30 */ new JR_cc_e8(),

/* 0x31 */ new LD_rr_nn(),

/* 0x32 */ new LD_r8_r8(),

/* 0x33 */ new INC_rr(),
/* 0x34 */ new INC_rr(),
/* 0x35 */ new DEC_rr(),

/* 0x36 */ new LD_rr_nn(),

/* 0x37 */ new SCF(),

/* 0x38 */ new JR_cc_e8(),

/* 0x39 */ new ADD_rr_nn(),

/* 0x3a */ new LD_r8_r8(),

/* 0x3b */ new DEC_rr(),
/* 0x3c */ new INC_rr(),
/* 0x3d */ new DEC_rr(),

/* 0x3e */ new LD_rr_nn(),

/* 0x3f */ new CCF(),

/* 0x40 */ new LD_r8_r8(),
/* 0x41 */ new LD_r8_r8(),
/* 0x42 */ new LD_r8_r8(),
/* 0x43 */ new LD_r8_r8(),
/* 0x44 */ new LD_r8_r8(),
/* 0x45 */ new LD_r8_r8(),
/* 0x46 */ new LD_r8_r8(),
/* 0x47 */ new LD_r8_r8(),

/* 0x48 */ new LD_r8_r8(),
/* 0x49 */ new LD_r8_r8(),
/* 0x4a */ new LD_r8_r8(),
/* 0x4b */ new LD_r8_r8(),
/* 0x4c */ new LD_r8_r8(),
/* 0x4d */ new LD_r8_r8(),
/* 0x4e */ new LD_r8_r8(),
/* 0x4f */ new LD_r8_r8(),

/* 0x50 */ new LD_r8_r8(),
/* 0x51 */ new LD_r8_r8(),
/* 0x52 */ new LD_r8_r8(),
/* 0x53 */ new LD_r8_r8(),
/* 0x54 */ new LD_r8_r8(),
/* 0x55 */ new LD_r8_r8(),
/* 0x56 */ new LD_r8_r8(),
/* 0x57 */ new LD_r8_r8(),

/* 0x58 */ new LD_r8_r8(),
/* 0x59 */ new LD_r8_r8(),
/* 0x5a */ new LD_r8_r8(),
/* 0x5b */ new LD_r8_r8(),
/* 0x5c */ new LD_r8_r8(),
/* 0x5d */ new LD_r8_r8(),
/* 0x5e */ new LD_r8_r8(),
/* 0x5f */ new LD_r8_r8(),

/* 0x60 */ new LD_r8_r8(),
/* 0x61 */ new LD_r8_r8(),
/* 0x62 */ new LD_r8_r8(),
/* 0x63 */ new LD_r8_r8(),
/* 0x64 */ new LD_r8_r8(),
/* 0x65 */ new LD_r8_r8(),
/* 0x66 */ new LD_r8_r8(),
/* 0x67 */ new LD_r8_r8(),

/* 0x68 */ new LD_r8_r8(),
/* 0x69 */ new LD_r8_r8(),
/* 0x6a */ new LD_r8_r8(),
/* 0x6b */ new LD_r8_r8(),
/* 0x6c */ new LD_r8_r8(),
/* 0x6d */ new LD_r8_r8(),
/* 0x6e */ new LD_r8_r8(),
/* 0x6f */ new LD_r8_r8(),

/* 0x70 */ new LD_r8_r8(),
/* 0x71 */ new LD_r8_r8(),
/* 0x72 */ new LD_r8_r8(),
/* 0x73 */ new LD_r8_r8(),
/* 0x74 */ new LD_r8_r8(),
/* 0x75 */ new LD_r8_r8(),

/* 0x76 */ new Halt(),

/* 0x77 */ new LD_r8_r8(),

/* 0x78 */ new LD_r8_r8(),
/* 0x79 */ new LD_r8_r8(),
/* 0x7a */ new LD_r8_r8(),
/* 0x7b */ new LD_r8_r8(),
/* 0x7c */ new LD_r8_r8(),
/* 0x7d */ new LD_r8_r8(),
/* 0x7e */ new LD_r8_r8(),
/* 0x7f */ new LD_r8_r8(),

/* 0x80 */ new ADD_rr_nn(),
/* 0x81 */ new ADD_rr_nn(),
/* 0x82 */ new ADD_rr_nn(),
/* 0x83 */ new ADD_rr_nn(),
/* 0x84 */ new ADD_rr_nn(),
/* 0x85 */ new ADD_rr_nn(),
/* 0x86 */ new ADD_rr_nn(),
/* 0x87 */ new ADD_rr_nn(),

/* 0x88 */ new ADD_rr_nn(),
/* 0x89 */ new ADD_rr_nn(),
/* 0x8a */ new ADD_rr_nn(),
/* 0x8b */ new ADD_rr_nn(),
/* 0x8c */ new ADD_rr_nn(),
/* 0x8d */ new ADD_rr_nn(),
/* 0x8e */ new ADD_rr_nn(),
/* 0x8f */ new ADD_rr_nn(),

/* 0x90 */ new SUB_rr_nn(),
/* 0x91 */ new SUB_rr_nn(),
/* 0x92 */ new SUB_rr_nn(),
/* 0x93 */ new SUB_rr_nn(),
/* 0x94 */ new SUB_rr_nn(),
/* 0x95 */ new SUB_rr_nn(),
/* 0x96 */ new SUB_rr_nn(),
/* 0x97 */ new SUB_rr_nn(),

/* 0x98 */ new SUB_rr_nn(),
/* 0x99 */ new SUB_rr_nn(),
/* 0x9a */ new SUB_rr_nn(),
/* 0x9b */ new SUB_rr_nn(),
/* 0x9c */ new SUB_rr_nn(),
/* 0x9d */ new SUB_rr_nn(),
/* 0x9e */ new SUB_rr_nn(),
/* 0x9f */ new SUB_rr_nn(),

/* 0xa0 */ new AND_nn(),
/* 0xa1 */ new AND_nn(),
/* 0xa2 */ new AND_nn(),
/* 0xa3 */ new AND_nn(),
/* 0xa4 */ new AND_nn(),
/* 0xa5 */ new AND_nn(),
/* 0xa6 */ new AND_nn(),
/* 0xa7 */ new AND_nn(),

/* 0xa8 */ new XOR_nn(),
/* 0xa9 */ new XOR_nn(),
/* 0xaa */ new XOR_nn(),
/* 0xab */ new XOR_nn(),
/* 0xac */ new XOR_nn(),
/* 0xad */ new XOR_nn(),
/* 0xae */ new XOR_nn(),
/* 0xaf */ new XOR_nn(),

/* 0xb0 */ new OR_nn(),
/* 0xb1 */ new OR_nn(),
/* 0xb2 */ new OR_nn(),
/* 0xb3 */ new OR_nn(),
/* 0xb4 */ new OR_nn(),
/* 0xb5 */ new OR_nn(),
/* 0xb6 */ new OR_nn(),
/* 0xb7 */ new OR_nn(),

/* 0xb8 */ new CP_nn(),
/* 0xb9 */ new CP_nn(),
/* 0xba */ new CP_nn(),
/* 0xbb */ new CP_nn(),
/* 0xbc */ new CP_nn(),
/* 0xbd */ new CP_nn(),
/* 0xbe */ new CP_nn(),
/* 0xbf */ new CP_nn(),

/* 0xc0 */ new RET_cc(),

/* 0xc1 */ new POP_r16(),

/* 0xc2 */ new JP_cc_nn(),
/* 0xc3 */ new JP_cc_nn(),
/* 0xc4 */ new CALL_cc_n16(),

/* 0xc5 */ new PUSH_r16(),

/* 0xc6 */ new ADD_rr_nn(),

/* 0xc7 */ new RST_vec(),
/* 0xc8 */ new RET_cc(),
/* 0xc9 */ new RET_cc(),

/* 0xca */ new JP_cc_nn(),

/* 0xcb */ new Prefixed(),

/* 0xcc */ new CALL_cc_n16(),
/* 0xcd */ new CALL_cc_n16(),

/* 0xce */ new ADD_rr_nn(),

/* 0xcf */ new RST_vec(),
/* 0xd0 */ new RET_cc(),

/* 0xd1 */ new POP_r16(),

/* 0xd2 */ new JP_cc_nn(),

/* 0xd3 */ new IllegalInst(), // illegal

/* 0xd4 */ new CALL_cc_n16(),

/* 0xd5 */ new PUSH_r16(),

/* 0xd6 */ new SUB_rr_nn(),

/* 0xd7 */ new RST_vec(),
/* 0xd8 */ new RET_cc(),
/* 0xd9 */ new RET_cc(),
/* 0xda */ new JP_cc_nn(),

/* 0xdb */ new IllegalInst(), // illegal

/* 0xdc */ new CALL_cc_n16(),

/* 0xdd */ new IllegalInst(), // illegal

/* 0xde */ new SUB_rr_nn(),

/* 0xdf */ new RST_vec(),

/* 0xe0 */ new LD_ptr_rr(),

/* 0xe1 */ new POP_r16(),

/* 0xe2 */ new LD_ptr_rr(),

/* 0xe3 */ new IllegalInst(), // illegal
/* 0xe4 */ new IllegalInst(), // illegal

/* 0xe5 */ new PUSH_r16(),

/* 0xe6 */ new AND_nn(),

/* 0xe7 */ new RST_vec(),

/* 0xe8 */ new ADD_rr_nn(),

/* 0xe9 */ new JP_cc_nn(),

/* 0xea */ new LD_ptr_rr(),

/* 0xeb */ new IllegalInst(), // illegal
/* 0xec */ new IllegalInst(), // illegal
/* 0xed */ new IllegalInst(), // illegal

/* 0xee */ new XOR_nn(),

/* 0xef */ new RST_vec(),

/* 0xf0 */ new LD_A_ptr(),

/* 0xf1 */ new POP_r16(),

/* 0xf2 */ new LD_A_ptr(),

/* 0xf3 */ new Interrupt(),

/* 0xf4 */ new IllegalInst(), // illegal

/* 0xf5 */ new PUSH_r16(),

/* 0xf6 */ new OR_nn(),

/* 0xf7 */ new RST_vec(),

/* 0xf8 */ new LD_SP(),
/* 0xf9 */ new LD_SP(),
/* 0xfa */ new LD_A_ptr(),

/* 0xfb */ new Interrupt(),

/* 0xfc */ new IllegalInst(), // illegal
/* 0xfd */ new IllegalInst(), // illegal

/* 0xfe */ new CP_nn(),

/* 0xff */ new RST_vec(),
    };
    public static final String[] opcodeNames = {
            "nop",

            "ld bc, $_N16",

            "ld [bc], a",

            "inc bc",
            "inc b",
            "dec b",

            "ld b, $_N8",

            "rlca",

            "ld [$_N16], sp",

            "add hl, bc",

            "ld a, [bc]",

            "dec bc",
            "inc c",
            "dec c",

            "ld c, $_N8",

            "rrca",

            "stop",

            "ld de, $_N16",

            "ld [de], a",

            "inc de",
            "inc d",
            "dec d",

            "ld d, $_N8",

            "rla",

            "jr $_N8",

            "add hl, de",

            "ld a, [de]",

            "dec de",
            "inc e",
            "dec e",

            "ld e, $_N8",

            "rra",

            "jr nz, $_N8",

            "ld hl, $_N16",

            "ld [hli], a",

            "inc hl",
            "inc h",
            "dec h",

            "ld h, $_N8",

            "daa",

            "jr z, $_N8",

            "add hl, hl",

            "ld a, [hli]",

            "dec hl",
            "inc l",
            "dec l",

            "ld l, $_N8",

            "cpl",

            "jr nc, $_N8",

            "ld sp, $_N16",

            "ld [hld], a",

            "inc sp",
            "inc [hl]",
            "dec [hl]",

            "ld [hl], $_N8",

            "scf",

            "jr c, $_N8",

            "add hl, sp",

            "ld a, [hld]",

            "dec sp",
            "inc a",
            "dec a",

            "ld a, $_N8",

            "ccf",

            "ld b, b",
            "ld b, c",
            "ld b, d",
            "ld b, e",
            "ld b, h",
            "ld b, l",
            "ld b, [hl]",
            "ld b, a",

            "ld c, b",
            "ld c, c",
            "ld c, d",
            "ld c, e",
            "ld c, h",
            "ld c, l",
            "ld c, [hl]",
            "ld c, a",

            "ld d, b",
            "ld d, c",
            "ld d, d",
            "ld d, e",
            "ld d, h",
            "ld d, l",
            "ld d, [hl]",
            "ld d, a",

            "ld e, b",
            "ld e, c",
            "ld e, d",
            "ld e, e",
            "ld e, h",
            "ld e, l",
            "ld e, [hl]",
            "ld e, a",

            "ld h, b",
            "ld h, c",
            "ld h, d",
            "ld h, e",
            "ld h, h",
            "ld h, l",
            "ld h, [hl]",
            "ld h, a",

            "ld l, b",
            "ld l, c",
            "ld l, d",
            "ld l, e",
            "ld l, h",
            "ld l, l",
            "ld l, [hl]",
            "ld l, a",

            "ld [hl], b",
            "ld [hl], c",
            "ld [hl], d",
            "ld [hl], e",
            "ld [hl], h",
            "ld [hl], l",

            "halt",

            "ld [hl], a",

            "ld a, b",
            "ld a, c",
            "ld a, d",
            "ld a, e",
            "ld a, h",
            "ld a, l",
            "ld a, [hl]",
            "ld a, a",

            "add a, b",
            "add a, c",
            "add a, d",
            "add a, e",
            "add a, h",
            "add a, l",
            "add a, [hl]",
            "add a, a",

            "adc b",
            "adc c",
            "adc d",
            "adc e",
            "adc h",
            "adc l",
            "adc [hl]",
            "adc a",

            "sub b",
            "sub c",
            "sub d",
            "sub e",
            "sub h",
            "sub l",
            "sub [hl]",
            "sub a",

            "sbc b",
            "sbc c",
            "sbc d",
            "sbc e",
            "sbc h",
            "sbc l",
            "sbc [hl]",
            "sbc a",

            "and b",
            "and c",
            "and d",
            "and e",
            "and h",
            "and l",
            "and [hl]",
            "and a",

            "xor b",
            "xor c",
            "xor d",
            "xor e",
            "xor h",
            "xor l",
            "xor [hl]",
            "xor a",

            "or b",
            "or c",
            "or d",
            "or e",
            "or h",
            "or l",
            "or [hl]",
            "or a",

            "cp b",
            "cp c",
            "cp d",
            "cp e",
            "cp h",
            "cp l",
            "cp [hl]",
            "cp a",

            "ret nz",

            "pop bc",

            "jp nz, $_N16",
            "jp $_N16",
            "call nz, $_N16",

            "push bc",

            "add a, $_N8",

            "rst $00",
            "ret z",
            "ret",

            "jp z, $_N16",

            "PREFIXED",

            "call z, $_N16",
            "call $_N16",

            "adc $_N8",

            "rst $08",
            "ret nc",

            "pop de",

            "jp nc, $_N16",

            "illegal $d3",

            "call nc, $_N16",

            "push de",

            "sub $_N8",

            "rst $10",
            "ret c",
            "reti",
            "jp c, $_N16",

            "illegal $db",

            "call c, $_N16",

            "illegal $dd",

            "sbc $_N8",

            "rst $18",

            "ldh [$ff_N8], a",

            "pop hl",

            "ldh [c], a",

            "illegal $e3",
            "illegal $e4",

            "push hl",

            "and $_N8",

            "rst $20",

            "add sp, $_N8",

            "jp hl",

            "ld [$_N16], a",

            "illegal $eb",
            "illegal $ec",
            "illegal $ed",

            "xor $_N8",

            "rst $28",

            "ldh a, [$ff_N8]",

            "pop af",

            "ldh a, [c]",

            "di",

            "illegal $f4",

            "push af",

            "or $_N8",

            "rst $30",

            "ld hl, sp + $_N8",
            "ld sp, hl",
            "ld a, [$_N16]",

            "ei",

            "illegal $fc",
            "illegal $fd",

            "cp $_N8",

            "rst $38",
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
        byte opcode = gb.readAddressNoCycle(address);

        String opcodeName = opcodeNames[Byte.toUnsignedInt(opcode)];

        if (opcodeName.equals("PREFIXED")) {
            opcode = gb.readAddressNoCycle(Bitwise.toShort(address + 1));
            opcodeName = new Prefixed().getPrefixedName(Byte.toUnsignedInt(opcode));
        }

        opcodeName = opcodeName.replace("_N8", "%02x");
        opcodeName = opcodeName.replace("_N16", "%2$02x%1$02x");

        byte[] operands = {
                gb.readAddressNoCycle(Bitwise.toShort(address + 1)),
                gb.readAddressNoCycle(Bitwise.toShort(address + 2))
        };

        return String.format(opcodeName, operands[0], operands[1]);
    }

}
