package tland.gb;

import tland.gb.Inst.*;
import static tland.gb.Registers.RegisterIndex;

public enum Opcodes {
    // TODO: Fix [hl] instructions
    // TODO: Move instructons to array instead
    NOP(0x00, new NOP()),
    
    LD_B_n8(0x06, new LD_r8_n8("ld b, `n8`", RegisterIndex.B)),

    LD_C_n8(0x0E, new LD_r8_n8("ld c, `n8`", RegisterIndex.C)),

    LD_D_n8(0x16, new LD_r8_n8("ld d, `n8`", RegisterIndex.D)),

    LD_E_n8(0x1E, new LD_r8_n8("ld e, `n8`", RegisterIndex.E)),
    
    LD_H_n8(0x26, new LD_r8_n8("ld h, `n8`", RegisterIndex.H)),
    
    LD_L_n8(0x2E, new LD_r8_n8("ld l, `n8`", RegisterIndex.L)),
    
    LD_B_B(0x40, new LD_r8_r8("ld b, b", RegisterIndex.B, RegisterIndex.B)),
    LD_B_C(0x41, new LD_r8_r8("ld b, c", RegisterIndex.B, RegisterIndex.C)),
    LD_B_D(0x42, new LD_r8_r8("ld b, d", RegisterIndex.B, RegisterIndex.D)),
    LD_B_E(0x43, new LD_r8_r8("ld b, e", RegisterIndex.B, RegisterIndex.E)),
    LD_B_H(0x44, new LD_r8_r8("ld b, h", RegisterIndex.B, RegisterIndex.H)),
    LD_B_L(0x45, new LD_r8_r8("ld b, l", RegisterIndex.B, RegisterIndex.L)),
    LD_B_HL(0x46, new LD_r8_r8("ld b, [hl]", RegisterIndex.B, RegisterIndex.HL)),

    LD_C_B(0x48, new LD_r8_r8("ld c, b", RegisterIndex.C, RegisterIndex.B)),
    LD_C_C(0x49, new LD_r8_r8("ld c, c", RegisterIndex.C, RegisterIndex.C)),
    LD_C_D(0x4A, new LD_r8_r8("ld c, d", RegisterIndex.C, RegisterIndex.D)),
    LD_C_E(0x4B, new LD_r8_r8("ld c, e", RegisterIndex.C, RegisterIndex.E)),
    LD_C_H(0x4C, new LD_r8_r8("ld c, h", RegisterIndex.C, RegisterIndex.H)),
    LD_C_L(0x4D, new LD_r8_r8("ld c, l", RegisterIndex.C, RegisterIndex.L)),
    LD_C_HL(0x4E, new LD_r8_r8("ld c, [hl]", RegisterIndex.C, RegisterIndex.HL)),

    LD_D_B(0x50, new LD_r8_r8("ld d, b", RegisterIndex.D, RegisterIndex.B)),
    LD_D_C(0x51, new LD_r8_r8("ld d, c", RegisterIndex.D, RegisterIndex.C)),
    LD_D_D(0x52, new LD_r8_r8("ld d, d", RegisterIndex.D, RegisterIndex.D)),
    LD_D_E(0x53, new LD_r8_r8("ld d, e", RegisterIndex.D, RegisterIndex.E)),
    LD_D_H(0x54, new LD_r8_r8("ld d, h", RegisterIndex.D, RegisterIndex.H)),
    LD_D_L(0x55, new LD_r8_r8("ld d, l", RegisterIndex.D, RegisterIndex.L)),
    LD_D_HL(0x56, new LD_r8_r8("ld d, [hl]", RegisterIndex.D, RegisterIndex.HL)),

    LD_E_B(0x58, new LD_r8_r8("ld e, b", RegisterIndex.E, RegisterIndex.B)),
    LD_E_C(0x59, new LD_r8_r8("ld e, c", RegisterIndex.E, RegisterIndex.C)),
    LD_E_D(0x5A, new LD_r8_r8("ld e, d", RegisterIndex.E, RegisterIndex.D)),
    LD_E_E(0x5B, new LD_r8_r8("ld e, e", RegisterIndex.E, RegisterIndex.E)),
    LD_E_H(0x5C, new LD_r8_r8("ld e, h", RegisterIndex.E, RegisterIndex.H)),
    LD_E_L(0x5D, new LD_r8_r8("ld e, l", RegisterIndex.E, RegisterIndex.L)),
    LD_E_HL(0x5E, new LD_r8_r8("ld e, [hl]", RegisterIndex.E, RegisterIndex.HL)),

