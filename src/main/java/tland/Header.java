package tland;

import java.util.Arrays;

import tland.emu.mem.ROM;

/**
 * Header information and utilities for a Game Boy cartridge header.
 * <p>
 * The header is stored in ROM at the address range $0100-$014f.
 * 
 * @author Tarjei Landøy
 */
public class Header {
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
    public static byte[] getHeaderData(byte[] data) {
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

    public String getDestinationCode() {
        return switch (data[0x4a]) {
            case 0x00 -> "Japan";
            case 0x01 -> "International";
            default -> "Invalid";
        };
    }

    /**
     * Get the version number from the header data.
     * 
     * @return the version number (stored as an int).
     */
    public byte getVersionNumber() {
        return data[0x4c];
    }

    /**
     * Get the header checksum from the header data.
     * 
     * @return the header checksum.
     */
    public byte getHeaderChecksum() {
        return data[0x4d];
    }

    /**
     * Get the global checksum from the header data.
     * 
     * @return the global checksum.
     */
    public short getGlobalChecksum() {
        return Bitwise.toShort(data[0x4e], data[0x4f]);
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
