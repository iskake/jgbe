package tland.emu.mem;

import tland.Bitwise;

/**
 * ROM bank. Bank size is 16KiB (0x4000 bytes).
 * TODO: replace with CartridgeROM
 */

public record ROMBank(byte[] bytes) implements ReadableMemory<Byte> {
    public final static int BANK_SIZE = 0x4000;

    @Override
    public Byte readAddress(int address) {
        address = Bitwise.intAsShort(address);
        return bytes[address];
    }

}
