include "entry.asm"

; main:
    ld a, $09
    add a, $09
    daa
    
    sub a, $09
    daa
    
    ld a, $10
    add a, $10
    daa 

    ld a, $50
    add a, $50
    daa

    ld a, $99
    sub a, $10
    daa
    
    add a, $11
    daa

.cpl_test:
    ld a, $6B
    cpl
    cpl
    cpl
    ld a, $ff
    cpl
    cpl
    cpl
    ld a, $00
    cpl
    cpl
    cpl
    ld a, $0f
    cpl
    cpl
    cpl

include "done.asm"