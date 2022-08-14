# Status

Currently, many games work correctly with minor glitches.
More 'advanced' games (such as multi-cart, mbc7, etc.) or games that need more accurate
timing may not work perfectly, or at all.

JGBE does not support CGB-only games, and will instead attempt to run them as a DMG Game Boy would.

## Technical overview of JGBE features

### CPU

- The blargg `cpu_instrs` test passes, but not `cpu_timing` (Error #255).
- Most of the mooneye tests fail.
- `HALT` is partially implemented.
- `STOP` not implemented at all.

### MBC, ROM, and RAM

DMG BootROMs are supported if named `dmg_boot.bin` and placed in the current directory (256b size exactly) 

RAM: External RAM is supported.

Saving is supported. Currently, this is done by simply saving all data in RAM to a `.sav` file.
From some brief testing, these files also seem to work in other emulators (note: see MBC2 support below.)

As for MBCs, JGBE passes all mooneye mbc tests for MBC2 and MBC5, and all MBC1 tests except for `multicart_rom_8Mb`

MBC support:
- [x] ROM only
- [x] MBC1
  - [ ] MBC1 multi-carts
- [x] MBC2
  - Note on other emulator save loading: mGBA saves each pair of '4bit bytes' as a single byte (resulting in 256b total),
while SameBoy simply saves each '4bit byte' as a single byte (resulting in 512b total.)
  - Should the mGBA loading be accounted for?
- [x] MBC3
  - [ ] RTC support
- [x] MBC5
  - [ ] Rumble
  - Technically, rumble _is_ 'implemented', but glfw does not currently support rumble (not that jgbe has gamepad support...)
- [ ] MBC6
  - Notes: Only used for one game?
- [ ] MBC7
  - Notes: Need to first implement a way to use accelerometer (with gamepad: joysticks / actual accelerometer?)
- [ ] Any other MBCs / multi-carts
  - Will maybe implement these _if they are official_, otherwise...

### PPU - graphics

JGBE currently _does_ pass the `dmg-acid2`and `sprite_priority` tests, but not certain other tests
(such as most of the Mealybug Tearoom Tests), and has some graphical glitches in commercial games, some examples:

Simple: mountain in LADX intro, kirby2 intro (note: both when LY=0).

Severe: Wario Land 2 intro is broken.

The ppu implementation uses per-scanline rendering, in which each scanline is added to a buffer once the 'scanline dot' 
reaches HBlank. This is used instead of a proper pixel FIFO for performance reasons,
a pixel FIFO is partially implemented, though it is _very_ slow.

### APU - audio

Audio is not implemented.

- There is no audio output. 
- Audio hardware registers do nothing other than hold data.
  - As a result, wave RAM is also not implemented.

### Other

- Serial transfers are not implemented
- Joypad (P1) is implemented, though only keyboard input is supported.
  - (currently: the joypad interrupt is not implemented)
