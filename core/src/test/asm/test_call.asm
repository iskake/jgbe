include "rst.asm"

include "entry.asm"

; main:
    ld a, $7f   ; 127
    ld b, $00   ; 0
    ld c, $01   ; 1
    ld d, $2e   ; 46
    ld e, $50   ; 80
    ld h, $a0   ; 160
    ld l, $ff   ; 255

    push bc
    push de
    push hl
    push af
    pop bc
    pop de
    pop hl
    pop af

_call_test:
    call _call1

    call nc, _call2

    call nz, _call3

    scf
    call c, _call4

    xor a
    call z, _call5
    nop

.rst_test:
    rst $00
    rst $08

    rst $10
    rst $18

    rst $20
    rst $28

    rst $30
    rst $38

include "done.asm"

_call1:
    ld a, $01
    ret
    jp _failed

_call2:
    ld a, $02
    add a, a
    ret nc
    jp _failed

_call3:
    ld a, $03
    add a, a
    ret nz
    jp _failed

_call4:
    ld a, $57
    ret c
    jp _failed

_call5:
    xor a
    ret z
    jp _failed

_failed:
    db $db  ; Illegal instruction