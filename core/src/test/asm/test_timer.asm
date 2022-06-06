section "timer_interrupt", rom0[$50]
    jp timer_handle

include "entry.asm"

; main:
    ld a, TACF_START | TACF_4KHZ
    ld [rTAC], a

    ld a, $fd
    ld [rTMA], a

    ld a, IEF_TIMER
    ld [rIE], a
    ei
    ld a, $42

wait_tima:
    cp b
    jr nz wait_tima

    di
include "done.asm"

timer_handle:
    ld b, $42
    reti