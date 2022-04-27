package tland.gb.mem;

/**
 * 'No Memory Bank Controller'.
 * All memory writes will not do anything.
 */
public class NoMBC extends MemoryBankController {

    @Override
    public void writeAddress(int address, Byte value) throws IndexOutOfBoundsException {
    }

}
