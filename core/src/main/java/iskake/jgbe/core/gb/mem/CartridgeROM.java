package iskake.jgbe.core.gb.mem;

import java.util.Arrays;

import iskake.jgbe.core.NotImplementedException;
import iskake.jgbe.core.gb.ROMHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Read Only Memory of 'Game Boy game pak', separated into multiple
 * {@code ROMBank}s.
 */
public class CartridgeROM implements ReadableMemory, WritableMemory {
    private static final Logger log = LoggerFactory.getLogger(CartridgeROM.class);
    private final ROMBank[] ROMBanks;
    private final MemoryBankController mbc;
    private final RAM[] RAMBanks;

    public CartridgeROM(byte[] bytes) {
        int numROMBanks = ROMHeader.getROMBanksNum(bytes);
        if (numROMBanks == -1) {
            log.warn("Invalid/unknown ROM bank size, assuming no extra banks...");
            numROMBanks = 2;

            // ?Also possible fallback:
            // numROMBanks = (bytes.length / ROMBank.BANK_SIZE);
            // ?If this is used, should we try to assume the MBC type too?
        }

        ROMBanks = new ROMBank[numROMBanks];
        for (int i = 0; i < numROMBanks; i++) {
            int offset = i * ROMBank.BANK_SIZE;
            ROMBanks[i] = new ROMBank(Arrays.copyOfRange(bytes, offset, offset + ROMBank.BANK_SIZE));
        }

        MemoryBankController tmpMBC;
        try {
            tmpMBC = ROMHeader.getMBCType(ROMBanks[0]);
        } catch (NotImplementedException | IllegalArgumentException e) {
            log.warn("Unimplemented/unknown MBC type, assuming no MBC...");
            tmpMBC = new NoMBC();
        }
        mbc = tmpMBC;

        int numRAMBanks = ROMHeader.getRAMBanksNum(ROMBanks[0]);
        if (numRAMBanks == -1) {
            log.warn("Invalid/unknown RAM bank size, assuming no external RAM...");
            numRAMBanks = 0;
        }

        RAMBanks = new RAM[numRAMBanks];
        for (int i = 0; i < RAMBanks.length; i++) {
            RAMBanks[i] = new RAM(0x2000); // TODO? handle RAM size < 8KiB.
        }
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
        // TODO: Test what reads to EXTRAM with no RAM bank returns.
        return RAMBanks[0];
    }

    @Override
    public void write(int address, byte value) throws IndexOutOfBoundsException {
        if (mbc == null)
            return;

        mbc.write(address, value);
    }

    @Override
    public byte read(int address) throws IndexOutOfBoundsException {
        if (address < 0x4000) {
            return getROMBank0().read(address);
        } else {
            address -= 0x4000;
            return getROMBankX().read(address);
        }
    }
}
