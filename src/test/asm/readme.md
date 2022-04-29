## ASM tests

This directory contains a few assembly files for testing JGBE.

Each file is commented to explain each line of code, so please look at each comment if you do not understand what is happening.

For example:

```
; Comments start with a semicolon!

ld a, $42   ; load the hex value $42 (66 in decimal) into the a register
ld b, a     ; load the value in the a register into the b register (b = $42)
```

These files should to be manually tested (looking at the output of the program) instead of being executed in the java tests (unless otherwise noted in the java source files).

## Running

To run these tests:
- Run JGBE and type `open` + the path of the file you wish to run. (e.g. `open src/test/asm/bin/test_alu.zb`)

### Note for building

To build these tests, you need rgbds. If you do not have rgbds installed, you can instead run the included pre-assembled files (binary files) with the `.zb` extension.