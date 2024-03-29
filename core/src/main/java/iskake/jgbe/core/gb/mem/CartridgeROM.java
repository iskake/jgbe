package iskake.jgbe.core.gb.mem;

import iskake.jgbe.core.gb.mem.mbc.MemoryBankController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Read Only Memory of 'Game Boy game pak', separated into multiple
 * {@code ROMBank}s.
 */
public class CartridgeROM implements ReadableMemory, WritableMemory {
    private static final Logger log = LoggerFactory.getLogger(CartridgeROM.class);
    private final String romName;
    private final ROMBank[] ROMBanks;
    private final MemoryBankController mbc;
    private final RAM[] RAMBanks;

    CartridgeROM(String romName, ROMBank[] romBanks, RAM[] ramBanks, MemoryBankController mbc) {
        this.romName = romName;
        this.ROMBanks = romBanks;
        this.RAMBanks = ramBanks;
        this.mbc = mbc;
    }

    public void init() {
        mbc.init();
    }

    /**
     * Try to restore all RAM banks, if possible.
     * Will attempt to read the 'save RAM' file if the ROM has battery support.
     */
    public void tryRestoreRAM() {
        if (mbc.battery && (RAMBanks != null)) {
            try {
                String inName = getPathNoExt(romName);
                FileInputStream fp = new FileInputStream(inName);
                for (RAM bank : RAMBanks) {
                    fp.read(bank.bytes);
                }
                fp.close();
                log.info("Read saveram: " + inName);
            } catch (IOException ignored) {
                log.warn("Tried to restore save RAM, but failed.");
            }
        }
    }

    /**
     * Try to save all RAM banks, if possible.
     * Will attempt to write all RAM banks if the ROM has battery support.
     */
    public void trySaveRAM() {
        if (mbc.battery && (RAMBanks != null)) {
            try {
                String outName = getPathNoExt(romName);
                FileOutputStream fp = new FileOutputStream(outName);
                for (RAM bank : RAMBanks) {
                    fp.write(bank.bytes);
                }
                fp.flush();
                fp.close();
                log.info("Wrote saveram: " + outName);
            } catch (IOException ignored) {
                log.warn("Tried to write save RAM to file, but failed.");
            }
        }
    }

    private static String getPathNoExt(String path) {
        if (path.endsWith(".gb") || path.endsWith(".gbc")) {
            return path.substring(0, path.indexOf(".")) + ".sav";
        } else {
            log.warn("The provided file does not have the extension: `.gb` or `.gbc`. Appending `.sav` to filename...");
            return path + ".sav";
        }
    }

    /**
     * Get the ROM bank with index 0.
     *
     * @return The correct ROM bank.
     */
    public ROMBank getROMBank0() {
        // ? Some MBCs can switch the first bank to other banks than bank0.
        return ROMBanks[mbc.getROMBank0Index()];
    }

    /**
     * Get the current switchable ROM bank, according to the MBC.
     *
     * @return The correct ROM bank.
     */
    public ROMBank getROMBankX() {
        return ROMBanks[mbc.getROMBankNIndex()];
    }

    /**
     * Get the current RAM bank, if any.
     *
     * @return The current RAM bank. If there is none, then {@code null} is
     *         returned instead.
     */
    public RAM getRAMBank() {
        if (RAMBanks.length == 0 || !mbc.isRAMEnabled())
            return null;

        return RAMBanks[mbc.getRAMBankIndex()];
    }

    @Override
    public void write(int address, byte value) throws IndexOutOfBoundsException {
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
