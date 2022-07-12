package iskake.jgbe.core.gb.mem;

import iskake.jgbe.core.gb.HardwareRegisters;
import iskake.jgbe.core.gb.HardwareRegisters.HardwareRegister;

/**
 * 'Not usable' memory region. Writing does nothing, and reads have weird
 * behaviour.
 * <p>
 * According to Nintendo, use of this area is prohibited.
 */
public class ProhibitedMemory implements WritableMemory, ReadableMemory {

    private HardwareRegisters hwreg;

    public ProhibitedMemory(HardwareRegisters hwreg) {
        this.hwreg = hwreg;
    }

    @Override
    public byte readByte(int address) {
        System.out.printf("Reading from unusable memory address: $%04x\n");
        return switch (hwreg.readRegister(HardwareRegister.STAT) & 0b11) {
            case 0, 1 -> (byte) 0;
            default -> (byte) 0xff;
        };
    }

    @Override
    public void writeByte(int address, byte value) {
        System.out.printf("Attempting write to unusable memory address: $%04x\n", address);
    }

}