package iskake.jgbe.core.gb.mem;

/**
 * 'No Memory Bank Controller'.
 * All memory writes will not do anything.
 */
public class NoMBC extends MemoryBankController {

    @Override
    public void write(int address, byte value) throws IndexOutOfBoundsException {
    }

}
