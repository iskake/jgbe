section "vlbank_interrupt", rom0[$40]
    jp vblank_handle

include "entry.asm"

wait_vblank:
    ld a, [rLY]
    cp $90
    jp c, wait_vblank

    ld a, 0
    ld [rLCDC], a


    ld hl, _OAMRAM
    ld bc, OAM_COUNT * 4

clear_vram:
    ; bc: size
    ; hl: dest
    ld a, 0
    ld [hli], a
    dec bc
    ld a, b
    or a, c
    jp nz, clear_vram


    ld de, tiles_start
    ld bc, tiles_end - tiles_start
    ld hl, _VRAM8000

copy_tiles:
    ; bc: size
    ; de: src start
    ; hl: dest
    ld a, [de]
    ld [hli], a
    inc de
    dec bc
    ld a, b
    or a, c
    jp nz, copy_tiles

set_oam_obj0:
    ld hl, _OAMRAM
    ld a, (SCRN_Y / 2) + 8
    ld [hli], a
    ld a, (SCRN_X / 2) + 8
    ld [hli], a
    ld a, 0
    ld [hli], a
    ld [hli], a

set_oam_obj1:
    ld hl, _OAMRAM + 4
    ld a, (SCRN_Y / 2) + 8
    ld [hli], a
    ld a, (SCRN_X / 2) + 8
    ld [hli], a
    ld a, 1
    ld [hli], a
    ld a, 0
    ld [hli], a

set_oam_obj2:
    ld hl, _OAMRAM + 8
    ld a, (SCRN_Y / 2) + 8
    ld [hli], a
    ld a, (SCRN_X / 2) + 8
    ld [hli], a
    ld a, 2
    ld [hli], a
    ld a, 0
    ld [hli], a

start_lcd:
    ld a, LCDCF_ON | LCDCF_OBJON
    ld [rLCDC], a

    ld a, %11100100
    ld [rOBP0], a

enable_interrupts:
    ld a, IEF_VBLANK
    ldh [rIE], a
    ei

include "done.asm"

tiles_start:
    db $bd,$7e, $42,$81, $a5,$a5, $a5,$a5, $81,$81, $99,$99, $42,$81, $bd,$7e,
    db $bd,$7e, $42,$81, $81,$81, $e7,$e7, $81,$81, $99,$99, $42,$81, $bd,$7e,
    db $bd,$7e, $40,$81, $a5,$a5, $a5,$a5, $81,$81, $a5,$a5, $5a,$99, $bd,$7e,
tiles_end:

vblank_handle:
    ld hl, _OAMRAM + 1
    inc [hl]
    ld hl, _OAMRAM + 4
    inc [hl]
    ld hl, _OAMRAM + 8
    inc [hl]
    inc l
    inc [hl]
    reti