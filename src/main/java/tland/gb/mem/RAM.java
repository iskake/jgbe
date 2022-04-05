package tland.gb.mem;

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
    public void writeByte(int address, byte value) throws IndexOutOfBoundsException {
        bytes[address] = value;
    }

}
