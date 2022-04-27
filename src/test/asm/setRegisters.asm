; Function to set each register to a specific value
SetRegisters:
    ld a, 127   ; $7f
    ld b, 0     ; $00
    ld c, 1     ; $01
    ld d, 46    ; $2e
    ld e, 80    ; $50
    ld h, 160   ; $a0
    ld l, 255   ; $ff
    ret         ; return from the function