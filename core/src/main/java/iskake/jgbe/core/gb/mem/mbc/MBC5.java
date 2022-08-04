package iskake.jgbe.core.gb.mem.mbc;

public class MBC5 extends MemoryBankController {
    private final boolean rumble;
    private boolean rumbling = false;

    public MBC5(int numROMBanks, int numRAMBanks, boolean battery, boolean rumble) {
        super(numROMBanks, numRAMBanks, battery);
        this.rumble = rumble;
    }

    @Override
    public void init() {
        super.init();
        rumbling = false;
    }

    /**
     * Check if the cartridge is currently rumbling.
     *
     * @return {@code true} if the rumble motor is enabled, {@code false} otherwise.
     */
    //? glfw does not currently support rumble, so this cannot be used to actually rumble the controller.
    public boolean isRumbling() {
        return rumbling;
    }

    @Override
    public void write(int address, byte value) throws IndexOutOfBoundsException {
        if (address < 0x2000) {
            ramEnable = (Byte.toUnsignedInt(value) & 0xf) == 0xa;
        } else if (address < 0x3000) {
            int num = Byte.toUnsignedInt(value);
            currROMBank = (currROMBank & 0b1_0000_0000) | (num & (numROMBanks - 1));
        } else if (address < 0x4000) {
            if (numROMBanks > 0xff)
                currROMBank = (currROMBank & 0b0_1111_1111) | (Byte.toUnsignedInt(value) & 1) << 8;
        } else if (address < 0x6000) {
            int num = Byte.toUnsignedInt(value);
            currRAMBank = (num & 0b1111) & (numRAMBanks - 1);

            if (rumble) {
                rumbling = (num & 0b1000) != 0;
            }
        }
    }
}
