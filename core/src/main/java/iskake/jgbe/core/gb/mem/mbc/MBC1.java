package iskake.jgbe.core.gb.mem.mbc;

public class MBC1 extends MemoryBankController {
    private int bankMode = 0;

    // TODO: 1MiB Multi-game MBC1 cartridges.
    public MBC1(int numROMBanks, int numRAMBanks, boolean battery) {
        super(numROMBanks, numRAMBanks, battery);
    }

    @Override
    public void init() {
        super.init();
        bankMode = 0;
    }

    @Override
    public int getROMBank0Index() {
        return bankMode == 1 ? (currROMBank & 0b0110_0000) : 0;
    }

    @Override
    public int getROMBankNIndex() {
        return bankMode == 1 ? (currROMBank & 0b0111_1111) : currROMBank;
    }

    @Override
    public int getRAMBankIndex() {
        return bankMode == 1 ? currRAMBank : 0;
    }

    @Override
    public void write(int address, byte value) throws IndexOutOfBoundsException {
        if (address < 0x2000) {
            ramEnable = (Byte.toUnsignedInt(value) & 0xf) == 0xa;
        } else if (address < 0x4000) {
            int num = Byte.toUnsignedInt(value);

            if ((num & 0x1f )== 0)
                num++;

            currROMBank = (currROMBank & 0b0110_0000) | ((num & 0x1f) & (numROMBanks - 1));
        } else if (address < 0x6000) {
            if (numRAMBanks > 1)
                currRAMBank = Byte.toUnsignedInt(value) & 0b11;

            // Bank mode select
            if (numROMBanks > 0b1_1111)
                currROMBank = (currROMBank & 0b0001_1111) | (((Byte.toUnsignedInt(value) & 0b11) << 5) & (numROMBanks - 1));
        } else if (address < 0x8000) {
            bankMode = Byte.toUnsignedInt(value) & 1;
        }
    }
}
