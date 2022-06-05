include "entry.asm"

; main:
    ld bc, $1234
    ld de, $5678

    dec sp
    inc sp

    ld hl, $ffd7
    ld sp, hl
    push bc

    ld sp, $fffe
    push de

    ld hl, _RAM
    ld [hl], $99
    ld [_RAM], sp

    ld hl, sp + $80
    
    add hl, sp
    add sp, $80

    ld hl, sp+0
    ld hl, sp+%0111_1111

include "done.asm"
