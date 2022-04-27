package tland.gb.mem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Read Only Memory, separated into multiple
 * {@code ROMBank}s.
 */
public class CartridgeROM implements ReadableMemory<Byte> {
    private final ROMBank[] banks;
    private final MemoryBankController mbc;

    public CartridgeROM(byte[] bytes) {
        List<ROMBank> tempBanks = new ArrayList<>();

        for (int i = 0; i < (bytes.length / ROMBank.BANK_SIZE); i++) {
            int offset = i * ROMBank.BANK_SIZE;
            tempBanks.add(new ROMBank(Arrays.copyOfRange(bytes, offset, offset + ROMBank.BANK_SIZE)));
        }

        banks = tempBanks.toArray(new ROMBank[0]);
        if (banks.length > 2) {
            System.out.println("Multiple ROM banks!!");
        }
        mbc = new NoMBC(); // Temp.
    }

    /**
     * Get the ROM bank with index 0.
     * 
     * @return The correct ROM bank.
     */
    public ROMBank getROMBank0() {
        return banks[0];
    }

    /**
     * Get the current switchable ROM bank, according to the MBC.
     * 
     * @return The correct ROM bank.
     */
    public ROMBank getROMBankX() {
        return banks[mbc.getSwitchableIndex()];
    }

    @Override
    public Byte readAddress(int address) throws IndexOutOfBoundsException {
        if (address < 0x4000) {
            return getROMBank0().readAddress(address);
        } else {
            address -= 0x4000;
            return getROMBankX().readAddress(address);
        }
    }
}
