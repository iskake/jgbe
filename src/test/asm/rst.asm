if !def(RST_H)
def RST_H equ 1

section "rst00", rom0[$00]
    ld a, $00
    add a, a
    ret z

section "rst08", rom0[$08]
    ld a, $08
    add a, a
    ret nz

section "rst10", rom0[$10]
    ld a, $10
    ret

section "rst18", rom0[$18]
    ld a, $18
    add a, a
    ret nc

section "rst20", rom0[$20]
    ld a, $20
    ret

section "rst28", rom0[$28]
    ld a, $28
    ret

section "rst30", rom0[$30]
    ld a, $30
    ret

section "rst38", rom0[$38]
    ld a, $ff
    add a, a
    ret c

endc