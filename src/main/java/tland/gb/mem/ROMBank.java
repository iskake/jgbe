package tland.gb.mem;

import tland.Bitwise;

/**
 * ROM bank of a 'Game Boy game pak'. Bank size is 16KiB (0x4000 bytes).
 */

public record ROMBank(byte[] bytes) implements ReadableMemory {
    public final static int BANK_SIZE = 0x4000;

    @Override
    public byte readByte(int address) {
        address = Bitwise.intAsShort(address);
        return bytes[address];
    }

}
