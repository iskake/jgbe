package tland.gb.mem;

import tland.Bitwise;

/**
 * Random Access Memory. Implementation of memory that can be both read and
 * written to.
 */
public class RAM implements WritableMemory, ReadableMemory {
    public final int size;
    private byte[] bytes;

    public RAM(int size) {
        this.size = size;
        bytes = new byte[size];
    }

    @Override
    public byte readByte(int address) throws IndexOutOfBoundsException {
        return bytes[address];
    }

    @Override
    public short readShort(int address) throws IndexOutOfBoundsException {
        byte lo = readByte(address);
        byte hi = readByte(address + 1);
        return Bitwise.toShort(hi, lo);
    }

    @Override
    public void writeByte(int address, byte value) throws IndexOutOfBoundsException {
        bytes[address] = value;
    }

    @Override
    public void writeShort(int address, short value) throws IndexOutOfBoundsException {
        byte lo = Bitwise.getLowByte(value);
        byte hi = Bitwise.getHighByte(value);
        writeByte(address, lo);
        writeByte(address + 1, hi);
    }

}
