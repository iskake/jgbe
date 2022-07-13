package iskake.jgbe.core.gb.mem;

import iskake.jgbe.core.Bitwise;

/**
 * Represents a byte that is mapped to a specific address in memory.
 */
public class MappedByte {
    private final int mappedAddress;
    private final ReadableMemory getFunc;
    private final WritableMemory setFunc;

    public MappedByte(int address, ReadableMemory getFunc, WritableMemory setFunc) {
        address = Bitwise.intAsShort(address);

        if (address > 0xffff) {
            throw new IndexOutOfBoundsException("The specified address is too large. Expected value <= 0xffff");
        }

        this.mappedAddress = address;
        this.getFunc = getFunc;
        this.setFunc = setFunc;
    }

    /**
     * Get the value of the mapped byte.
     * 
     * @return The value stored at the mapped address.
     */
    public byte get() {
        return getFunc.read(mappedAddress);
    }

    /**
     * Set the value of the mapped byte.
     * 
     * @param value The value to write to the byte at the mapped address.
     */
    public void set(byte value) {
        setFunc.write(mappedAddress, value);
    }
}
