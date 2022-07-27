package iskake.jgbe.core.gb.mem;

import iskake.jgbe.core.gb.HardwareRegisters;
import iskake.jgbe.core.gb.HardwareRegisters.HardwareRegister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 'Not usable' memory region. Writing does nothing, and reads have weird
 * behaviour.
 * <p>
 * According to Nintendo, use of this area is prohibited.
 */
public class ProhibitedMemory implements WritableMemory, ReadableMemory {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final HardwareRegisters hwreg;

    public ProhibitedMemory(HardwareRegisters hwreg) {
        this.hwreg = hwreg;
    }

    @Override
    public byte read(int address) {
        log.warn("Reading from unusable memory address: $%04x".formatted(address));
        return switch (hwreg.read(HardwareRegister.STAT) & 0b11) {
            case 0, 1 -> (byte) 0;
            default -> (byte) 0xff;
        };
    }

    @Override
    public void write(int address, byte value) {
        log.warn("Attempting write to unusable memory address: $%04x".formatted(address));
    }

}
