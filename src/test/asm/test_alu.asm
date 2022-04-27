

; This file contains testing of arithmetic instructions.
; 
; See the comments for full explanation.

include "entry.asm" ; note: `include` is NOT included in the CLI interpreter.
                    ;       In RGBDS assembly (tools needed to build these files),
                    ;       `include` includes all code in the specified file in this file.
                    ;       Can be thought of as equivalent to `#include` in C (the programming language.)

main:
    ; Typing `n` into the debugger will jump over this instruction
    ; to the next line (the registers will still be set).

    call SetRegisters   ; Call the function `SetRegisters`, 
                        ; defined in the setRegisters.asm file
                        ; this will set the registers to the following:
                        ;   a = 127 = $7f (hex)
                        ;   b = 0   = $00 (hex) (note: decrementing will underflow)
                        ;   c = 1   = $01 (hex)
                        ;   d = 46  = $2e (hex)
                        ;   e = 80  = $50 (hex)
                        ;   h = 160 = $a0 (hex)
                        ;   l = 255 = $ff (hex) (note: incrementing will overflow)
                        ; see the `test_call.asm` file for more information

    ; INC test
    ; The inc instruction increments the specified register by one.
    ; If the new value is more than 255, it will overflow and set the register
    ; value to 0, and the carry flag (flag `C`) is set.
    inc b   ; b + 1 = 0   + 1 = 1
    inc c   ; c + 1 = 1   + 1 = 2
    inc d   ; d + 1 = 46  + 1 = 47
    inc e   ; e + 1 = 80  + 1 = 81
    inc h   ; h + 1 = 160 + 1 = 161
    inc l   ; l + 1 = 255 + 1 = 0 (overflow)
    inc a   ; a + 1 = 127 + 1 = 128
    ld hl, RAM
    inc [hl]; (memory at $8000) + 1 = 0 + 1 = 1

    call SetRegisters
    ; ADD test
    ; The add instruction adds the value in the second register
    ; to the value in the first register, and stores the result in
    ; the first regiser.
    add a, b   ; a + b = 127 + 0 = 127
    add a, c   ; a + c = 127 + 1 = 128
    add a, d   ; a + d = 128 + 46 = 174
    add a, e   ; a + e = 174 + 80 = 254
    add a, h   ; a + h = 254 + 160 = (414 % 256) = 158 (overflow, carry flag (flag `C`) is set)
    add a, l   ; a + l = 158 + 255 = (413 % 256) = 157 (overflow, --||--)
    add a, a   ; a + a = 157 + 157 = (314 % 256) = 58  (overflow, --||--)
    ld hl, RAM
    add a, [hl]; a + (mem at $8000 = 1) = 59

    ld hl, 0
    add hl, bc ; hl + bc = $0000 + $0001 = $0001
    add hl, de ; hl + bc = $0001 + $2e50 = $2e51
    add hl, hl ; hl + bc = $2e51 + $2e51 = $5ca2

    call SetRegisters
    ; ADC test
    ; The adc instruction adds the value in the second register
    ; to the value in the a register PLUS 1 if the carry flag ('flag c')
    ; is set, and stores the result in the a regiser.
    ; (note: there is no adc for hl, as opposed to `add`.)
    adc b   ; a + b = 127 + 0 + 0 = 127
    adc c   ; a + c = 127 + 1 + 0 = 128
    adc d   ; a + d = 128 + 46 + 0 = 174
    adc e   ; a + e = 174 + 80 + 0 = 254
    adc h   ; a + h = 254 + 160 + 0 = (414 % 256) = 158 (note: overflow carry flag is set!)
    adc l   ; a + l = 158 + 255 + 1 = (414 % 256) = 158 (--||--)
    adc a   ; a + a = 158 + 158 + 1 = (317 % 256) = 61  (--||--)
    ld hl, RAM
    adc [hl]; a + (mem at $8000 = 1) + 1 = 63

    call SetRegisters
    ld a, $50       ; set a for more interesting results
    ; SUB test
    ; The sub instruction subtracts the value in the second register
    ; from the value in the a register
    sub b   ; a - b = $50 - $00 = $50 (flags set: n)
    sub c   ; a - c = $50 - $01 = $4f (flags set: n)
    sub d   ; a - d = $4f - $2e = $21 (flags set: n)
    sub e   ; a - e = $21 - $50 = $d1 (flags set: n,c) (underflow -> carry flag (flag c))
    sub h   ; a - h = $d1 - $a0 = $31 (flags set: n)
    sub l   ; a - l = $31 - $ff = $32 (flags set: n,c) (underflow)
    sub a   ; a - a = $32 - $32 = $00 (flags set: n,z) (the result is zero -> the zero flag (flag z) is set)
    ld hl, RAM
    sub [hl]; a - (mem at $8000 = 1) = $ff (flags set: n,c) (underflow)

    call SetRegisters
    ld a, $50       ; set a for more interesting results
    ; SBC test
    ; The sub instruction subtracts the value in the second register
    ; AND 1 if the carry flag (flag c) is set from the value in the a register
    sbc b   ; a - b = $50 - $00 - $1 = $4f (flags set: n)
    sbc c   ; a - c = $4f - $01 - $0 = $4e (flags set: n)
    sbc d   ; a - d = $4e - $2e - $0 = $20 (flags set: n)
    sbc e   ; a - e = $20 - $50 - $0 = $d0 (flags set: n,c) (underflow -> carry flag (flag c))
    sbc h   ; a - h = $d0 - $a0 - $1 = $2f (flags set: n)
    sbc l   ; a - l = $2f - $ff - $1 = $30 (flags set: n,c) (underflow)
    sbc a   ; a - a = $30 - $30 - $1 = $ff (flags set: n,c) (underflow)
    ld hl, RAM
    sbc [hl]; a - (mem at $8000 = 1) - 1 = $fd (flags set: n)
    
    ; half-carry test (not important for instructions other than `daa`)
    ; (Note: because the H flag is only important for a single instruction,
    ; it has not been noted in these comments when it has been set.)
    ld a, %10001 ; a = $11
    sbc a, %1    ; a - 1 = $10
    sbc a, %1    ; a - 1 = $0f (half-carry flag is set (flag H))

    call SetRegisters
    ld a, $50
    ; AND test
    ; The and instruction takes the bitwise and of value in the specified register
    ; with the value in a and stores the result in a.
    and b   ; a ^ b = 
    ld a, $50   ; note: we set a to $50 after every `and` instruction
                ;       so that the result will not just be $00 after
                ;       a while...
    and c   ; a ^ c = 
    ld a, $50
    and d   ; a ^ d = 
    ld a, $50
    and e   ; a ^ e = 
    ld a, $50
    and h   ; a ^ h = 
    ld a, $50
    and l   ; a ^ l = 
    ld a, $50
    and a   ; a ^ a = 
    ld a, $50
    ld hl, RAM
    and [hl]

    call SetRegisters
    ld a, $50
    ; OR test
    ; The or instruction takes the bitwise or of the value in the specified register
    ; with the value in a and stores the result in a.
    or b
    ld a, $50   ; note: we set a to $50 after every `or` instruction
                ;       so that the result will not just be $ff after
                ;       a while...
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
    ld hl, RAM
    or [hl]

    ; XOR test
    ; The xor instruction takes teh bitwise xor of the value in the specified register
    ; with the value in a and stores the result in a.
    call SetRegisters
    xor b
    xor c
    xor d
    xor e
    xor h
    xor l
    xor a
    ld hl, RAM
    xor [hl]

    ; CP test
    ; The cp or, ComPare instruction is equivalent to the dec instruction, but without storing 
    ; the result in the a register, only the corresponding flags are set instead.
    call SetRegisters
    cp b
    cp c
    cp d
    cp e
    cp h
    cp l
    cp a
    ld hl, RAM
    cp [hl]

    ; DEC test
    ; The dec instruction decrements the specified register by one.
    ; If the new value is less than 0, it will underflow and set the register
    ; value to 255 ($ff), and the carry flag (flag `C`) is set.
    call SetRegisters
    dec b   ; b - 1 = 0   - 1 = 255 (underflow)
    dec c   ; c - 1 = 1   - 1 = 0
    dec d   ; d - 1 = 46  - 1 = 45
    dec e   ; e - 1 = 80  - 1 = 79
    dec h   ; h - 1 = 160 - 1 = 159
    dec l   ; l - 1 = 255 - 1 = 254
    dec a   ; a - 1 = 127 - 1 = 126
    ld hl, RAM
    dec [hl]; 

    ; HLI
    ; The 'hli instructions' will do the normal operations (such as ld [hl], a), but also
    ; incrementing hl after the operation.
    ld [hli], a
    ld [hl], $45
    ld [hld], a
    ldh [$ff00+$45], a

    stop    ; Stop program execution. Everything after this instruction will not run
            ; (that is unless the program 'jumps to' or 'calls' an address after this
            ; instruction.)

include "setRegisters.asm"  ; All of the contents of the 'setRegisters.asm' file is included
                            ; into the program when we assemble this program.
                            ; (needs an assembler)

