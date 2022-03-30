package tland.gb.mem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Read Only Memory of 'Game Boy game pak', separated into multiple
 * {@code ROMBank}s.
 */
public class CartridgeROM implements ReadableMemory, WritableMemory {
    private final ROMBank[] ROMBanks;
    private final MemoryBankController mbc;
    // private RAM[] RAMBanks; // TODO add External RAM support

    public CartridgeROM(byte[] bytes) {
        List<ROMBank> tempBanks = new ArrayList<>();

        for (int i = 0; i < (bytes.length / ROMBank.BANK_SIZE); i++) {
            int offset = i * ROMBank.BANK_SIZE;
            tempBanks.add(new ROMBank(Arrays.copyOfRange(bytes, offset, offset + ROMBank.BANK_SIZE)));
        }

        ROMBanks = tempBanks.toArray(new ROMBank[tempBanks.size()]);
        mbc = new NoMBC(); // Temp.
    }

    /**
     * Get the ROM bank with index 0.
     * 
     * @return The correct ROM bank.
     */
    public ROMBank getROMBank0() {
        // ? Some MBCs can switch the first bank to other banks than bank0.
        return ROMBanks[0];
    }

    /**
     * Get the current switchable ROM bank, according to the MBC.
     * 
     * @return The correct ROM bank.
     */
    public ROMBank getROMBankX() {
        return ROMBanks[mbc.getSwitchableIndex()];
    }

    /**
     * Get the current RAM bank, if any.
     * 
     * @return The current RAM bank. If there is none, then {@code null} is
     *         returned instead.
     */
    public RAM getRAMBank() {
        return null; // TODO
    }

    @Override
    public void writeByte(int address, byte value) throws IndexOutOfBoundsException {
        if (mbc == null)
            return;

        mbc.writeByte(address, value);
    }

    @Override
    public byte readByte(int address) throws IndexOutOfBoundsException {
        if (address < 0x4000) {
            return getROMBank0().readByte(address);
        } else {
            address -= 0x4000;
            return getROMBankX().readByte(address);
        }
    }
}
