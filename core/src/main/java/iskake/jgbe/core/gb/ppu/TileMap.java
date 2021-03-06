package iskake.jgbe.core.gb.ppu;

import iskake.jgbe.core.gb.mem.MappedByteRange;
import iskake.jgbe.core.gb.mem.ReadableMemory;
import iskake.jgbe.core.gb.mem.WritableMemory;

/**
 * Represents a tilemap as specified in VRAM ({@code $9800-$9bff} and
 * {@code $9c00-$9fff})
 */
public class TileMap {
    /** The horizontal and vertical size of a tilemap in tiles. */
    public static final int TILEMAP_SIZE = 32;

    public final MappedByteRange tileIndices;

    public TileMap(ReadableMemory readFunc, WritableMemory writeFunc, int tileMapStartAddress) {
        this.tileIndices = new MappedByteRange(tileMapStartAddress, TILEMAP_SIZE * TILEMAP_SIZE, readFunc, writeFunc);
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
        int index = TILEMAP_SIZE * dy + dx;

        return tileIndices.get(index);
    }

    /**
     * Get the tile at the specified coordinate.
     * 
     * @param x      The x coordinate to get the tile index from.
     * @param y      The y coordinate to get the tile index from.
     * @param tiles1 'Tile block 1' of VRAM, used to get the correct tile.
     * @param tilesN 'Tile block N' of VRAM, used to get the correct tile.
     * @return The tile at the specified coordinate.
     */
    public Tile getTileAtCoordinate(int x, int y, Tile[] tiles1, Tile[] tilesN) {
        int index = Byte.toUnsignedInt(getTileIndexAtCoordinate(x, y));
        return Tile.getTileAtIndex(index, tiles1, tilesN);
    }
}
