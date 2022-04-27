
; This file tests how the stop instruction works
; 
; See the comments for full explanation.

include "entry.asm"

main:
    nop 
    nop 
    stop    ; Stop the execution of this program.

            ; Since stop will stop all execution, all lines after the stop
            ; instruction will not run.
    ld a, $42
    ld b, a
    daa

    db $e3  ; `$e3` is an illegal opcode (the program will throw an exception)
            ; again, because stop has already run, this instruction will not run.
            ;
            ; Note that because the assembler does not allow us to write illegal opcodes,
            ; we have to write `db` ("define byte") and the opcode we want instead.