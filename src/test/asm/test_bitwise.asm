include "entry.asm"

; This file contains testing of bitwise instructions.
; 
; See the comments for full explanation.

main:
    call SetRegisters

    rlc b   ; rotate b left
    rlca    ; rotate a left
    rrc d   ; rotate d right
    rrca    ; rotate a right
    rl l    ; rotate l left through carry
    rla     ; rotate a left through carry
    rr h    ; rotate h right through carry
    rra     ; rotate a right through carry
    sla c   ; shift c left arithmetically
    sra d   ; shift d right arithmetically
    swap e  ; swap e: lower and higher 4 bits are swapped
    srl a   ; shift a right logically
    ; See https://rgbds.gbdev.io/docs/v0.5.2/gbz80.7#Bit_Shift_Instructions
    ; for an explanation of each instruction.

    ld hl, $c000
    ld [hl], $b5    ; Memory address $c000 is set to $b5

    ; bit shifting with the value at memory address $c000
    rlc [hl]
    rrc [hl]
    rl [hl]
    rr [hl]
    sla [hl]
    sra [hl]
    swap [hl]
    srl [hl]

    call SetRegisters
    bit 1, a ; check if bit 1 is set in the a register
             ; if it is not, the zero flag is set
    res 2, d ; reset bit 2 in the d register
    set 7, b ; set bit 7 in the b register

    ld hl, $c000
    ld [hl], $b5

    ; bit check/set/reset with the value at memory address $c000
    bit 2, [hl]
    bit 3, [hl]
    res 3, [hl]
    set 3, [hl]

    stop

include "setRegisters.asm"