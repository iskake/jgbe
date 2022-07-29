package iskake.jgbe.core.gb.mem;

public class MBC1 extends MemoryBankController {
    private boolean ramEnable = false; // TODO...

    public MBC1(int numROMBanks, int numRAMBanks) {
        super(numROMBanks, numRAMBanks);
    }

    @Override
    public void write(int address, byte value) throws IndexOutOfBoundsException {
        if (address < 0x2000) {
            ramEnable = (Byte.toUnsignedInt(value) & 0xf) == 0xa;
        } else if (address < 0x4000) {
            int bank = Byte.toUnsignedInt(value) & 0x1f;

            if (bank == 0)
                bank = 1;

            currROMBank = bank;
        } else if (address < 0x6000) {
            // TODO: mbc for banks which need > 5bits for bank selection
            int bank = Byte.toUnsignedInt(value) & 0b11;
            currRAMBank = bank;
        } else {
            int bank = Byte.toUnsignedInt(value) & 1;
            // TODO: switchable bank0?
        }
    }
}