    LD_H_B(0x60, new LD_r8_r8("ld h, b", RegisterIndex.H, RegisterIndex.B)),
    LD_H_C(0x61, new LD_r8_r8("ld h, c", RegisterIndex.H, RegisterIndex.C)),
    LD_H_D(0x62, new LD_r8_r8("ld h, d", RegisterIndex.H, RegisterIndex.D)),
    LD_H_E(0x63, new LD_r8_r8("ld h, e", RegisterIndex.H, RegisterIndex.E)),
    LD_H_H(0x64, new LD_r8_r8("ld h, h", RegisterIndex.H, RegisterIndex.H)),
    LD_H_L(0x65, new LD_r8_r8("ld h, l", RegisterIndex.H, RegisterIndex.L)),
    LD_H_HL(0x66, new LD_r8_r8("ld h, [hl]", RegisterIndex.H, RegisterIndex.HL)),

    LD_L_B(0x68, new LD_r8_r8("ld l, b", RegisterIndex.L, RegisterIndex.B)),
    LD_L_C(0x69, new LD_r8_r8("ld l, c", RegisterIndex.L, RegisterIndex.C)),
    LD_L_D(0x6A, new LD_r8_r8("ld l, d", RegisterIndex.L, RegisterIndex.D)),
    LD_L_E(0x6B, new LD_r8_r8("ld l, e", RegisterIndex.L, RegisterIndex.E)),
    LD_L_H(0x6C, new LD_r8_r8("ld l, h", RegisterIndex.L, RegisterIndex.H)),
    LD_L_L(0x6D, new LD_r8_r8("ld l, l", RegisterIndex.L, RegisterIndex.L)),
    LD_L_HL(0x6E, new LD_r8_r8("ld l, [hl]", RegisterIndex.L, RegisterIndex.HL)),

    LD_HL_B(0x70, new LD_r8_r8("ld [hl], b", RegisterIndex.HL, RegisterIndex.B)),
    LD_HL_C(0x71, new LD_r8_r8("ld [hl], c", RegisterIndex.HL, RegisterIndex.C)),
    LD_HL_D(0x72, new LD_r8_r8("ld [hl], d", RegisterIndex.HL, RegisterIndex.D)),
    LD_HL_E(0x73, new LD_r8_r8("ld [hl], e", RegisterIndex.HL, RegisterIndex.E)),
    LD_HL_H(0x74, new LD_r8_r8("ld [hl], h", RegisterIndex.HL, RegisterIndex.H)),
    LD_HL_L(0x75, new LD_r8_r8("ld [hl], l", RegisterIndex.HL, RegisterIndex.L)),
    LD_HL_HL(0x76, new LD_r8_r8("ld [hl], [hl]", RegisterIndex.HL, RegisterIndex.HL)),

    LD_A_B(0x78, new LD_r8_r8("ld a, b", RegisterIndex.A, RegisterIndex.B)),
    LD_A_C(0x79, new LD_r8_r8("ld a, c", RegisterIndex.A, RegisterIndex.C)),
    LD_A_D(0x7A, new LD_r8_r8("ld a, d", RegisterIndex.A, RegisterIndex.D)),
    LD_A_E(0x7B, new LD_r8_r8("ld a, e", RegisterIndex.A, RegisterIndex.E)),
    LD_A_H(0x7C, new LD_r8_r8("ld a, h", RegisterIndex.A, RegisterIndex.H)),
    LD_A_L(0x7D, new LD_r8_r8("ld a, l", RegisterIndex.A, RegisterIndex.L)),
    LD_A_HL(0x7E, new LD_r8_r8("ld a, [hl]", RegisterIndex.A, RegisterIndex.HL)),
    LD_A_A(0x7F, new LD_r8_r8("ld a, a", RegisterIndex.A, RegisterIndex.A)),
    ;

    int index;
    Instruction opcode;
    Opcodes(int index, Instruction opcode) {
        this.index = index;
        this.opcode = opcode;
    }

}
