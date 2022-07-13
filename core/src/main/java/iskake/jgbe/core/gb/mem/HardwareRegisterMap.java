package iskake.jgbe.core.gb.mem;

import iskake.jgbe.core.gb.HardwareRegisters;
import iskake.jgbe.core.gb.HardwareRegisters.HardwareRegister;

/**
 * Memory map of hardware registers.
 */
public class HardwareRegisterMap implements ReadableMemory, WritableMemory {

    private HardwareRegisters hwregisters;

    public HardwareRegisterMap(HardwareRegisters hwreg) {
        this.hwregisters = hwreg;
    }

    @Override
    public void write(int address, byte value) throws IndexOutOfBoundsException {
        HardwareRegister hwreg = HardwareRegister.getRegisterFromAddress(address);
        if (!hwregisters.write(hwreg, value)) {
            System.out.printf("Writing to invalid hw register: %04x\n", address);
        }
    }

    @Override
    public byte read(int address) throws IndexOutOfBoundsException {
        HardwareRegister hwreg = HardwareRegister.getRegisterFromAddress(address);
        return hwregisters.read(hwreg);
    }
}
