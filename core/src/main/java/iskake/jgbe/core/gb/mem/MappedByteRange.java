package iskake.jgbe.core.gb.mem;

import iskake.jgbe.core.Bitwise;

/**
 * Represents a range of bytes that are mapped to memory.
 */
public class MappedByteRange {
    public final int startAddress;
    public final int length;
    private final ReadableMemory getFunc;
    private final WritableMemory setFunc;

    public MappedByteRange(int startAddress, int length, ReadableMemory getFunc, WritableMemory setFunc) {
        startAddress = Bitwise.intAsShort(startAddress);
        length = Bitwise.intAsShort(length);

        if (startAddress > 0xffff || length > 0xffff) {
            throw new IndexOutOfBoundsException("The specified address is too large. Expected value <= 0xffff.");
        }

        this.startAddress = startAddress;
        this.length = length;
        this.getFunc = getFunc;
        this.setFunc = setFunc;
    }

    /**
     * Get the value stored at the specified index in the mapped range.
     * 
     * @param index The index (relative address) to get the byte from.
     * @return The value stored relative to the range's starting address.
     */
    public byte get(int index) {
        if (index > length) {
            throw new IndexOutOfBoundsException("The specified index is larger than the mapped range's length.");
        }

        return getFunc.read(startAddress + index);
    }

    /**
     * Set the value at the specified index in the mapped range.
     * 
     * @param index The index (relative address) to set the byte at.
     * @param value The value to write.
     */
    public void set(int index, byte value) {
        if (index > length) {
            throw new IndexOutOfBoundsException("The specified index is larger than the mapped range's length.");
        }

        setFunc.write(startAddress + index, value);
    }
}
