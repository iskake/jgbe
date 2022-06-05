if !def(SET_REGISTERS)
def SET_REGISTERS equ 1

SetRegisters::
    ld a, $7f   ; 127
    ld b, $00   ; 0
    ld c, $01   ; 1
    ld d, $2e   ; 46
    ld e, $50   ; 80
    ld h, $a0   ; 160
    ld l, $ff   ; 255
    ret

endc    ; SET_REGISTERS