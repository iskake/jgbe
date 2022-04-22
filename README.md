# JGBE

JGBE is a SM83 CPU emulator/interpreter/virtual machine for assembly programs and scripts (see instruction set section below.)

## Running JGBE

Running programs can be done in two different ways:

- Running an interactive shell
- Running a script or binary file

<!-- ## Example usage

'Hello world' in JGBE assembly

```
>>> prt "Hello, world!\n"
>>> run
Hello world!
>>> 
```

A loop printing numbers 0 - 9

```
>>> ld a, 0
>>> start_loop:
>>>     prt "%d\n", a
>>>     inc a
>>>     cp 10
>>>     jr nz, start_loop
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

TODO: finish CLI start message first... -->

## JGBE instruction set

JGBE uses a modified version of the instruction set of the SM83 processor (used in the Game Boy systems), with the following changes/differences:

- The `ei` and `di` instructions are undefined (TODO!), and do nothing because there are no interrupts.
- The `halt` instruction has been changed to wait for the amount of milliseconds stored in the `hl` register.
- The `stop` instruction has been changed to stop the execution of the program and exit.
<!-- - New instruction: `prt`, works as a print statement. -->

## References

[Pandocs](https://gbdev.io/pandocs/)

[RGBDS docs](https://rgbds.gbdev.io/docs/v0.5.2/gbz80.7)

[GBDocs opcode table](https://gbdev.io/gb-opcodes//optables/)

