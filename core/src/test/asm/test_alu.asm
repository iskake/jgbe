include "entry.asm"

; main:
    call SetRegisters

    ; INC test
    inc b
    inc c
    inc d
    inc e
    inc h
    inc l
    inc a
    ld hl, _RAM
    inc [hl]

    call SetRegisters
    ; ADD test
    add a, b
    add a, c
    add a, d
    add a, e
    add a, h
    add a, l
    add a, a
    ld hl, _RAM
    add a, [hl]

    ld hl, 0
    add hl, bc
    add hl, de
    add hl, hl

    call SetRegisters
    ; ADC test
    adc b
    adc c
    adc d
    adc e
    adc h
    adc l
    adc a
    ld hl, _RAM
    adc [hl]

    call SetRegisters
    ld a, $50       ; set a for more interesting results
    ; SUB test
    sub b
    sub c
    sub d
    sub e
    sub h
    sub l
    sub a
    ld hl, _RAM
    sub [hl]

    call SetRegisters
    ld a, $50
    ; SBC test
    sbc b
    sbc c
    sbc d
    sbc e
    sbc h
    sbc l
    sbc a
    ld hl, _RAM
    sbc [hl]
    
    ; half-carry test
    ld a, %10001
    sbc a, %1
    sbc a, %1

    call SetRegisters
    ld a, $50
    ; AND test
    and b
    ld a, $50
    and c
    ld a, $50
    and d
    ld a, $50
    and e
    ld a, $50
    and h
    ld a, $50
    and l
    ld a, $50
    and a
    ld a, $50
    ld hl, _RAM
    and [hl]

    call SetRegisters
    ld a, $50
    ; OR test
    or b
    ld a, $50
    or c
    ld a, $50
    or d
    ld a, $50
    or e
    ld a, $50
    or h
    ld a, $50
    or l
    ld a, $50
    or a
    ld a, $50
    ld hl, _RAM
    or [hl]

    ; XOR test
    call SetRegisters
    xor b
    xor c
    xor d
    xor e
    xor h
    xor l
    xor a
    ld hl, _RAM
    xor [hl]

    ; CP test
    call SetRegisters
    cp b
    cp c
    cp d
    cp e
    cp h
    cp l
    cp a
    ld hl, _RAM
    cp [hl]

    ; DEC test
    call SetRegisters
    dec b
    dec c
    dec d
    dec e
    dec h
    dec l
    dec a
    ld hl, _RAM
    dec [hl]; 

    ; HLI
    ld [hli], a
    ld [hl], $45
    ld [hld], a
    ldh [$ff00+$45], a

include "done.asm"

include "setRegisters.asm"

