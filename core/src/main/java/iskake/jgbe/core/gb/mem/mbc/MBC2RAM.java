package iskake.jgbe.core.gb.mem.mbc;

import iskake.jgbe.core.gb.mem.RAM;

/**
 * RAM used in MBC2.
 */
public class MBC2RAM extends RAM {
    public MBC2RAM() {
        super(512);
    }

    @Override
    public byte read(int address) throws IndexOutOfBoundsException {
        return getCorrectValue(super.read(getCorrectAddress(address)));
    }

    @Override
    public void write(int address, byte value) throws IndexOutOfBoundsException {
        super.write(getCorrectAddress(address), getCorrectValue(value));
    }

    private static int getCorrectAddress(int address) {
        return (address & 0x1ff);
    }

    private static byte getCorrectValue(byte value) {
        return (byte)((value & 0x0f) | 0xf0);
    }
}
