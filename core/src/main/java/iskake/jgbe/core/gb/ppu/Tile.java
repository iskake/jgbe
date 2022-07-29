package iskake.jgbe.core.gb.ppu;

import iskake.jgbe.core.gb.mem.MappedByteRange;
import iskake.jgbe.core.gb.mem.ReadableMemory;
import iskake.jgbe.core.gb.mem.WritableMemory;

/**
 * Represents a tile as specified in VRAM ({@code $8000-$97ff})
 */
public class Tile {
    public static final int TILE_SIZE = 8;

    public boolean isDecoded = false;
    public final MappedByteRange data;
    private final byte[] dots;

    public Tile(ReadableMemory readFunc, WritableMemory writeFunc, int tileStartAddress) {
        this.data = new MappedByteRange(tileStartAddress, 16, readFunc, writeFunc);
        dots = new byte[TILE_SIZE * TILE_SIZE];
    }

    /**
     * Decode the tile from a 2bpp format to a indexed byte format.
     */
    public void decode() {
        if (isDecoded) {
            return;
        }

        /*
         * Decoding process:
         *   $db, $7e = %11011011, %01111110
         *        Combine:
         *   %11011011 x %01111110
         *             |
         *             v
         *   %01 11 10 11 11 10 11 01
         * Example:
         *   bd 7e -> 10111101 01111110 -> 0110111111111001
         *   42 81 -> 01000010 10000001 -> 1001000000000010
         *   81 81 -> 10000001 10000001 -> 1100110000110011
         *   e7 e7 -> 11100111 11100111 -> 1100110000110011
         *   81 81 -> 10000001 10000001 -> 1100000000000011
         *   99 99 -> 10011001 10011001 -> 1100110000110011
         *   42 81 -> 01000010 10000001 -> 1001001111000110
         *   bd 7e -> 10111101 01111110 -> 0110111111111001
         * 
         * Note that: image colors are based on the value in the OBP and BGP hw registers.)
         * See: https://gbdev.io/pandocs/Tile_Data.html
         */
        for (int i = 0; i < TILE_SIZE; i++) {
            int offset = i * 2;
            int b1 = Byte.toUnsignedInt(data.get(offset));
            int b2 = Byte.toUnsignedInt(data.get(offset + 1));

            for (int j = 7; j >= 0; j--) {
                int mask = 1 << j;
                int b1_ = (b1 & mask) >> j;
                int b2_ = ((b2 & mask) << 1) >> j;
                dots[i * TILE_SIZE + (7 - j)] = (byte)(b1_ | b2_);
            }
        }

        isDecoded = true;
    }

    /**
     * Get the dot ('pixel') at the tile x/y coordinate.
     * 
     * @param x The x coordinate on the tile.
     * @param y The y coordinate on the tile.
     * @return The dot on the specified coordinate.
     */
    public byte getDot(int x, int y) throws IndexOutOfBoundsException {
        return dots[y * TILE_SIZE + x];
    }

    /**
     * Get the decoded dots ('pixels') of this tile.
     *
     * @return The decoded dots of this tile.
     */
    public byte[] getDots() {
        return dots;
    }

    public static Tile getTileAtIndex(int index, Tile[] tiles1, Tile[] tilesN) {
        if (index < 128)
            return tilesN[index];
        else
            return tiles1[index - 128];
    }
}
