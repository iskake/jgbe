package iskake.gb.ppu;

import iskake.Bitwise;

/**
 * Represents a tile as specified in VRAM ({@code $8000-$97ff})
 */
public record Tile(byte[] data) {
    public static final int TILE_SIZE = 8;

    /**
     * Update the tile data.
     * 
     * @param tileData The data of the tile.
     */
    public void updateAttributes(byte[] tileData) {
        System.arraycopy(tileData, 0, data, 0, data.length);
    }

    /**
     * Decode the tile from a 2bpp format to a indexed byte format.
     * 
     * @return The decoded image in a byte indexed format.
     */
    public byte[] decoded() {
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

        byte[] dots = new byte[8 * 8];

        // ? This algorithm can probably be optimized more...
        for (int i = 0; i < TILE_SIZE; i++) {
            int offset = i * 2;
            byte b1 = data[offset];
            byte b2 = data[offset + 1];

            // Decode each 'line' of dots
            for (int j = 7; j >= 0; j--) {
                int b1_ = Bitwise.isBitSet(b1, j) ? 1 : 0;
                int b2_ = (Bitwise.isBitSet(b2, j) ? 1 : 0) << 1;
                int index = (i * 8) + (7 - j);
                dots[index] = (byte) (b2_ | b1_);
            }
        }
        return dots;
    }

    /**
     * Get the dot ('pixel') at the tile x/y coordinate.
     * 
     * @param x The x coordinate on the tile.
     * @param y The y coordinate on the tile.
     * @return The dot on the specified coordinate.
     */
    public byte getDot(int x, int y) {
        return decoded()[TILE_SIZE * y + x];
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Tile t) {
            return Bitwise.byteArrayEquals(data, t.data());
        }
        return false;
    }
}
