package iskake.jgbe.core.gb.mem;

import iskake.jgbe.core.NotImplementedException;
import iskake.jgbe.core.gb.ROMHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class ROMFactory {
    private static final Logger log = LoggerFactory.getLogger(ROMFactory.class);

    public CartridgeROM getROM(byte[] bytes) {
        int numROMBanks = ROMHeader.getROMBanksNum(bytes);
        if (numROMBanks == -1) {
            log.warn("Invalid/unknown ROM bank size, assuming no extra banks...");
            numROMBanks = 2;

            // ?Also possible fallback:
            // numROMBanks = (bytes.length / ROMBank.BANK_SIZE);
            // ?If this is used, should we try to assume the MBC type too?
        } else {
            log.info("ROM banks: " + numROMBanks);
        }
        ROMBank[] ROMBanks = new ROMBank[numROMBanks];
        for (int i = 0; i < numROMBanks; i++) {
            int offset = i * ROMBank.BANK_SIZE;
            ROMBanks[i] = new ROMBank(Arrays.copyOfRange(bytes, offset, offset + ROMBank.BANK_SIZE));
        }

        int numRAMBanks = ROMHeader.getRAMBanksNum(ROMBanks[0]);
        if (numRAMBanks == -1) {
            log.warn("Invalid/unknown RAM bank size, assuming no external RAM...");
            numRAMBanks = 0;
        } else {
            log.info("RAM banks: " + numRAMBanks);
        }
        RAM[] RAMBanks = new RAM[numRAMBanks];
        for (int i = 0; i < RAMBanks.length; i++) {
            RAMBanks[i] = new RAM(0x2000); // TODO? handle RAM size < 8KiB.
        }

        MemoryBankController tmpMBC;
        try {
            tmpMBC = ROMHeader.getMBCType(ROMBanks[0], numROMBanks, numRAMBanks);
        } catch (NotImplementedException | IllegalArgumentException e) {
            log.error(e.getMessage());
            log.warn("Unimplemented/unknown MBC type, assuming no MBC...");
            tmpMBC = new NoMBC(numRAMBanks);
        }

        return new CartridgeROM(ROMBanks, RAMBanks, tmpMBC);
    }

    /**
     * Load a new ROM file.
     *
     * @param pathString the path to the ROM file.
     */
    public CartridgeROM getROM(String pathString) {
        log.info("Loading rom: " + pathString);
        Path path = Paths.get(pathString);
        byte[] romFile;

        try {
            romFile = Files.readAllBytes(path);
        } catch (IOException e) {
            String reason = e instanceof FileNotFoundException || e instanceof NoSuchFileException
                    ? "file not found."
                    : "an exception occurred: " + e;
            log.error("Could not read the file " + path.getFileName() + ": " + reason);
            return null;
        }

        try {
            return getROM(romFile);
        } catch (Exception e) {
            log.error("Could not load the file " + path.getFileName() + ": an exception occurred: " + e);
            return null;
        }
    }
}
