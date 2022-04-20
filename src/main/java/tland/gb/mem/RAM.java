package tland.gb.mem;

import java.util.Arrays;
import java.util.Random;

/**
 * Random Access Memory. Implementation of memory that can be both read and
 * written to.
 */
public class RAM implements WritableMemory, ReadableMemory {
    public final int size;
    
    protected byte[] bytes;
    private final Random rand;

    public RAM(int size) {
        this.size = size;
        bytes = new byte[size];
        rand = new Random();
    }

    @Override
    public byte readByte(int address) throws IndexOutOfBoundsException {
        return bytes[address];
    }

    @Override
    public void writeByte(int address, byte value) throws IndexOutOfBoundsException {
        bytes[address] = value;
    }

    /**
     * Clear the memory.
     * Each byte will have a value of $00.
     */
    public void clear() {
        Arrays.fill(bytes, (byte)0);
    }

    /**
     * Randomize each byte in memory.
     * Each byte will have a value from $00 to $ff.
     */
    public void randomize() {
        rand.nextBytes(bytes);
    }

}
