package tland.gb.ppu;

/**
 * Represents a tilemap as specified in VRAM ({@code $9800-$9bff} and
 * {@code $9c00-$9fff})
 */
public record TileMap(byte[] tileIndices) {
    public static final int TILEMAP_SIZE = 256;

    /**
     * Update the tiles.
     * 
     * @param tilesData The new tiles.
     */
    public void updateAttributes(byte[] data) {
        System.arraycopy(data, 0, tileIndices, 0, tileIndices.length);
    }

    /**
     * Get the tile index at the coordinate (or, at the 'dot'.)
     * 
     * @param x The x coordinate to get the tile index from.
     * @param y The y coordinate to get the tile index from.
     * @return The tile index at the specified coordinate.
     */
    private byte getTileIndexAtCoordinate(int x, int y) {
        int dx = x / 8;
        int dy = y / 8;
        int index = (TILEMAP_SIZE / Tile.TILE_SIZE) * dy + dx;

        return tileIndices[index];
    }

    /**
     * Get the tile at the specified coordinate.
     * 
     * @param x     The x coordinate to get the tile index from.
     * @param y     The y coordinate to get the tile index from.
     * @param tiles The tiles apply the tile index to.
     * @return The tile at the specified coordinate.
     */
    public Tile getTileAtCoordinate(int x, int y, Tile[] tiles) {
        return tiles[getTileIndexAtCoordinate(x, y)];
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TileMap t) {
            for (int i = 0; i < tileIndices.length; i++) {
                if (tileIndices[i] != t.tileIndices[i]) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}
