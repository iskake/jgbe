package tland.gb.mem;

/**
 * 'No Memory Bank Controller'.
 * All memory writes will not do anything.
 */
public class NoMBC extends MemoryBankController {

    @Override
    public void writeByte(int address, byte value) throws IndexOutOfBoundsException {
    }

}
