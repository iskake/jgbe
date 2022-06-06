package iskake.jgbe.core.gb.ppu;

import iskake.jgbe.core.gb.HardwareRegisters;
import iskake.jgbe.core.gb.HardwareRegisters.HardwareRegisterIndex;
import iskake.jgbe.core.gb.mem.OAM;
import iskake.jgbe.core.gb.mem.VRAM;
import iskake.jgbe.core.Bitwise;

import java.awt.*;
import java.util.Arrays;

/**
 * Pixel Processing Unit.
 */
public class PPU {
    /** Color based on index. */
    public static final Color[] colors = { Color.BLACK, Color.DARK_GRAY, Color.LIGHT_GRAY, Color.WHITE };
    public static final int LCD_SIZE_X = 160;
    public static final int LCD_SIZE_Y = 144;
    private final OAM oam;
    private final VRAM vram;
    private final HardwareRegisters hwreg;
    private final PPUController ppuControl;

    private final byte[][] scanlines;

    public PPU(VRAM vram, OAM oam, HardwareRegisters hwreg, PPUController ppuControl) {
        this.vram = vram;
        this.oam = oam;
        this.hwreg = hwreg;
        this.ppuControl = ppuControl;

        scanlines = new byte[LCD_SIZE_Y][LCD_SIZE_X];
    }

    /**
     * Create a scanline.
     * The scanline is created from the tilemap and sprites + other hw registers.
     * 
     * @param x       The x value on the tilemap. (Note: should be the value in SCX)
     * @param y       The y value on the tilemap. (Note: should be the value in SCY)
     * @param sprites The sprites to draw.
     * @param tiles   The tiles to use in the tilemap/sprites.
     * @param BG      The BG tilemap to draw.
     * @param window  The window tielmap to draw.
     * @return The newly created scanline.
     */
    private byte[] createScanline(int x, int y, Sprite[] sprites, Tile[] tiles, TileMap BG, TileMap window) {
        boolean drawWindow = false;
        boolean drawBG = false;
        boolean drawSprites = false;

        int spriteSize = ppuControl.getOBJSize();

        if (ppuControl.isBGAndWindowEnabled()) {
            drawWindow = true;
            drawBG = true;
        }
        if (ppuControl.areOBJsEnabled()) {
            drawSprites = true;
        }
        if (drawWindow && !ppuControl.isWindowEnabled()) {
            drawWindow = false;
        }
        byte[] scanline = new byte[LCD_SIZE_X];

        // TODO: handle window/sprites/tilemap enable.
        for (int i = 0; i < LCD_SIZE_X; i++) {
            int dx = (x + i) % 256;
            Tile[] tilemapTiles = getTilesForTilemap(tiles);
            scanline[i] = BG.getTileAtCoordinate(dx, y, tilemapTiles).getDot(dx % 8, y % 8);
        }
        return scanline;
    }

    /**
     * Add the a scanline to the current frame.
     * Adds the scanline specified in the LY register, unless the value is >= 144.
     */
    public void addScanline() {
        int line = hwreg.readRegisterInt(HardwareRegisterIndex.LY);

        if (line < 144) {
            Sprite[] sprites = getSprites();
            Tile[] tiles = getTiles();
            TileMap BG = getBGTileMap();
            TileMap window = getWindowTileMap();

            int scrollY = hwreg.readRegisterInt(HardwareRegisterIndex.SCY);
            int scrollX = hwreg.readRegisterInt(HardwareRegisterIndex.SCX);

            scanlines[line] = createScanline(scrollX, scrollY + line, sprites, tiles, BG, window);
        }
    }

    /**
     * Get the current frame (all scanlines) as a 2d byte array.
     * 
     * @return The current frame.
     */
    public byte[][] getFrame() {
        // Note: temp.
        return scanlines;
    }

    /**
     * Get the sprites in OAM.
     * 
     * @return All sprites in OAM.
     */
    public Sprite[] getSprites() {
        return oam.getSprites();
    }

    /**
     * Get the tiles in VRAM.
     * 
     * @return All tiles in VRAM.
     */
    public Tile[] getTiles() {
        return vram.getTiles();
    }

    /**
     * Get the tiles based on the 'addressing mode'.
     * 
     * @param tiles The tiles to get the tile array from.
     * @return The BG and Window tilemap tile data.
     */
    private Tile[] getTilesForTilemap(Tile[] tiles) {
        // (See this table if confused: https://gbdev.io/pandocs/Tile_Data.html)
        int tilesStart;
        int tilesEnd;
        int tileDataVal = ppuControl.getBGAndWindowTileArea();

        if (tileDataVal == 0x8000) {
            tilesStart = 0;
            tilesEnd = tilesStart + 0x800 / 16;
            return Arrays.copyOfRange(tiles, tilesStart, tilesEnd);
        } else /* if (tileDataVal == 0x8800) */ {
            // ? There is probably a better/easier way to do this.
            tilesStart = (0x9000 - 0x8000) / 16;
            tilesEnd = tilesStart + 0x800 / 16;
            Tile[] tiles9000 = Arrays.copyOfRange(tiles, tilesStart, tilesEnd);

            tilesStart = (0x8800 - 0x8000) / 16;
            tilesEnd = tilesStart + 0x800 / 16;
            Tile[] tiles8800 = Arrays.copyOfRange(tiles, tilesStart, tilesEnd);

            Tile[] tmpTiles = new Tile[0x1800 / 16];
            System.arraycopy(tiles9000, 0, tmpTiles, 0, tiles9000.length);
            System.arraycopy(tiles8800, 0, tmpTiles, tiles9000.length, tiles8800.length);

            return tmpTiles;
        }
    }

    /**
     * Get the current tilemap used for the background.
     * 
     * @return The current background tilemap.
     */
    public TileMap getBGTileMap() {
        int index = Bitwise.isBitSet(hwreg.readRegisterInt(HardwareRegisterIndex.LCDC), 6) ? 0 : 1;
        return vram.getTileMap(index);
    }

    /**
     * Get the current tilemap used for the window.
     * 
     * @return The current window tilemap.
     */
    public TileMap getWindowTileMap() {
        int index = Bitwise.isBitSet(hwreg.readRegisterInt(HardwareRegisterIndex.LCDC), 6) ? 1 : 0;
        return vram.getTileMap(index);
    }
}
