

; This file contains testing of function calls.
; 
; See the comments for full explanation.

section "rst00", rom0[$00]
    ld a, $00
    add a, a    ; a = $0+$0 = $0 (z flag is set)
    ret z

section "rst08", rom0[$08]
    ld a, $08
    add a, a    ; a = $8+$8 = $10 (z flag is not set)
    ret nz

section "rst10", rom0[$10]
    ld a, $10   ; a = $10
    ret         ; return without condition

section "rst18", rom0[$18]
    ld a, $18
    add a, a    ; a = $18+$18 = $30 (c flag is not set)
    ret nc

section "rst20", rom0[$20]
    ld a, $20    ; a = 10
    ret         ; return without condition

section "rst28", rom0[$28]
    ld a, $28
    ret         ; return without condition

section "rst30", rom0[$30]
    ld a, $30
    ret         ; return without condition

section "rst38", rom0[$38]
    ld a, $ff
    add a, a    ; a = $ff+$ff = $fe (overflow, c flag is set)
    ret c

include "entry.asm"

main:

_call_test:
    call _call1 ; The 'call' instruction calls the address of `_call1` by:
                ; - pushing the current value of the program counter (PC) to the stack
                ; - Jumping to the specified address (in this calse, to `_call1`)
                ; 
                ; The end of the function call is expected to have a 'ret' instruction
                ; so that the PC will be 'pop'-ed from the stack and the program continues
                ; from after the call.

    call nc, _call2 ; nc -> call if the carry flag (C) is not set

    call nz, _call3 ; nz -> call if the zero flag (Z) is not set

    scf             ; Set Carry Flag
    call c, _call4  ; c  -> call if the carry flag is set.

    xor a           ; xor a with a -> a is set to 0 (since xor with same bits are reset)
    call z, _call5  ; z  -> call if the zero flag is set
    nop

.rst_test:
    rst $00 ; Call address $00 (see `section "rst00", rom0[$00]` $00 above)
    rst $08 ; Call address $08

    rst $10 ; Call address $10 (see section above)
    rst $18 ; Call address $18 (see section above)

    rst $20 ; Call address $20 (see section above)
    rst $28 ; Call address $28 (see section above)

    rst $30 ; Call address $30 (see section above)
    rst $38 ; Call address $38 (see section above)

    stop

_call1:
    ld a, $01
    ret     ; The 'ret' instruction attempt to return from a function
            ; by 'popping' the short value stored at the stack poiner (SP)
            ; and storing it in the PC.
            ; (in this case, no other values have been pushed to the stack,
            ; so the function returns normally.)
    jp _failed

_call2:
    ld a, $02
    add a, a; Add a to a -> 2 + 2 = 4 (carry flag is not set because no overflow)
    ret nc  ; nc -> return if the carry flag is not set.
    jp _failed

_call3:
    ld a, $03
    add a, a; add a to a -> 3 + 3 = 6 (zero flag is not set because the result is not 0)
    ret nz  ; nc -> return if the zero flag is not set.
    jp _failed

_call4:
    ld a, $57; note: flag values are not reset/modified when loading values into registers.
    ret c   ; c  -> return if the carry flag is set.
    jp _failed

_call5:
    xor a   ; xor a with a -> a is set to 0 (see explanation above)
    ret z   ; z  -> return if the zero flag is set.
    jp _failed

_failed:
    db $db  ; Illegal instruction