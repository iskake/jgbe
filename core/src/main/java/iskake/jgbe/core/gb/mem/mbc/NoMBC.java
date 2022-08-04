package iskake.jgbe.core.gb.mem.mbc;

/**
 * 'No Memory Bank Controller'.
 * All memory writes will not do anything.
 */
public class NoMBC extends MemoryBankController {

    // ToDoMaybe: From pandocs: "No licensed cartridge makes use of [No MBC + RAM (+ Battery)]. The exact behavior is unknown."
    // Should this even be checked out in the first place?
    public NoMBC() {
        super(1, 0, false);
    }

    @Override
    public boolean isRAMEnabled() {
        return false;
    }

    @Override
    public void write(int address, byte value) throws IndexOutOfBoundsException {
    }

}
