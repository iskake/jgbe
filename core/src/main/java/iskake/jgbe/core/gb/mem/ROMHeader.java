package iskake.jgbe.core.gb.mem;

import iskake.jgbe.core.Bitwise;
import iskake.jgbe.core.NotImplementedException;
import iskake.jgbe.core.gb.mem.mbc.*;

import java.util.Arrays;

/**
 * Header information and utilities for a Game Boy cartridge header.
 * <p>
 * The header is stored in ROM Bank 0 at the address range {@code $0100-$014f}.
 */
public class ROMHeader {

    private ROMHeader() {
    }

    /**
     * Compressed Nintendo logo data.
     * Each tile of the logo is stored in 2 bytes, as opposed to 16 bytes.
     */
    private final static byte[] logo = {
            (byte) 0xCE, (byte) 0xED, (byte) 0x66, (byte) 0x66, (byte) 0xCC, (byte) 0x0D, (byte) 0x00, (byte) 0x0B,
            (byte) 0x03, (byte) 0x73, (byte) 0x00, (byte) 0x83, (byte) 0x00, (byte) 0x0C, (byte) 0x00, (byte) 0x0D,
            (byte) 0x00, (byte) 0x08, (byte) 0x11, (byte) 0x1F, (byte) 0x88, (byte) 0x89, (byte) 0x00, (byte) 0x0E,
            (byte) 0xDC, (byte) 0xCC, (byte) 0x6E, (byte) 0xE6, (byte) 0xDD, (byte) 0xDD, (byte) 0xD9, (byte) 0x99,
            (byte) 0xBB, (byte) 0xBB, (byte) 0x67, (byte) 0x63, (byte) 0x6E, (byte) 0x0E, (byte) 0xEC, (byte) 0xCC,
            (byte) 0xDD, (byte) 0xDC, (byte) 0x99, (byte) 0x9F, (byte) 0xBB, (byte) 0xB9, (byte) 0x33, (byte) 0x3E,
    };

    /**
     * Get the ROM size in amount of banks, according to the header.
     *
     * @param data The ROM bank data.
     * @return The ROM size in bytes, or -1 if the detected size is invalid.
     */
    public static int getROMBanksNum(byte[] data) {
        return switch (data[0x148]) {
            case 0x00 -> 2;
            case 0x01 -> 4;
            case 0x02 -> 8;
            case 0x03 -> 16;
            case 0x04 -> 32;
            case 0x05 -> 64;
            case 0x06 -> 128;
            case 0x07 -> 256;
            case 0x08 -> 512;
            // case 0x52 -> 72; // ?
            // case 0x53 -> 80; // ?
            // case 0x54 -> 96; // ?
            default -> -1;
        };
    }

    /**
     * Get the ROM title, according to the header.
     *
     * @param bank The ROM bank to get the data from.
     * @return The ROM title.
     */
    public static String getTitle(ROMBank bank) {
        return new String(Arrays.copyOfRange(bank.bytes(), 0x134, 0x144));
    }

    public static MemoryBankController getMBCType(ROMBank romBank, int numROM, int numRAM)
            throws NotImplementedException, IllegalArgumentException {
        int MBCType = Byte.toUnsignedInt(romBank.bytes()[0x147]);
        return switch (MBCType) {
            case 0x00 -> new NoMBC();

            case 0x01 -> new MBC1(numROM, 0, false);
            case 0x02 -> new MBC1(numROM, numRAM, false);
            case 0x03 -> new MBC1(numROM, numRAM, true);

            case 0x05 -> throw new NotImplementedException("Unimplemented MBC: 'MBC2'");
            case 0x06 -> throw new NotImplementedException("Unimplemented MBC: 'MBC2+BATTERY'");
            //? No licensed games use this
            case 0x08 -> throw new NotImplementedException("Unimplemented MBC: 'ROM+RAM 1'");
            case 0x09 -> throw new NotImplementedException("Unimplemented MBC: 'ROM+RAM+BATTERY 1'");

            case 0x0B -> throw new NotImplementedException("Unimplemented MBC: 'MMM01'");
            case 0x0C -> throw new NotImplementedException("Unimplemented MBC: 'MMM01+RAM'");
            case 0x0D -> throw new NotImplementedException("Unimplemented MBC: 'MMM01+RAM+BATTERY'");

            case 0x0F -> new MBC3(numROM, 0, true, true);
            case 0x10 -> new MBC3(numROM, numRAM, true, true);
            case 0x11 -> new MBC3(numROM, 0, false, false);
            case 0x12 -> new MBC3(numROM, numRAM, false, false);
            case 0x13 -> new MBC3(numROM, numRAM, true, false);

            case 0x19 -> new MBC5(numROM, 0, false, false);
            case 0x1A -> new MBC5(numROM, numRAM, false, false);
            case 0x1B -> new MBC5(numROM, numRAM, true, false);

            case 0x1C -> new MBC5(numROM, 0, false, true);   //? RUMBLE
            case 0x1D -> new MBC5(numROM, numRAM, false, true);           //? RUMBLE
            case 0x1E -> new MBC5(numROM, numRAM, true, true);            //? RUMBLE

            case 0x20 -> throw new NotImplementedException("Unimplemented MBC: 'MBC6'");

            case 0x22 -> throw new NotImplementedException("Unimplemented MBC: 'MBC7+SENSOR+RUMBLE+RAM+BATTERY'");

            case 0xFC -> throw new NotImplementedException("Unimplemented MBC: 'POCKET CAMERA'");
            case 0xFD -> throw new NotImplementedException("Unimplemented MBC: 'BANDAI TAMA5'");

            case 0xFE -> throw new NotImplementedException("Unimplemented MBC: 'HuC3'");
            case 0xFF -> throw new NotImplementedException("Unimplemented MBC: 'HuC1+RAM+BATTERY'");

            default -> throw new IllegalArgumentException("Invalid cartridge type: " + Integer.toHexString(MBCType));
        };
    }

