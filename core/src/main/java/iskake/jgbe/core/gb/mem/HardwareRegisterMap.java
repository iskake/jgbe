package iskake.jgbe.core.gb.mem;

import iskake.jgbe.core.gb.HardwareRegisters;
import iskake.jgbe.core.gb.HardwareRegisters.HardwareRegister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Wrapper class around {@link HardwareRegisters#map}.
 * <p>
 * Internally this class simply calls the {@code get} method on the register map.
 * </p>
 */
public class HardwareRegisterMap implements ReadableMemory, WritableMemory {
    private static final Logger log = LoggerFactory.getLogger(HardwareRegisterMap.class);
    private final HardwareRegisters hwreg;

    public HardwareRegisterMap(HardwareRegisters hwreg) {
        this.hwreg = hwreg;
    }

    @Override
    public void write(int address, byte value) throws IndexOutOfBoundsException {
        HardwareRegister reg = HardwareRegisters.map.get(address);
        if (!hwreg.write(reg, value)) {
            log.warn("Writing to invalid hw register: %04x".formatted(address));
        }
    }

    @Override
    public byte read(int address) throws IndexOutOfBoundsException {
        HardwareRegister reg = HardwareRegisters.map.get(address);
        return hwreg.read(reg);
    }
}
