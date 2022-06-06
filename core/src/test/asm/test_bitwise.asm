include "entry.asm"

; main:
    call SetRegisters

    rlc b
    rlca
    rrc d
    rrca
    rl l
    rla
    rr h
    rra
    sla c
    sra d
    swap e
    srl a

    ld hl, _RAM
    ld [hl], $b5

    rlc [hl]
    rrc [hl]
    rl [hl]
    rr [hl]
    sla [hl]
    sra [hl]
    swap [hl]
    srl [hl]

    call SetRegisters
    bit 1, a
    res 2, d
    set 7, b

    ld hl, _RAM
    ld [hl], $b5

    bit 2, [hl]
    bit 3, [hl]
    res 3, [hl]
    set 3, [hl]

include "done.asm"

include "setRegisters.asm"