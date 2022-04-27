; Define RAM to mean $8000
; we will use RAM as a pointer (using [hl]) to write to.
def RAM equ $8000

; `section` sets the starting location of the code to
; the value specified in rom0 (so, memory address $100 (256 hexadecimal))
section "Header", rom0[$100]