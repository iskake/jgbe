package iskake.jgbe.core.gb.mem.mbc;

public class MBC2 extends MemoryBankController {
    public MBC2(int numROMBanks, boolean battery) {
        // We use RAM banks = 0 because this is handled in ROMFactory.
        super(numROMBanks, 0, battery);
    }

    @Override
    public void write(int address, byte value) throws IndexOutOfBoundsException {
        if (address < 0x4000) {
            int num = Byte.toUnsignedInt(value);
            if (((address >> 8) & 1) == 0) {
                ramEnable = (num & 0xf) == 0xa;
            } else {
                if (numROMBanks > 1) {
                    num &= 0xf;
                    if (num == 0)
                        num++;

                    currROMBank = (num & (numROMBanks - 1));
                }
            }
        }
    }
}
