include "hardware.inc"

if !def(ENTRY)
def ENTRY equ 1

section "Header", rom0[$100]

	jp main

	ds $150 - @, 0

main:

endc	; ENTRY