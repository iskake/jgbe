package tland.gb.mem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Read Only Memory of 'Game Boy game pak', separated into multiple
 * {@code ROMBank}s.
 */
public class CartridgeROM {
    private final ROMBank[] ROMBanks;
    // private RAM[] RAMBanks; // TODO add MBC and External RAM support

    public CartridgeROM(byte[] bytes) {
        List<ROMBank> tempBanks = new ArrayList<>();

        for (int i = 0; i < (bytes.length / ROMBank.BANK_SIZE); i++) {
            int offset = i * ROMBank.BANK_SIZE;
            tempBanks.add(new ROMBank(Arrays.copyOfRange(bytes, offset, offset + ROMBank.BANK_SIZE)));
        }

        ROMBanks = tempBanks.toArray(new ROMBank[tempBanks.size()]);
    }

    /**
     * Get specified {@code ROMBank} from cartridge ROM.
     * 
     * @param index The index of the {@code ROMBank}.
     * @return The specified {@code ROMBank}.
     */
    public ROMBank getBank(int index) {
        return ROMBanks[index];
    }

    public RAM getRAMBank(int index) {
        return null; // return RAMBanks[index];
    }
}
