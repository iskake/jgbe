
; This file contains all instructions for manipulating the stack pointer
; 
; See the comments for full explanation.

include "entry.asm"

main:
    ld bc, $1234
    ld de, $5678

    dec sp          ; decrease the stack pointer by 1, the new value will be $fffd
                    ; (note: sp means stack pointer)
    inc sp          ; increase the stack pointer by 1, the new value will be $fffe

    ld hl, $ffd7
    ld sp, hl       ; set the stack pointer to the value in hl (sp is now $ffd7)

    push bc         ; 'push' the value in bc to the stack pointer.
                    ; this decrements sp, (sp = $ffd6)
                    ; writes the value in b to the address pointed to by the sp
                    ; (the address $ffd6 now holds a value of $12),
                    ; decrements sp again, (sp = $ffd5)
                    ; and finally writes the value in c to the address 
                    ; pointed to by the sp (the address $ffd5 now holds a value of $34),

    ld sp, $fffe    ; set the stack pointer to $fffe

    push de         ; 'push' the value in de to the stack pointer, the same procedure
                    ; as last time will happen again for de:
                    ;   dec sp  => sp = $fffd
                    ;   memory at sp is set to d => memory at $fffd = 56
                    ;   dec sp  => sp = $fffc
                    ;   memory at sp is set to e => memory at $fffc = 78

    ld hl, RAM      ; hl = $8000
    ld [hl], $99    ; load $99 into the memory address pointed to by hl (memory address $8000 = $99)
    ld [RAM], sp    ; store the low byte of sp ($fffc -> $fc) at 
                    ; the memory address $8000 (memory $8000 = $fc),
                    ; then store the high byte of sp ($fffc -> $ff) at 
                    ; the memory address $8000 + 1 (memory $8001 = $ff),

    ld hl, sp + $fd ; load the value of sp plus the _signed_ value $80 (-128 decimal)
                    ; into hl (sp+$ff = $fffc - 3 = $fff9)
    
    add hl, sp      ; add the value in sp to hl (hl + sp = $fff9 + $fffc = $1_fff5 = $fff5 (overflow))
    add sp, $80     ; add the signed value $80 (-128) to sp ($fffc - 128 = $ff7c)

    ld a, $7f   ; 127
    ld b, $00   ; 0
    ld c, $01   ; 1
    ld d, $2e   ; 46
    ld e, $50   ; 80
    ld h, $a0   ; 160
    ld l, $ff   ; 255
    ld sp, $fffe

    ; Some more stack operations for testing the stack pointer
    ; 
    ; In the debugger: type `x` and then the value of the stack pointer,
    ; shown in the debugger as 'SP' prefixed with either '0x' or '$'.
    ; For example: `SP: fffe` type: `x $fffe` or `x 0xfffe` to print
    ; the memory values at the stack pointer (note: shorts are stored
    ; with little-endian, so a value of $1234 is stored as `34 12` in memory).
    push bc
    push de
    push hl
    push af
    pop bc
    pop de
    pop hl
    pop af

    stop
