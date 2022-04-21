package tland.gb.mem;

/**
 * Object Attribute Memory ('Sprite Attribute Table'). 
 * Only accessible in modes 0-1 (STAT register bits 0-1).
 */
public class OAM extends RAM {

    public OAM(int size) {
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
