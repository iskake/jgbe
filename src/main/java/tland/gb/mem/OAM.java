package tland.gb.mem;

import tland.gb.HardwareRegisters;
import tland.gb.HardwareRegisters.HardwareRegisterIndex;

/**
 * Object Attribute Memory ('Sprite Attribute Table'). 
 * Only accessable in modes 0-1 (STAT register bits 0-1).
 */
public class OAM extends RAM {
    private final HardwareRegisters hwreg;

    public OAM(int size, HardwareRegisters hwreg) {
        super(size);
        this.hwreg = hwreg;
    }

    @Override
    public byte readByte(int address) throws IndexOutOfBoundsException {
        int i = Byte.toUnsignedInt(hwreg.readRegister(HardwareRegisterIndex.STAT)) & 0b11;
        if (i > 1) {
            return (byte) 0xff;
        }
        return super.readByte(address);
    }

    @Override
    public void writeByte(int address, byte value) throws IndexOutOfBoundsException {
        int i = Byte.toUnsignedInt(hwreg.readRegister(HardwareRegisterIndex.STAT)) & 0b11;
        if (i > 1) {
            return;
        }
        super.writeByte(address, value);
    }
}
