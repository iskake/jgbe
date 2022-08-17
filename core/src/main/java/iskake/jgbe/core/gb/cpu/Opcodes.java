package iskake.jgbe.core.gb.cpu;

import iskake.jgbe.core.gb.IGameBoy;
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
            MISC::nop,          LD::ld_r16_n16,  LD::ld_ptr,        INC_rr::inc_rr,       INC_rr::inc_rr,       DEC_rr::dec_rr,       LD::ld_r8_n8,  ROT::rota,
            LD_ptr::ld_ptr_rr,  ADD::add_hl_r16, LD::ld_ptr,        DEC_rr::dec_rr,       INC_rr::inc_rr,       DEC_rr::dec_rr,       LD::ld_r8_n8,  ROT::rota,
            Halt::stop,         LD::ld_r16_n16,  LD::ld_ptr,        INC_rr::inc_rr,       INC_rr::inc_rr,       DEC_rr::dec_rr,       LD::ld_r8_n8,  ROT::rota,
            JR_cc_e8::jr_cc_e8, ADD::add_hl_r16, LD::ld_ptr,        DEC_rr::dec_rr,       INC_rr::inc_rr,       DEC_rr::dec_rr,       LD::ld_r8_n8,  ROT::rota,
            JR_cc_e8::jr_cc_e8, LD::ld_r16_n16,  LD::ld_ptr,        INC_rr::inc_rr,       INC_rr::inc_rr,       DEC_rr::dec_rr,       LD::ld_r8_n8,  MISC::daa,
            JR_cc_e8::jr_cc_e8, ADD::add_hl_r16, LD::ld_ptr,        DEC_rr::dec_rr,       INC_rr::inc_rr,       DEC_rr::dec_rr,       LD::ld_r8_n8,  MISC::cpl,
            JR_cc_e8::jr_cc_e8, LD::ld_r16_n16,  LD::ld_ptr,        INC_rr::inc_rr,       INC_rr::inc_rr,       DEC_rr::dec_rr,       LD::ld_r8_n8,  MISC::scf,
            JR_cc_e8::jr_cc_e8, ADD::add_hl_r16, LD::ld_ptr,        DEC_rr::dec_rr,       INC_rr::inc_rr,       DEC_rr::dec_rr,       LD::ld_r8_n8,  MISC::ccf,

            LD::ld_r8_r8,       LD::ld_r8_r8,    LD::ld_r8_r8,      LD::ld_r8_r8,         LD::ld_r8_r8,         LD::ld_r8_r8,         LD::ld_r8_r8,  LD::ld_r8_r8,
            LD::ld_r8_r8,       LD::ld_r8_r8,    LD::ld_r8_r8,      LD::ld_r8_r8,         LD::ld_r8_r8,         LD::ld_r8_r8,         LD::ld_r8_r8,  LD::ld_r8_r8,
            LD::ld_r8_r8,       LD::ld_r8_r8,    LD::ld_r8_r8,      LD::ld_r8_r8,         LD::ld_r8_r8,         LD::ld_r8_r8,         LD::ld_r8_r8,  LD::ld_r8_r8,
            LD::ld_r8_r8,       LD::ld_r8_r8,    LD::ld_r8_r8,      LD::ld_r8_r8,         LD::ld_r8_r8,         LD::ld_r8_r8,         LD::ld_r8_r8,  LD::ld_r8_r8,
            LD::ld_r8_r8,       LD::ld_r8_r8,    LD::ld_r8_r8,      LD::ld_r8_r8,         LD::ld_r8_r8,         LD::ld_r8_r8,         LD::ld_r8_r8,  LD::ld_r8_r8,
            LD::ld_r8_r8,       LD::ld_r8_r8,    LD::ld_r8_r8,      LD::ld_r8_r8,         LD::ld_r8_r8,         LD::ld_r8_r8,         LD::ld_r8_r8,  LD::ld_r8_r8,
            LD::ld_r8_r8,       LD::ld_r8_r8,    LD::ld_r8_r8,      LD::ld_r8_r8,         LD::ld_r8_r8,         LD::ld_r8_r8,         Halt::halt,    LD::ld_r8_r8,
            LD::ld_r8_r8,       LD::ld_r8_r8,    LD::ld_r8_r8,      LD::ld_r8_r8,         LD::ld_r8_r8,         LD::ld_r8_r8,         LD::ld_r8_r8,  LD::ld_r8_r8,

            ADD::add_a_r8,      ADD::add_a_r8,   ADD::add_a_r8,     ADD::add_a_r8,        ADD::add_a_r8,        ADD::add_a_r8,        ADD::add_a_r8, ADD::add_a_r8,
            ADD::add_a_r8,      ADD::add_a_r8,   ADD::add_a_r8,     ADD::add_a_r8,        ADD::add_a_r8,        ADD::add_a_r8,        ADD::add_a_r8, ADD::add_a_r8,
            SUB::sub,           SUB::sub,        SUB::sub,          SUB::sub,             SUB::sub,             SUB::sub,             SUB::sub,      SUB::sub,
            SUB::sub,           SUB::sub,        SUB::sub,          SUB::sub,             SUB::sub,             SUB::sub,             SUB::sub,      SUB::sub,
            AND::and,           AND::and,        AND::and,          AND::and,             AND::and,             AND::and,             AND::and,      AND::and,
            XOR::xor,           XOR::xor,        XOR::xor,          XOR::xor,             XOR::xor,             XOR::xor,             XOR::xor,      XOR::xor,
            OR::or,             OR::or,          OR::or,            OR::or,               OR::or,               OR::or,               OR::or,        OR::or,
            CP::cp,             CP::cp,          CP::cp,            CP::cp,               CP::cp,               CP::cp,               CP::cp,        CP::cp,

            RET::ret_cc,        POP::pop_r16,    JP::jp_cc_nn,      JP::jp_cc_nn,         CALL::call_cc_n16,    PUSH::push_r16,       ADD::add_a_r8, RST::rst_vec,
            RET::ret_cc,        RET::ret_cc,     JP::jp_cc_nn,      Prefixed::doOp,       CALL::call_cc_n16,    CALL::call_cc_n16,    ADD::add_a_r8, RST::rst_vec,
            RET::ret_cc,        POP::pop_r16,    JP::jp_cc_nn,      IllegalInst::illegal, CALL::call_cc_n16,    PUSH::push_r16,       SUB::sub,      RST::rst_vec,
            RET::ret_cc,        RET::ret_cc,     JP::jp_cc_nn,      IllegalInst::illegal, CALL::call_cc_n16,    IllegalInst::illegal, SUB::sub,      RST::rst_vec,
            LD_ptr::ld_ptr_rr,  POP::pop_r16,    LD_ptr::ld_ptr_rr, IllegalInst::illegal, IllegalInst::illegal, PUSH::push_r16,       AND::and,      RST::rst_vec,
            ADD::add_sp_e8,     JP::jp_cc_nn,    LD_ptr::ld_ptr_rr, IllegalInst::illegal, IllegalInst::illegal, IllegalInst::illegal, XOR::xor,      RST::rst_vec,
            LD_ptr::ld_a_ptr,   POP::pop_r16,    LD_ptr::ld_a_ptr,  Interrupt::di,        IllegalInst::illegal, PUSH::push_r16,       OR::or,        RST::rst_vec,
            LD_SP::ld_hl_sp,    LD_SP::ld_sp_hl, LD_ptr::ld_a_ptr,  Interrupt::ei,        IllegalInst::illegal, IllegalInst::illegal, CP::cp,        RST::rst_vec,
    };
    public static final String[] opcodeNames = {
            "nop",             "ld bc, $_N16", "ld [bc], a",    "inc bc",     "inc b",          "dec b",      "ld b, $_N8",    "rlca",
            "ld [$_N16], sp",  "add hl, bc",   "ld a, [bc]",    "dec bc",     "inc c",          "dec c",      "ld c, $_N8",    "rrca",
            "stop",            "ld de, $_N16", "ld [de], a",    "inc de",     "inc d",          "dec d",      "ld d, $_N8",    "rla",
            "jr $_N8",         "add hl, de",   "ld a, [de]",    "dec de",     "inc e",          "dec e",      "ld e, $_N8",    "rra",
            "jr nz, $_N8",     "ld hl, $_N16", "ld [hli], a",   "inc hl",     "inc h",          "dec h",      "ld h, $_N8",    "daa",
            "jr z, $_N8",      "add hl, hl",   "ld a, [hli]",   "dec hl",     "inc l",          "dec l",      "ld l, $_N8",    "cpl",
            "jr nc, $_N8",     "ld sp, $_N16", "ld [hld], a",   "inc sp",     "inc [hl]",       "dec [hl]",   "ld [hl], $_N8", "scf",
            "jr c, $_N8",      "add hl, sp",   "ld a, [hld]",   "dec sp",     "inc a",          "dec a",      "ld a, $_N8",    "ccf",

            "ld b, b",         "ld b, c",      "ld b, d",       "ld b, e",    "ld b, h",        "ld b, l",    "ld b, [hl]",    "ld b, a",
            "ld c, b",         "ld c, c",      "ld c, d",       "ld c, e",    "ld c, h",        "ld c, l",    "ld c, [hl]",    "ld c, a",
            "ld d, b",         "ld d, c",      "ld d, d",       "ld d, e",    "ld d, h",        "ld d, l",    "ld d, [hl]",    "ld d, a",
            "ld e, b",         "ld e, c",      "ld e, d",       "ld e, e",    "ld e, h",        "ld e, l",    "ld e, [hl]",    "ld e, a",
            "ld h, b",         "ld h, c",      "ld h, d",       "ld h, e",    "ld h, h",        "ld h, l",    "ld h, [hl]",    "ld h, a",
            "ld l, b",         "ld l, c",      "ld l, d",       "ld l, e",    "ld l, h",        "ld l, l",    "ld l, [hl]",    "ld l, a",
            "ld [hl], b",      "ld [hl], c",   "ld [hl], d",    "ld [hl], e", "ld [hl], h",     "ld [hl], l", "halt",          "ld [hl], a",
            "ld a, b",         "ld a, c",      "ld a, d",       "ld a, e",    "ld a, h",        "ld a, l",    "ld a, [hl]",    "ld a, a",

            "add a, b",         "add a, c",    "add a, d",      "add a, e",   "add a, h",       "add a, l",   "add a, [hl]",   "add a, a",
            "adc b",            "adc c",       "adc d",         "adc e",      "adc h",          "adc l",      "adc [hl]",      "adc a",
            "sub b",            "sub c",       "sub d",         "sub e",      "sub h",          "sub l",      "sub [hl]",      "sub a",
            "sbc b",            "sbc c",       "sbc d",         "sbc e",      "sbc h",          "sbc l",      "sbc [hl]",      "sbc a",
            "and b",            "and c",       "and d",         "and e",      "and h",          "and l",      "and [hl]",      "and a",
            "xor b",            "xor c",       "xor d",         "xor e",      "xor h",          "xor l",      "xor [hl]",      "xor a",
            "or b",             "or c",        "or d",          "or e",       "or h",           "or l",       "or [hl]",       "or a",
            "cp b",             "cp c",        "cp d",          "cp e",       "cp h",           "cp l",       "cp [hl]",       "cp a",

            "ret nz",           "pop bc",      "jp nz, $_N16",  "jp $_N16",   "call nz, $_N16", "push bc",    "add a, $_N8",   "rst $00",
            "ret z",            "ret",         "jp z, $_N16",   "PREFIXED",   "call z, $_N16",  "call $_N16", "adc $_N8",      "rst $08",
            "ret nc",           "pop de",      "jp nc, $_N16",  "ILLEGAL",    "call nc, $_N16", "push de",    "sub $_N8",      "rst $10",
            "ret c",            "reti",        "jp c, $_N16",   "ILLEGAL",    "call c, $_N16",  "ill $dd",    "sbc $_N8",      "rst $18",
            "ldh [$ff_N8], a",  "pop hl",      "ldh [c], a",    "ILLEGAL",    "ILLEGAL",        "push hl",    "and $_N8",      "rst $20",
            "add sp, $_N8",     "jp hl",       "ld [$_N16], a", "ILLEGAL",    "ILLEGAL",        "ILLEGAL",    "xor $_N8",      "rst $28",
            "ldh a, [$ff_N8]",  "pop af",      "ldh a, [c]",    "di",         "ILLEGAL",        "push af",    "or $_N8",       "rst $30",
            "ld hl, sp + $_N8", "ld sp, hl",   "ld a, [$_N16]", "ei",         "ILLEGAL",        "ILLEGAL",    "cp $_N8",       "rst $38",
    };

    public static final String[] opcodeNamesPrefixed = {
            "rlc b",    "rlc c",    "rlc d",    "rlc e",    "rlc h",    "rlc l",    "rlc [hl]",    "rlc a",
            "rrc b",    "rrc c",    "rrc d",    "rrc e",    "rrc h",    "rrc l",    "rrc [hl]",    "rrc a",
            "rl b",     "rl c",     "rl d",     "rl e",     "rl h",     "rl l",     "rl [hl]",     "rl a",
            "rr b",     "rr c",     "rr d",     "rr e",     "rr h",     "rr l",     "rr [hl]",     "rr a",
            "sla b",    "sla c",    "sla d",    "sla e",    "sla h",    "sla l",    "sla [hl]",    "sla a",
            "sra b",    "sra c",    "sra d",    "sra e",    "sra h",    "sra l",    "sra [hl]",    "sra a",
            "swap b",   "swap c",   "swap d",   "swap e",   "swap h",   "swap l",   "swap [hl]",   "swap a",
            "srl b",    "srl c",    "srl d",    "srl e",    "srl h",    "srl l",    "srl [hl]",    "srl a",
            "bit 0, b", "bit 0, c", "bit 0, d", "bit 0, e", "bit 0, h", "bit 0, l", "bit 0, [hl]", "bit 0, a",
            "bit 1, b", "bit 1, c", "bit 1, d", "bit 1, e", "bit 1, h", "bit 1, l", "bit 1, [hl]", "bit 1, a",
            "bit 2, b", "bit 2, c", "bit 2, d", "bit 2, e", "bit 2, h", "bit 2, l", "bit 2, [hl]", "bit 2, a",
            "bit 3, b", "bit 3, c", "bit 3, d", "bit 3, e", "bit 3, h", "bit 3, l", "bit 3, [hl]", "bit 3, a",
            "bit 4, b", "bit 4, c", "bit 4, d", "bit 4, e", "bit 4, h", "bit 4, l", "bit 4, [hl]", "bit 4, a",
            "bit 5, b", "bit 5, c", "bit 5, d", "bit 5, e", "bit 5, h", "bit 5, l", "bit 5, [hl]", "bit 5, a",
            "bit 6, b", "bit 6, c", "bit 6, d", "bit 6, e", "bit 6, h", "bit 6, l", "bit 6, [hl]", "bit 6, a",
            "bit 7, b", "bit 7, c", "bit 7, d", "bit 7, e", "bit 7, h", "bit 7, l", "bit 7, [hl]", "bit 7, a",
            "res 0, b", "res 0, c", "res 0, d", "res 0, e", "res 0, h", "res 0, l", "res 0, [hl]", "res 0, a",
            "res 1, b", "res 1, c", "res 1, d", "res 1, e", "res 1, h", "res 1, l", "res 1, [hl]", "res 1, a",
            "res 2, b", "res 2, c", "res 2, d", "res 2, e", "res 2, h", "res 2, l", "res 2, [hl]", "res 2, a",
            "res 3, b", "res 3, c", "res 3, d", "res 3, e", "res 3, h", "res 3, l", "res 3, [hl]", "res 3, a",
            "res 4, b", "res 4, c", "res 4, d", "res 4, e", "res 4, h", "res 4, l", "res 4, [hl]", "res 4, a",
            "res 5, b", "res 5, c", "res 5, d", "res 5, e", "res 5, h", "res 5, l", "res 5, [hl]", "res 5, a",
            "res 6, b", "res 6, c", "res 6, d", "res 6, e", "res 6, h", "res 6, l", "res 6, [hl]", "res 6, a",
            "res 7, b", "res 7, c", "res 7, d", "res 7, e", "res 7, h", "res 7, l", "res 7, [hl]", "res 7, a",
            "set 0, b", "set 0, c", "set 0, d", "set 0, e", "set 0, h", "set 0, l", "set 0, [hl]", "set 0, a",
            "set 1, b", "set 1, c", "set 1, d", "set 1, e", "set 1, h", "set 1, l", "set 1, [hl]", "set 1, a",
            "set 2, b", "set 2, c", "set 2, d", "set 2, e", "set 2, h", "set 2, l", "set 2, [hl]", "set 2, a",
            "set 3, b", "set 3, c", "set 3, d", "set 3, e", "set 3, h", "set 3, l", "set 3, [hl]", "set 3, a",
            "set 4, b", "set 4, c", "set 4, d", "set 4, e", "set 4, h", "set 4, l", "set 4, [hl]", "set 4, a",
            "set 5, b", "set 5, c", "set 5, d", "set 5, e", "set 5, h", "set 5, l", "set 5, [hl]", "set 5, a",
            "set 6, b", "set 6, c", "set 6, d", "set 6, e", "set 6, h", "set 6, l", "set 6, [hl]", "set 6, a",
            "set 7, b", "set 7, c", "set 7, d", "set 7, e", "set 7, h", "set 7, l", "set 7, [hl]", "set 7, a",
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
        int opcode = Byte.toUnsignedInt(gb.readAddressNoCycle(address));

        String opcodeName = opcodeNames[opcode];

        if (opcodeName.equals("PREFIXED")) {
            opcodeName = opcodeNamesPrefixed[opcode];
        } else if (opcodeName.equals("ILLEGAL")) {
            opcodeName = "Undefined opcode ($%02x)".formatted(opcode);
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

