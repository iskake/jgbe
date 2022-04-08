package tland.gb.mem;

import tland.gb.HardwareRegisters;
import tland.gb.HardwareRegisters.HardwareRegisterIndex;

/**
 * Video memory. Only accessable in modes 0-2 (STAT register bits 0-1).
 */
public class VRAM extends RAM {
    private final HardwareRegisters hwreg;

    public VRAM(int size, HardwareRegisters hwreg) {
        super(size);
        this.hwreg = hwreg;
    }

    @Override
    public byte readByte(int address) throws IndexOutOfBoundsException {
        int i = Byte.toUnsignedInt(hwreg.readRegister(HardwareRegisterIndex.STAT)) & 0b11;
        if (i == 3) {
            return (byte) 0xff;
        }
        return super.readByte(address);
    }

    @Override
    public void writeByte(int address, byte value) throws IndexOutOfBoundsException {
        int i = Byte.toUnsignedInt(hwreg.readRegister(HardwareRegisterIndex.STAT)) & 0b11;
        if (i == 3) {
            return;
        }
        super.writeByte(address, value);
    }
}