    /**
     * Get the ROM size, according to the header.
     *
     * @param bank The ROM bank to get the data from.
     * @return The ROM size in bytes, or -1 if the detected size is invalid.
     */
    public static int getROMSize(ROMBank bank) {
        return switch (bank.bytes()[0x148]) {
            case 0x00 -> 0x8000;
            case 0x01 -> 0x10000;
            case 0x02 -> 0x20000;
            case 0x03 -> 0x40000;
            case 0x04 -> 0x80000;
            case 0x05 -> 0x100000;
            case 0x06 -> 0x200000;
            case 0x07 -> 0x400000;
            case 0x08 -> 0x800000;
            default -> -1;
        };
    }

    /**
     * Get the ROM size, according to the header.
     *
     * @param bank The ROM bank to get the data from.
     * @return The ROM size as a formatted string.
     */
    public static String getROMSizeString(ROMBank bank) {
        return switch (bank.bytes()[0x148]) {
            case 0x00 -> "32 KiB";
            case 0x01 -> "64 KiB";
            case 0x02 -> "128 KiB";
            case 0x03 -> "256 KiB";
            case 0x04 -> "512 KiB";
            case 0x05 -> "1 MiB";
            case 0x06 -> "2 MiB";
            case 0x07 -> "4 MiB";
            case 0x08 -> "8 MiB";
            default -> "Unknown/Invalid";
        };
    }

    /**
     * Get the RAM size in amount of banks, according to the header.
     *
     * @param bank The ROM bank to get the data from.
     * @return The number or RAM banks, or -1 if the detected size is invalid.
     */
    public static int getRAMBanksNum(ROMBank bank) {
        return switch (bank.bytes()[0x149]) {
            case 0x00 -> 0; // TODO: MBC2 lists $00 while in actuality having 512*4bits of RAM
            // case 0x01 -> ???;
            case 0x02 -> 1;
            case 0x03 -> 4;
            case 0x04 -> 16;
            case 0x05 -> 8;
            default -> -1;
        };
    }

    /**
     * Get the RAM size, according to the header.
     *
     * @param bank The ROM bank to get the data from.
     * @return The RAM size in bytes, or -1 if the detected size is invalid.
     */
    public static int getRAMSize(ROMBank bank) {
        return switch (bank.bytes()[0x149]) {
            case 0x00 -> 0;
            case 0x02 -> 0x2000;
            case 0x03 -> 0x8000;
            case 0x04 -> 0x20000;
            case 0x05 -> 0x10000;
            default -> -1;
        };
    }

    /**
     * Get the ROM size, according to the header.
     *
     * @param bank The ROM bank to get the data from.
     * @return The ROM size as a formatted string.
     */
    public static String getRAMSizeString(ROMBank bank) {
        return switch (bank.bytes()[0x149]) {
            case 0x00 -> "None";
            case 0x02 -> "8 KiB";
            case 0x03 -> "32 KiB";
            case 0x04 -> "128 KiB";
            case 0x05 -> "64 MiB";
            default -> "Unknown/Invalid";
        };
    }

    /**
     * Get the destination code, according to the header.
     *
     * @param bank The ROM bank to get the data from.
     * @return The destination code as a formatted string.
     */
    public static String getDestinationCode(ROMBank bank) {
        return switch (bank.bytes()[0x14a]) {
            case 0x00 -> "Japan";
            case 0x01 -> "International";
            default -> "Invalid";
        };
    }

    /**
     * Get the version number from the header data.
     *
     * @param bank The ROM bank to get the data from.
     * @return The version number as an int.
     */
    public static byte getVersionNumber(ROMBank bank) {
        return bank.bytes()[0x14c];
    }

    /**
     * Get the header checksum from the header data.
     *
     * @param bank The ROM bank to get the data from.
     * @return The header checksum as a byte.
     */
    public static byte getHeaderChecksum(ROMBank bank) {
        return bank.bytes()[0x14d];
    }

    /**
     * Get the global checksum from the header data.
     *
     * @param bank The ROM bank to get the data from.
     * @return The global checksum as a short.
     */
    public static short getGlobalChecksum(ROMBank bank) {
        return Bitwise.toShort(bank.bytes()[0x4e], bank.bytes()[0x14f]);
    }

    /**
     * Check if the Nintendo logo data is valid.
     *
     * @param bank The ROM bank to get the header and check the logo of.
     * @return {@code true} if the bytes match, {@code false} otherwise.
     */
    public static boolean validLogo(ROMBank bank) {
        if (bank == null)
            return false;

        for (int i = 0; i < logo.length; i++) {
            if (bank.bytes()[0x104 + i] != logo[i]) {
                return false;
            }
        }
        return true;
    }

    /**
     * Check if the header checksum of a ROM bank is valid.
     *
     * @param bank The ROM bank to get the header and check the checksum of.
     * @return {@code true} if the checksum matches, {@code false} otherwise.
     */
    public static boolean validHeaderChecksum(ROMBank bank) {
        if (bank == null)
            return false;

        int x = 0;
        int i = 0x134;
        while (i <= 0x14c) {
            x = x - bank.bytes()[i] - 1;
            i++;
        }
        return (byte) x == bank.bytes()[0x14d];
    }
}
