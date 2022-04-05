package tland.gb.mem;

import tland.gb.HardwareRegisters;
import tland.gb.HardwareRegisters.HardwareRegisterIndex;

/**
 * Memory map of hardware registers.
 */
public class HardwareRegisterMap implements ReadableMemory, WritableMemory {

    private HardwareRegisters hwregisters;

    public HardwareRegisterMap(HardwareRegisters hwreg) {
        this.hwregisters = hwreg;
    }

    @Override
    public void writeByte(int address, byte value) throws IndexOutOfBoundsException {
        HardwareRegisterIndex hwreg = HardwareRegisterIndex.getRegisterFromAddress(address);
        if (!hwregisters.writeRegister(hwreg, value)) {
            System.out.printf("Writing to invalid hw register: %04x\n", address);
        }
    }

    @Override
    public byte readByte(int address) throws IndexOutOfBoundsException {
        HardwareRegisterIndex hwreg = HardwareRegisterIndex.getRegisterFromAddress(address);
        if (hwreg == null) {
            System.out.printf("Reading from invalid hw register: %04x\n", address);
        }
        return hwregisters.readRegister(hwreg);
    }
}
