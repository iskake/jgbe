package iskake.jgbe.core.gb.mem;

import iskake.jgbe.core.gb.ppu.PPUController;
import iskake.jgbe.core.gb.ppu.Tile;
import iskake.jgbe.core.gb.ppu.TileMap;

/**
 * Video memory. Only accessible in modes 0-2 (STAT register bits 0-1).
 */
public class VRAM extends RAM implements ReadableMemoryUnrestricted, WritableMemoryUnrestricted  {
    private final PPUController ppuControl;
    private final Tile[] tileBlock0 = new Tile[0x80];
    private final Tile[] tileBlock1 = new Tile[0x80];
    private final Tile[] tileBlock2 = new Tile[0x80];
    private final Tile[][] tileDataBlocks = { tileBlock0, tileBlock1, tileBlock2 };
    private final TileMap[] tileMaps = new TileMap[2];

    public VRAM(int size, PPUController ppu) {
        super(size);
        this.ppuControl = ppu;
        for (int i = 0; i < 0x80; i++) {
            int offset = i * 16;
            tileBlock0[i] = new Tile(this::readUnrestricted, this::writeUnrestricted, offset);
            tileBlock1[i] = new Tile(this::readUnrestricted, this::writeUnrestricted, 0x800 + offset);
            tileBlock2[i] = new Tile(this::readUnrestricted, this::writeUnrestricted, 0x1000 + offset);
        }
        tileMaps[0] = new TileMap(this::readUnrestricted, this::writeUnrestricted, 0x1800);
        tileMaps[1] = new TileMap(this::readUnrestricted, this::writeUnrestricted, 0x1c00);
    }

    @Override
    public byte read(int address) throws IndexOutOfBoundsException {
        if (!ppuControl.isVRAMAccessible()) {
            return (byte) 0xff;
        }
        return super.read(address);
    }

    @Override
    public byte readUnrestricted(int address) throws IndexOutOfBoundsException {
        return super.read(address);
    }

    @Override
    public void write(int address, byte value) throws IndexOutOfBoundsException {
        if (!ppuControl.isVRAMAccessible()) {
            return;
        }
        super.write(address, value);

        // Since the tile has been written to, it is necessary to decode it again.
        if (address < 0x800) {
            tileBlock0[address / 16].isDecoded = false;
        } else if (address < 0x1000) {
            tileBlock1[(address - 0x800) / 16].isDecoded = false;
        } else if (address < 0x1800) {
            tileBlock2[(address - 0x1000) / 16].isDecoded = false;
        }
    }

    @Override
    public void writeUnrestricted(int address, byte value) throws IndexOutOfBoundsException {
        super.write(address, value);
    }

    public Tile[] getTileBlock(int tileDataVal) {
        return tileDataBlocks[tileDataVal];
    }

    // TODO: update docs
    /**
     * Get the tiles in VRAM.
     * The tiles are stored in the range {@code $8000-$97ff}.
     * 
     * @return The tiles in VRAM.
     */
    public Tile[] getTileBlock0() {
        return tileBlock0;
    }

    /**
     * Get the tiles in VRAM.
     * The tiles are stored in the range {@code $8000-$97ff}.
     * 
     * @return The tiles in VRAM.
     */
    public Tile[] getTileBlock1() {
        return tileBlock1;
    }

    /**
     * Get the tiles in VRAM.
     * The tiles are stored in the range {@code $8000-$97ff}.
     * 
     * @return The tiles in VRAM.
     */
    public Tile[] getTileBlock2() {
        return tileBlock2;
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
        return tileMaps;
    }
}
