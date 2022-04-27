INCLUDE "entry.asm"

; This file contains testing of jump instructions (including with conditions.)
; 
; See the comments for full explanation.

main:
    call SetRegisters

    jp .jp1     ; Jump to the label .jp1.
                ; This will set the program counter (pc) to the memory address
                ; of the label `.jp1` (can be thought of as equivalent to
                ; `goto` in programming languages such as C and C#)
                ;
                ; Note: the debugger will not show the name of the label, instead, it will show
                ;       the memory address of the label (notice the output after running this 
                ;       instruction will show: `PC: 0112`, meaning, the program has jumped
                ;       to the memory address $0112.)
    db $db      ; Illegal instruction. This instruction will not run because we previously jumped
                ; to the .jp1 label, and consequently, jumped over this instruction.

.jp1:
    inc b       ; Increment the b register. Notice that no flags are set (in debugger: `Flags: ----`)
    jp z, failed; The condition z checks if the Z flag is set. 
                ; Since it is not, the program continues to the next line.
    jp nz, .jp2 ; nz -> not zero
    db $db  ; Illegal instruction

.jp2:
    dec b           ; decrement the b register (=> b = 0), 
    jp nz, failed   ; The condition z checks 
    jp z, .jp3
    db $db  ; Illegal instruction

.jp3:
    or a
    jp c, failed
    jp nc, .jp4
    db $db  ; Illegal instruction

.jp4:
    add a, l
    jp nc, failed
    jp c, .jp5
    db $db  ; Illegal instruction

.jr1:
    jr c, .jr2

.jp5:
    ld hl, .jp6
    jp hl
    db $db  ; Illegal instruction

.jp6:
    jr .jr1
    db $db  ; Illegal instruction

.jr2:
    nop
    call SetRegisters

    stop

include "setRegisters.asm"

failed:
    db $db  ; Illegal instruction