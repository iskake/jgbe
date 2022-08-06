package iskake.jgbe.core.gb.mem.mbc;

public class MBC3 extends MemoryBankController {
    private final boolean rtc;
    private boolean rtcEnable;
    private int latchedIndex = -1;
    private byte latchValue = -1;

    public MBC3(int numROMBanks, int numRAMBanks, boolean battery, boolean rtc) {
        super(numROMBanks, numRAMBanks, battery);
        this.rtc = rtc;
    }

    @Override
    public void init() {
        super.init();
        rtcEnable = false;
        latchedIndex = -1;
        latchValue = -1;
    }

    public boolean isRTCEnabled() {
        return rtcEnable;
    }

    public void readRTC() {
        // TODO
    }

    private void latchRTCData() {
        // TODO
    }

    @Override
    public void write(int address, byte value) throws IndexOutOfBoundsException {
        if (address < 0x2000) {
            ramEnable = (Byte.toUnsignedInt(value) & 0xf) == 0xa;
            rtcEnable = ramEnable;
        } else if (address < 0x4000) {
            int num = Byte.toUnsignedInt(value) & 0b0111_1111;

            if (num == 0)
                num++;

            currROMBank = (num & (numROMBanks - 1));
        } else if (address < 0x6000) {
            int num = Byte.toUnsignedInt(value) & 0b1111;

            if (numRAMBanks > 0 && num < 4)
                currRAMBank = num;
            else if (rtcEnable && (num >= 8 && num <= 0xc))
                latchedIndex = num;
        } else if (address < 0x8000) {
            if (latchValue == 0 && value == 1) {
                latchRTCData();
            }
            latchValue = value;
        } else if (address >= 0xa000 && address < 0xc000) {
            throw new RuntimeException("RTC is currently not implemented!");
        }
    }
}
