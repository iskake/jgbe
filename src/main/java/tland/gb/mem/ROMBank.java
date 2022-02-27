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
    public byte readByte(int address) {
        address = Bitwise.intAsByte(address);
        return bytes[address];
    }

    @Override
    public short readShort(int address) {
        address = Bitwise.intAsShort(address);
        byte lo = readByte(address);
        byte hi = readByte(address + 1);
        return Bitwise.toShort(hi, lo);
    }

}
