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

    private final short[] decodedLines;

    public Tile(ReadableMemory readFunc, WritableMemory writeFunc, int tileStartAddress) {
        this.data = new MappedByteRange(tileStartAddress, 16, readFunc, writeFunc);
        decodedLines = new short[TILE_SIZE];
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
        // ?Should this loop be unrolled as well?
        for (int i = 0; i < TILE_SIZE; i++) {
            int offset = i * 2;
            int b1 = Byte.toUnsignedInt(data.get(offset));
            int b2 = Byte.toUnsignedInt(data.get(offset + 1));

            b1 = ((b1 & 0b10000000) << 7
                    | (b1 & 0b01000000) << 6
                    | (b1 & 0b00100000) << 5
                    | (b1 & 0b00010000) << 4
                    | (b1 & 0b00001000) << 3
                    | (b1 & 0b00000100) << 2
                    | (b1 & 0b00000010) << 1
                    | (b1 & 0b00000001));
            b2 = ((b2 & 0b10000000) << 8
                    | (b2 & 0b01000000) << 7
                    | (b2 & 0b00100000) << 6
                    | (b2 & 0b00010000) << 5
                    | (b2 & 0b00001000) << 4
                    | (b2 & 0b00000100) << 3
                    | (b2 & 0b00000010) << 2
                    | (b2 & 0b00000001) << 1);

            decodedLines[i] = (short) (b1 | b2);
        }

        isDecoded = true;
    }

    /**
     * Get the decoded line of dots specified.
     * 
     * @param y The line to get.
     * @return The line of dots corresponding to the y value.
     */
    public int getLine(int y) throws IndexOutOfBoundsException {
        return Short.toUnsignedInt(decodedLines[y]);
    }

    /**
     * Get the dot ('pixel') at the tile x/y coordinate.
     * 
     * @param x The x coordinate on the tile.
     * @param y The y coordinate on the tile.
     * @return The dot on the specified coordinate.
     */
    public byte getDot(int x, int y) throws IndexOutOfBoundsException {
        int line = getLine(y);
        int pixel = 0xe - (x << 1);
        return (byte)((line & (0b11 << pixel)) >> pixel);
    }

    public static Tile getTileAtIndex(int index, Tile[] tiles1, Tile[] tilesN) {
        if (index < 128)
            return tilesN[index];
        else
            return tiles1[index - 128];
    }
}
