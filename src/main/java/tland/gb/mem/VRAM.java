package tland.gb.mem;

import java.util.Arrays;

import tland.Bitwise;
import tland.gb.ppu.Tile;
import tland.gb.ppu.TileMap;

/**
 * Video memory. Only accessable in modes 0-2 (STAT register bits 0-1).
 */
public class VRAM extends RAM {
    private final Tile[] tiles = new Tile[0x180];
    private final TileMap[] tileMaps = new TileMap[2];

    public VRAM(int size) {
        super(size);
        for (int i = 0; i < tiles.length; i++) {
            tiles[i] = new Tile(getByteArray(16));
        }
        tileMaps[0] = new TileMap(getByteArray(32 * 32));
        tileMaps[1] = new TileMap(getByteArray(32 * 32));
    }

    private byte[] getByteArray(int size) {
        byte[] data = new byte[size];
        Arrays.fill(data, (byte) 0);
        return data;
    }

    @Override
    public byte readByte(int address) throws IndexOutOfBoundsException {
        return super.readByte(address);
    }

    @Override
    public void writeByte(int address, byte value) throws IndexOutOfBoundsException {
        super.writeByte(address, value);
    }

    /**
     * Get the tiles in VRAM.
     * The tiles are stored in the range {@code $8000-$97ff}.
     * 
     * @return The tiles in VRAM.
     */
    public Tile[] getTiles() {
        for (int i = 0; i < tiles.length; i++) {
            int offset = i * 16;
            tiles[i].updateAttributes(Arrays.copyOfRange(bytes, offset, offset + 16));
        }
        return tiles;
    }

    /**
     * Get the specified tilemap in VRAM.
     * The tilemaps are stored in the ranges {@code $9800-$9bff} and
     * {@code $9c00-$9fff} respectively.
     * 
     * @param i The index of the tilemap to get.
     * @return The specified tilemap in VRAM.
     */
    public TileMap getTileMap(int i) {
        // update the tilemap if necessary
        int offset = 0;
        byte[] newData = Arrays.copyOfRange(bytes, offset, offset + 0x400);
        if (!Bitwise.byteArrayEquals(tileMaps[i].tileIndices(), newData)) {
            // update tilemap
            tileMaps[i].updateAttributes(newData);
        }

        return tileMaps[i];
    }

    /**
     * Get both of the tilemaps in VRAM.
     * The tilemaps are stored in the ranges {@code $9800-$9bff} and
     * {@code $9c00-$9fff} respectively.
     * 
     * @return Both tilemaps in VRAM.
     */
    public TileMap[] getTileMaps() {
        // update the tilemaps (if necessary)
        getTileMap(0);
        getTileMap(1);

        return tileMaps;
    }
}
