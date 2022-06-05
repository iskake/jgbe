section "vblank", ROM0[$40]
    ld b, $12
    reti

section "stat", rom0[$48]
    ld b, $34
    reti

section "timer", rom0[$50]
    ld b, $56
    reti

section "serial", rom0[$58]
    ld b, $78
    reti

section "joypad", rom0[$60]
    ld b, $90
    reti

include "entry.asm"

; main:
start_vblank:
    ld a, IEF_VBLANK
    ld [rIE], a
    ei
    ld a, $12

wait_vblank:
    inc b
    cp c
    jp nz, wait_vblank
    di
    xor a
    ld [rIE], a

start_timer:
    ld a, TACF_START | TACF_4KHZ
    ld [rTAC], a

    ld a, $fd
    ld [rTMA], a

    ld a, IEF_TIMER
    ld [rIE], a
    ei
    ld a, $34

wait_tima:
    cp b
    jr nz, wait_tima

    di

include "done.asm"