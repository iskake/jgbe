include "entry.asm"

; This file contains testing of the daa and cpl instructions.
; 
; See the comments for full explanation.

main:
    ; daa testing
    ld a, $09   ; a = $09
    add a, $09  ; a = $09 + $09 = $12
    daa         ; Decimal adjust a.
                ; The result of the last operation, if we interpret the hex values
                ; $9 as the decimal value 9, we get:
                ;   9 + 9 = 18.
                ; The result of this instruction will therefore be 
                ; stored in the a register as the hex number $18
    
    sub a, $09  ; a = $18 - $09 = $0f
    daa         ; Result of last operation in decimal:
                ;   18 - 9 = 9
                ; So: a = $09
    
    ld a, $10   ; a = $10
    add a, $10  ; a = $10 + $10 = $20
    daa         ; Result of last operation in decimal:
                ;   10 + 10 = 20
                ; So: a = $20. In this case, the result will be the same.

    ld a, $50   ; a = $50
    add a, $50  ; a = $50 + $50 = $a0
    daa         ; Result of last operation in decimal:
                ;   50 + 50 = 100 (overflow) = 00
                ; So: a = $00. Because there are only two digits available
                ; the range of values are 0-99.

    ld a, $99   ; a = $99
    sub a, $10  ; a = $99 - $10 = $89
    daa         ; Result of last operation in decimal:
                ;   99 - 10 = 89
                ; So: a = $89. In this case, the result will be the same.
    
    add a, $11  ; a = $89 + $11 = $9a
    daa         ; Result of last operation in decimal:
                ;   89 + 11 = 100 (overflow) = 00
                ; So: a = $00

.cpl_test:
    ld a, $6b   ; a = $6b
    cpl         ; complement the accumulator (a register):
                ; a = ~a = $94 (%0110_1011 => %1001_0100)
    cpl         ; a = ~a = $6b (%1001_0100 => %0110_1011)
    cpl         ; a = ~a = $94 (%0110_1011 => %1001_0100)

    ld a, $ff   ; a = $ff
    cpl         ;  a = $00 (%1111_1111 => %0000_0000)
    cpl         ;  a = $ff (%0000_0000 => %1111_1111)
    cpl         ;  a = $00 (%1111_1111 => %0000_0000)

    ld a, $00   ; a = $ff
    cpl         ;  a = $ff (%0000_0000 => %1111_1111)
    cpl         ;  a = $00 (%1111_1111 => %0000_0000)
    cpl         ;  a = $ff (%0000_0000 => %1111_1111)

    ld a, $0f   ; a = $0f
    cpl         ;  a = $f0 (%0000_1111 => %1111_0000)
    cpl         ;  a = $0f (%1111_0000 => %0000_1111)
    cpl         ;  a = $f0 (%0000_1111 => %1111_0000)

    stop