INCLUDE "entry.asm"

; main:
    ld a, $50
    ld b, $00
    ld c, $01
    ld d, $2e
    ld e, $50
    ld h, $a0
    ld l, $ff

    jp .jp1
    db $db  ; Illegal instruction

.jp1:
    inc b
    jp z, failed
    jp nz, .jp2
    db $db  ; Illegal instruction

.jp2:
    dec b
    jp nz, failed
    jp z, .jp3
    db $db  ; Illegal instruction

.jp3:
    or a
    jp c, failed
    jp nc, .jp4
    db $db  ; Illegal instruction

.jp4:
    add a, l
    jp nc, failed
    jp c, .jp5
    db $db  ; Illegal instruction

.jr1:
    jr c, .jr2

.jp5:
    ld hl, .jp6
    jp hl
    db $db  ; Illegal instruction

.jp6:
    jr .jr1
    db $db  ; Illegal instruction

.jr2:
    nop
    call SetRegisters

include "done.asm"

include "setRegisters.asm"

failed:
    db $db  ; Illegal instruction