package tland.emu.mem;

/**
 * Read Only Memory, memory that can only be read from, not written to.
 */
public record ROM(byte[] data) implements ReadableMemory<Byte> {
    public final static int ROM_SIZE = 0x8000;

    public ROM(byte[] data) {
        if (data.length > 0x8000) {
            throw new IndexOutOfBoundsException(
                    "The specified ROM data is too large! (must be either under or exactly 32Kib / 32768 bytes.)" + "\nActual size:" + data.length);
        }

        this.data = data;
    }

    @Override
    public Byte readAddress(int address) throws IndexOutOfBoundsException {
        return data[address];
    }
}
