INCLUDE "entry.asm"

; main:
    ld hl, _RAM
    ld a, $00
    ld [hli], a
    ld [hld], a

    call SetRegisters

    ; INC test
    inc b
    inc c
    inc d
    inc e
    inc h
    inc l
    ld hl, _RAM
    inc [hl]
    inc a

    ; ADD test
    call SetRegisters
    add b
    add c
    add d
    add e
    add h
    add l
    ld hl, _RAM
    add [hl]
    add a

    add hl, bc
    add hl, de
    add hl, hl

    ; ADC test
    call SetRegisters
    adc b
    adc c
    adc d
    adc e
    adc h
    adc l
    ld hl, _RAM
    adc [hl]
    adc a

    ; SUB test
    ld a, $50
    sub b
    sub c
    sub d
    sub e
    sub h
    sub l
    sub [hl]
    sub a

    ; SBC test
    call SetRegisters
    sbc b
    sbc c
    sbc d
    sbc e
    sbc h
    sbc l
    ld hl, _RAM
    sbc [hl]
    sbc a
    ld a, %10001
    sbc a, %1
    sbc a, %1

    ; AND test
    call SetRegisters
    and b
    and c
    and d
    and e
    and h
    and l
    ld hl, _RAM
    and [hl]
    and a

    ; OR test
    call SetRegisters
    or b
    or c
    or d
    or e
    or h
    or l
    ld hl, _RAM
    or [hl]
    or a

    ; XOR test
    call SetRegisters
    xor b
    xor c
    xor d
    xor e
    xor h
    xor l
    ld hl, _RAM
    xor [hl]
    xor a

    ; CP test
    call SetRegisters
    cp b
    cp c
    cp d
    cp e
    cp h
    cp l
    ld hl, _RAM
    cp [hl]
    cp a

    ; DEC test
    call SetRegisters
    dec b
    dec c
    dec d
    dec e
    dec h
    dec l
    ld hl, _RAM
    dec [hl]
    dec a

    ; HLI
    ld [hli], a
    ld [hl], $45
    ld [hld], a
    ldh [$ff00+$45], a

include "done.asm"

include "setRegisters.asm"

