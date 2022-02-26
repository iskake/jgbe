package tland.gb.mem;

import java.util.Arrays;

import tland.Bitwise;

/**
 * ROM bank of a 'Game Boy game pak'. Bank size is 16KiB (0x4000 bytes).
 */
public class ROMBank implements ReadableMemory {
    final static int BANK_SIZE = 0x4000;
    final byte[] bytes;

    public ROMBank(byte[] bytes) {
        this.bytes = bytes;
    }

    @Override
    public int readByte(int index) {
        return bytes[index];
    }

    @Override
    public int readShort(int index) {
        int hi = readByte(index);
        int lo = readByte(index + 1);
        return Bitwise.toShort(hi, lo);
    }

}
