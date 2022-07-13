package iskake.jgbe.core.gb.ppu;

import iskake.jgbe.core.gb.HardwareRegisters;
import iskake.jgbe.core.gb.HardwareRegisters.HardwareRegister;
import iskake.jgbe.core.gb.mem.OAM;
import iskake.jgbe.core.gb.mem.VRAM;
import iskake.jgbe.core.Bitwise;

import java.util.Arrays;

/**
 * Pixel Processing Unit.
 */
public class PPU {
    /** Color based on index. */
    public static final int LCD_SIZE_X = 160;
    public static final int LCD_SIZE_Y = 144;
    private final OAM oam;
    private final VRAM vram;
    private final HardwareRegisters hwreg;
    private final PPUController ppuControl;
    private final byte[] scanlineBuf;
    private final byte[][] scanlines;

    public PPU(VRAM vram, OAM oam, HardwareRegisters hwreg, PPUController ppuControl) {
        this.vram = vram;
        this.oam = oam;
        this.hwreg = hwreg;
        this.ppuControl = ppuControl;

        scanlineBuf = new byte[LCD_SIZE_X];
        scanlines = new byte[LCD_SIZE_Y][LCD_SIZE_X]; // TODO: replace with 1d byte array.
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
     * @return The newly created scanline (also stored in the {@code scanlineBuf}.)
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

        Tile[] tilemapTilesBlockN = getTilesBlockN();
        Tile[] tilemapTilesBlock1 = getTilesBlock1();
        // TODO: handle window/sprites/tilemap enable.
        for (int i = 0; i < LCD_SIZE_X; i++) {
            int dx = (x + i) % 256;
            Tile t = BG.getTileAtCoordinate(dx, y, tilemapTilesBlock1, tilemapTilesBlockN);
            t.decode();
            scanlineBuf[i] = t.getDot(dx % 8, y % 8);
        }
        return scanlineBuf;
    }

    /**
     * Add the a scanline to the current frame.
     * Adds the scanline specified in the LY register, unless the value is >= 144.
     */
    public void addScanline() {
        int line = hwreg.readAsInt(HardwareRegister.LY);

        if (line < LCD_SIZE_Y) {
            Sprite[] sprites = getSprites();
            Tile[] tiles = getTilesBlock1();
            TileMap BG = getBGTileMap();
            TileMap window = getWindowTileMap();

            int scrollY = hwreg.readAsInt(HardwareRegister.SCY);
            int scrollX = hwreg.readAsInt(HardwareRegister.SCX);

            // Update buffer
            createScanline(scrollX, scrollY + line, sprites, tiles, BG, window);
            for (int i = 0; i < LCD_SIZE_X; i++) {
                scanlines[line][i] = scanlineBuf[i];
            }
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

    // TODO: update docs
    /**
     * Get the tiles in VRAM.
     * 
     * @return All tiles in VRAM.
     */
    public Tile[] getTilesBlock1() {
        return vram.getTileBlock1();
    }

    /**
     * Get the tiles based on the 'addressing mode'.
     * 
     * @param tiles The tiles to get the tile array from.
     * @return The BG and Window tilemap tile data.
     */
    private Tile[] getTilesBlockN() {
        int tileDataVal = ppuControl.getBGAndWindowTileBlock();
        return vram.getTileBlock(tileDataVal);
    }

    /**
     * Get the current tilemap used for the background.
     * 
     * @return The current background tilemap.
     */
    public TileMap getBGTileMap() {
        int index = Bitwise.isBitSet(hwreg.readAsInt(HardwareRegister.LCDC), 3) ? 1 : 0;
        return vram.getTileMap(index);
    }

    /**
     * Get the current tilemap used for the window.
     * 
     * @return The current window tilemap.
     */
    public TileMap getWindowTileMap() {
        int index = Bitwise.isBitSet(hwreg.readAsInt(HardwareRegister.LCDC), 6) ? 1 : 0;
        return vram.getTileMap(index);
    }
}
