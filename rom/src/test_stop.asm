include "entry.asm"

; main:
    ld a, [rIE]
    ld b, a
    xor a, a
    ld [rIE], a
    ld a, $00
    ld [rP1], a
    stop 
    ld b, a

include "done.asm"