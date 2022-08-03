package iskake.jgbe.core.gb.mem;

public class MBC1 extends MemoryBankController {
    // TODO: move ram enabling to either:
    // - 'Ram enabled mbc', new mbc?
    // - The MemoryBankController constructor, non-ram (NoMBC) just uses `false` for this.
    private boolean ramEnable = false;

    public MBC1(int numROMBanks, int numRAMBanks, boolean battery) {
        super(numROMBanks, numRAMBanks, battery);
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
            currRAMBank = Byte.toUnsignedInt(value) & 0b11;
        } else {
            int bank = Byte.toUnsignedInt(value) & 1;
            // TODO: switchable bank0?
        }
    }
}
