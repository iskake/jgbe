package iskake;

import java.util.Arrays;

import iskake.gb.mem.ROM;

/**
 * The point of this class is to extract information about ROM files that are
 * idenitied as Game Boy ROM files (which JGBE _can_ run, but is unsupported) so
 * we can warn the user they are trying to run a Game Boy ROM (.gb or .gbc file)
 * instead of a JGBE binary file (.zb file)
 * <p>
 * The header is stored in ROM at the address range $0100-$014f.
 * See https://gbdev.io/pandocs/The_Cartridge_Header.html for more information.
 */
public class Header {
    /**
     * Compressed Nintendo logo data.
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
     * Rom header data, specified in the constructor.
     */
    private byte[] data;

    public Header(byte[] data) {
        this.data = data;
    }

    public Header(ROM ROM) {
        this.data = getHeaderData(ROM.data());
    }

    /**
     * Get the header data from the ROM.
     * 
     * @param data The data to get the header from.
     * @return The header data from the rom.
     */
    private static byte[] getHeaderData(byte[] data) {
        return Arrays.copyOfRange(data, 0x100, 0x14f);
    }

    /**
     * Get the ROM title, according to the header.
     * 
     * @return The ROM title.
     */
    public String getTitle() {
        byte[] str = Arrays.copyOfRange(data, 0x34, 0x44);
        return new String(str);
    }

    /**
     * Get the ROM size, according to the header.
     * 
     * @return The ROM size in bytes, or -1 if the detected size is invalid.
     */
    public int getROMSize() {
        return switch (data[0x48]) {
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
     * @return The ROM size as a formatted string.
     */
    public String getROMSizeString() {
        return switch (data[0x48]) {
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
     * Get the ROM size, according to the header.
     * 
     * @return The ROM size in bytes, or -1 if the detected size is invalid.
     */
    public int getRAMSize() {
        return switch (data[0x49]) {
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
     * @return The ROM size as a formatted string.
     */
    public String getRAMSizeString() {
        return switch (data[0x49]) {
            case 0x00 -> "None";
            case 0x02 -> "8 KiB";
            case 0x03 -> "32 KiB";
            case 0x04 -> "128 KiB";
            case 0x05 -> "64 MiB";
            default -> "Unknown/Invalid";
        };
    }

    /**
     * Check if the Nintendo logo data is valid.
     * 
     * @param logoData The header data to check the logo of.
     * @return {@code true} if the bytes match, {@code false} otherwise.
     */
    public static boolean validLogo(byte[] logoData) {
        if (logoData == null)
            return false;

        for (int i = 0; i < logo.length; i++) {
            if (logoData[i] != logo[i]) {
                return false;
            }
        }
        return true;
    }

    /**
     * Check if the Nintendo logo data is valid.
     * 
     * @param rom The ROM to get the header and check the logo of.
     * @return {@code true} if the bytes match, {@code false} otherwise.
     */
    public static boolean validLogo(ROM rom) {
        if (rom == null)
            return false;

        byte[] logoData = Arrays.copyOfRange(rom.data(), 0x104, 0x134);
        return validLogo(logoData);
    }

    /**
     * Check if the header checksum is valid.
     * 
     * @param headerData The header data to check the checksum of.
     * @return {@code true} if the checksum matches, {@code false} otherwise.
     */
    public static boolean validHeaderChecksum(byte[] headerData) {
        if (headerData == null)
            return false;

        int x = 0;
        int i = 0x34;
        while (i <= 0x4c) {
            x = x - headerData[i] - 1;
            i++;
        }
        return (byte) x == headerData[0x4d];
    }

    /**
     * Check if the header checksum of a ROM is valid.
     * 
     * @param rom The ROM to get the header and check the checksum of.
     * @return {@code true} if the checksum matches, {@code false} otherwise.
     */
    public static boolean validHeaderChecksum(ROM rom) {
        if (rom == null)
            return false;

        return validHeaderChecksum(Arrays.copyOfRange(rom.data(), 0x100, 0x150));
    }
}
