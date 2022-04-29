package tland.emu.mem;

import java.util.Arrays;

/**
 * Random Access Memory. Implementation of memory that can be both read and
 * written to.
 * 
 * @author Tarjei Landøy
 */
public class RAM implements WritableMemory<Byte>, ReadableMemory<Byte> {
    public final int size;
    protected byte[] bytes;

    public RAM(int size) {
        this.size = size;
        bytes = new byte[size];
    }

    @Override
    public Byte readAddress(int address) throws IndexOutOfBoundsException {
        return bytes[address];
    }

    @Override
    public void writeAddress(int address, Byte value) throws IndexOutOfBoundsException {
        bytes[address] = value;
    }

    /**
     * Clear the memory.
     * Each byte will have a value of $00.
     */
    public void clear() {
        Arrays.fill(bytes, (byte)0);
    }

}
