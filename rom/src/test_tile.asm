section "vlbank_interrupt", rom0[$40]
    jp vblank_handle

include "entry.asm"

def OBJ0 equ _OAMRAM
def OBJ1 equ _OAMRAM + 4
def OBJ2 equ _OAMRAM + 8

def OBJ_Y equ 0
def OBJ_X equ 1
def OBJ_TILE_INDEX equ 2
def OBJ_ATTR equ 3

wait_vblank:
    ld a, [rLY]
    cp $90
    jp c, wait_vblank

disable_lcd:
    ld a, 0
    ld [rLCDC], a

clear_oam:
    ld hl, _OAMRAM
    ld bc, OAM_COUNT * 4
    call memset

load_tiles:
    ld de, tiles_start
    ld bc, tiles_end - tiles_start
    ld hl, _VRAM
    call memcpy

set_oam_obj0:
    ld hl, OBJ0
    ld a, (SCRN_Y / 2) + 8
    ld [hli], a
    ld a, (SCRN_X / 2) + 8
    ld [hli], a
    ld a, 0
    ld [hli], a
    ld [hli], a

set_oam_obj1:
    ld hl, OBJ1
    ld a, (SCRN_Y / 2) + 8
    ld [hli], a
    ld a, (SCRN_X / 2) + 8
    ld [hli], a
    ld a, 1
    ld [hli], a
    ld a, 0
    ld [hli], a

set_oam_obj2:
    ld hl, OBJ2
    ld a, (SCRN_Y / 2) + 8
    ld [hli], a
    ld a, (SCRN_X / 2) + 8
    ld [hli], a
    ld a, 2
    ld [hli], a
    ld a, 0
    ld [hli], a

set_tilemap:
    ld de, bg_start
    ld bc, bg_end - bg_start
    ld hl, _VRAM + $1000
    call memcpy

    ; set tile 1
    ld de, tiles_start
    ld bc, tile1_end - tiles_start
    ld hl, _VRAM + $1000 + 16
    call memcpy

clear_tilemap:
    ld a, 0
    ld hl, _SCRN0
    ld bc, _SCRN1 - _SCRN0
    call memset

    ; Set tile 1 in tilemap to tile 1
    ld a, 1
    ld b, 32
    ld hl, _SCRN0
set_line1:
    ld [hli], a
    dec b
    jr nz, set_line1


start_lcd:
    ld a, LCDCF_ON | LCDCF_OBJON | LCDCF_BGON
    ld [rLCDC], a

    ld a, %11100100
    ld [rBGP], a
    ld [rOBP0], a

enable_interrupts:
    ld a, IEF_VBLANK
    ldh [rIE], a
    ei

include "done.asm"

tiles_start:
    db $bd,$7e, $42,$81, $a5,$a5, $a5,$a5, $81,$81, $99,$99, $42,$81, $bd,$7e,
tile1_end:
    db $bd,$7e, $42,$81, $81,$81, $e7,$e7, $81,$81, $99,$99, $42,$81, $bd,$7e,
    db $bd,$7e, $40,$81, $a5,$a5, $a5,$a5, $81,$81, $a5,$a5, $5a,$99, $bd,$7e,
tiles_end:

bg_start:
    db $87,$00, $0f,$00, $1e,$00, $3c,$00, $78,$00, $f0,$00, $e1,$00, $c3,$00,
bg_end:

vblank_handle:
    ld hl, OBJ0 + OBJ_X  ; hl = obj0.x
    inc [hl]
    ld hl, OBJ1 + OBJ_Y  ; hl = obj1.y
    inc [hl]
    ld hl, OBJ2 + OBJ_Y  ; hl = obj2.y
    inc [hl]
    inc l                ; hl = obj2.x
    inc [hl]

    inc c
    bit 0, c
    jr nz, vblank_finish

    ld hl, rSCX
    inc [hl]


vblank_finish:
    reti

memset:
    ; bc: size
    ; hl: dest
    ld a, 0
    ld [hli], a
    dec bc
    ld a, b
    or a, c
    jr nz, memset
    ret

memcpy:
    ; bc: size
    ; de: src start
    ; hl: dest
    ld a, [de]
    ld [hli], a
    inc de
    dec bc
    ld a, b
    or a, c
    jr nz, memcpy
    ret