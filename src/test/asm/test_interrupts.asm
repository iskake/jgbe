
SECTION "vblank", ROM0[$40]
    ld c, IEF_VBLANK
    reti

include "entry.asm"

; main:
    ld b, $00
    ld c, $00
    ld a, IEF_VBLANK
    ld [rIE], a
    ei
wait:
    inc b
    cp c
    jp nz, wait
    di

include "done.asm"