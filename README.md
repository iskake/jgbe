# JGBE

JGBE is a SM83 CPU emulator/interpreter/virtual machine for assembly programs and scripts (see instruction set section below.)

## Running JGBE

Running programs can be done in two different ways:

- Running an interactive shell
- Running a script or binary file

## Example usage

'Hello world' in JGBE assembly

```
>>> prt "Hello, world!"
>>> run
Hello world!
>>> 
```

A loop printing numbers 0 - 9

```
>>> ld a, 0
>>> start_loop:
>>>     prt "%d", a
>>>     inc a
>>>     cp 10
>>>     jp nz, start_loop
>>> run
0
1
2
3
4
5
6
7
8
9
>>>
```


## Instruction set / assembly language

JGBE uses a modified version of the instruction set of the SM83 processor (used in the Game Boy systems), with the following changes/differences:

- The `ei` and `di` instructions are undefined, and do nothing because there are no interrupts (`ei` = "enable interrupts", `di` = "disable interrupts").
    - As a result of no interrupts, the `reti` instruction also does not 
- The `halt` instruction has been changed to wait for the amount of milliseconds stored in the `hl` register.
- The `stop` instruction has been changed to stop the execution of the program and exit.
- New instruction: `prt`, works as a print statement.

If you are curious about how the assembly is supposed to work, please look through the files in the [`src/test/asm/`](src/test/asm/) folder.

It is recommended to read through each file in alphabetical order (`test_alu.asm` to `test_stop.asm`.)

**Note:** that the syntax used in `.asm` files differs from the syntax used in the CLI interpreter, because the language used is assembly for the sm83 processor (in the Game Boy) NOT the modified version used in JGBE. This means that new instructions (e.g. the `prt` instruction) are not included, and other differences (such as `include` and `section`, which are both not included in the CLI interpreter) which are.)

## References

[Pandocs](https://gbdev.io/pandocs/)

[RGBDS docs](https://rgbds.gbdev.io/docs/v0.5.2/gbz80.7)

[GBDocs opcode table](https://gbdev.io/gb-opcodes//optables/)

