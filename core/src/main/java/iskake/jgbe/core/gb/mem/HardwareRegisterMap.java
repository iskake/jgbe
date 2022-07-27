package iskake.jgbe.core.gb.mem;

import iskake.jgbe.core.gb.HardwareRegisters;
import iskake.jgbe.core.gb.HardwareRegisters.HardwareRegister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Memory map of hardware registers.
 */
public class HardwareRegisterMap implements ReadableMemory, WritableMemory {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final HardwareRegisters hwreg;

    public HardwareRegisterMap(HardwareRegisters hwreg) {
        this.hwreg = hwreg;
    }

    @Override
    public void write(int address, byte value) throws IndexOutOfBoundsException {
        HardwareRegister reg = HardwareRegister.getRegisterFromAddress(address);
        if (!hwreg.write(reg, value)) {
            log.warn("Writing to invalid hw register: %04x".formatted(address));
        }
    }

    @Override
    public byte read(int address) throws IndexOutOfBoundsException {
        HardwareRegister reg = HardwareRegister.getRegisterFromAddress(address);
        return hwreg.read(reg);
    }
}
