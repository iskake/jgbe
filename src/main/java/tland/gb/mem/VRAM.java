package tland.gb.mem;

/**
 * Video memory. Only accessable in modes 0-2 (STAT register bits 0-1).
 */
public class VRAM extends RAM {

    public VRAM(int size) {
        super(size);
    }

    @Override
    public byte readByte(int address) throws IndexOutOfBoundsException {
        return super.readByte(address);
    }

    @Override
    public void writeByte(int address, byte value) throws IndexOutOfBoundsException {
        super.writeByte(address, value);
    }
}
