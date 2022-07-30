package iskake.jgbe.core.gb.ppu;

import iskake.jgbe.core.gb.HardwareRegisters;
import iskake.jgbe.core.gb.HardwareRegisters.HardwareRegister;
import iskake.jgbe.core.gb.mem.OAM;
import iskake.jgbe.core.gb.mem.VRAM;
import iskake.jgbe.core.Bitwise;

import java.util.Arrays;
import java.util.List;

/**
 * Pixel Processing Unit.
 */
public class PPU {
    public static final int LCD_SIZE_X = 160;
    public static final int LCD_SIZE_Y = 144;
    public static final int MAX_SPRITES_ON_SCANLINE = 10;
    private final OAM oam;
    private final VRAM vram;
    private final HardwareRegisters hwreg;
    private final PPUController ppuControl;
    /** Buffer used to hold the raw pixels on the screen (bg tilemap) (each byte is %00-%11). */
    private final byte[] bgBuffer;
    /** Buffer used to hold the raw pixels on the screen (window tilemap) (each byte is %00-%11). */
    private final byte[] winBuffer;
    /** Buffer used to hold the raw pixels on the screen (sprites) (each byte is %00-%11). */
    private final byte[] sprBuffer;
    /** Buffer used to hold the pixels of the screen mapped to RGB values. */
    private final byte[] frameBuffer;

    public PPU(VRAM vram, OAM oam, HardwareRegisters hwreg, PPUController ppuControl) {
        this.vram = vram;
        this.oam = oam;
        this.hwreg = hwreg;
        this.ppuControl = ppuControl;

        bgBuffer = new byte[LCD_SIZE_Y * LCD_SIZE_X];
        winBuffer = new byte[LCD_SIZE_Y * LCD_SIZE_X];
        sprBuffer = new byte[LCD_SIZE_Y * LCD_SIZE_X];
        frameBuffer = new byte[LCD_SIZE_X * LCD_SIZE_Y * 3];
    }

    /**
     * Create a scanline.
     * The scanline is created from the tilemap and sprites + other hw registers.
     * 
     * @param currScanline The current scanline being fetched.
     * @param tiles1       The tiles to use for the tilemap.
     * @param tilesN       The tiles to use for the tilemap.
     * @param bg           The background tilemap to draw.
     */
    private void createScanlineBG(int currScanline, Tile[] tilesN, Tile[] tiles1, TileMap bg) {
        if (!ppuControl.isBGAndWindowEnabled()) {
            for (int i = 0; i < LCD_SIZE_X; i++) {
                // We use -1 for 'invisible' dots.
                bgBuffer[currScanline * LCD_SIZE_X + i] = -1;
            }
            return;
        }

        int SCY = hwreg.readAsInt(HardwareRegister.SCY);
        int SCX = hwreg.readAsInt(HardwareRegister.SCX);

        int bgp = hwreg.readAsInt(HardwareRegister.BGP);

        for (int i = 0; i < LCD_SIZE_X; i++) {
            int dx = (SCX + i) & 255;
            int dy = (SCY + currScanline) & 255;
            Tile t = bg.getTileAtCoordinate(dx, dy, tiles1, tilesN);
            t.decode();
            int colorIndex = Byte.toUnsignedInt(t.getDot(dx % 8, dy % 8));
            bgBuffer[currScanline * LCD_SIZE_X + i] = getDotColor(bgp, colorIndex);
        }
    }

    private void createScanlineWIN(int currScanline, Tile[] tilesN, Tile[] tiles1, TileMap map) {
        int wx = hwreg.readAsInt(HardwareRegister.WX); // TODO: use this to calculate dots
        int wy = hwreg.readAsInt(HardwareRegister.WY); // TODO: use this to calculate dots

        boolean coordinateInRange = (wx <= 166) && (wy <= 143);

        if (!coordinateInRange || !ppuControl.isBGAndWindowEnabled() || !ppuControl.isWindowEnabled()) {
            // We use -1 for 'invisible' dots.
            for (int i = 0; i < LCD_SIZE_X; i++) {
                winBuffer[currScanline * LCD_SIZE_X + i] = -1;
            }
            return;
        }

        int bgp = hwreg.readAsInt(HardwareRegister.BGP);

        for (int i = 0; i < LCD_SIZE_X; i++) {
//            int dx = i & 255;
//            int dy = currScanline & 255;
//            Tile t = map.getTileAtCoordinate(dx, dy, tiles1, tilesN);
//            t.decode();
//            byte dotCol = t.getDot(dx % 8, dy % 8);
            byte dotCol = -1; // TEMP
            winBuffer[currScanline * LCD_SIZE_X + i] = dotCol;
        }
    }

    private void createScanlineSPR(int currScanline, Tile[] tiles0, Tile[] tiles1, Sprite[] sprites) {
        for (int i = 0; i < LCD_SIZE_X; i++) {
            // We use -1 for 'invisible' dots.
            sprBuffer[currScanline * LCD_SIZE_X + i] = -1;
        }

        if (!ppuControl.areOBJsEnabled())
            return;

        int obp0 = hwreg.readAsInt(HardwareRegister.OBP0);
        int obp1 = hwreg.readAsInt(HardwareRegister.OBP1);

        int spriteSize = ppuControl.getOBJSize();

        List<Sprite> spritesToDraw = Arrays.stream(sprites)
                .filter(s -> currScanline >= (Byte.toUnsignedInt(s.getYPos()) - 16)
                        && currScanline < (Byte.toUnsignedInt(s.getYPos()) - 16 + spriteSize))
                .toList();

        for (int i = 0; i < MAX_SPRITES_ON_SCANLINE; i++) {

            if (spritesToDraw.size() < i + 1)
                return;

            Sprite sprite = spritesToDraw.get(i);

            int xPos = Byte.toUnsignedInt(sprite.getXPos());
            int xPosScreen = (xPos - 8);

            int yPos = Byte.toUnsignedInt(sprite.getYPos());
            int yPosScreen = (yPos - 16);

            int attr = Byte.toUnsignedInt(sprite.getAttributes());
            boolean xFilp = Bitwise.isBitSet(attr, Sprite.BIT_FLIP_X);
            boolean yFilp = Bitwise.isBitSet(attr, Sprite.BIT_FLIP_Y);

            // https://gbdev.io/pandocs/OAM.html#byte-2---tile-index
            int tileIdx = Byte.toUnsignedInt(sprite.getTileIndex());
            if (spriteSize == 16) {
                if (!yFilp)
                    tileIdx = (currScanline - yPosScreen) < 8 ? tileIdx & 0xfe : tileIdx | 0x1;
                else
                    tileIdx = (currScanline - yPosScreen) < 8 ? tileIdx | 0x1 : tileIdx & 0xfe;
            }
            Tile tile = Tile.getTileAtIndex(tileIdx, tiles1, tiles0);
            tile.decode();

            if (xPos > 0 && xPos < LCD_SIZE_X + 8) {
                for (int j = xPosScreen; j < (xPosScreen + 8); j++) {
                    if (j < LCD_SIZE_X) {
                        int pal = Bitwise.isBitSet(attr, Sprite.BIT_PALETTE_NO) ? obp1 : obp0;
                        int dx = xFilp ? 7 - (j - xPosScreen) : j - xPosScreen;
                        int dy = yFilp ? 7 - ((currScanline - yPosScreen) % 8) : (currScanline - yPosScreen) % 8;
                        byte dot = tile.getDot(dx, dy);
                        byte color = getDotColorObj(pal, dot);
                        // Allow sprite overlap
                        if (color != -1 && j >= 0)
                            sprBuffer[currScanline * LCD_SIZE_X + j] = color;
                    }
                }
            }
        }
    }

    /**
     * Add a scanline to the current frame.
     * Adds the scanline specified in the LY register, unless the value is >= 144.
     */
    public void addScanline() {
        // TODO: https://gbdev.io/pandocs/Scrolling.html#mid-frame-behavior
        int line = hwreg.readAsInt(HardwareRegister.LY);

        if (line < LCD_SIZE_Y) {
            Sprite[] sprites = getSprites();
            TileMap BG = getBGTileMap();
            TileMap window = getWindowTileMap();

            Tile[] tilesN = getTilesBlockN();
            Tile[] tiles0 = getTilesBlock0();
            Tile[] tiles1 = getTilesBlock1();

            // Update the scanline buffers.
            createScanlineBG(line, tilesN, tiles1, BG);
            createScanlineWIN(line, tilesN, tiles1, window); // TODO: Handle window
            createScanlineSPR(line, tiles0, tiles1, sprites);
        }
    }

    public void clearFrameBuffer() {
        // Temp.
        Arrays.fill(bgBuffer, COLORS_MAP[0]);
        Arrays.fill(winBuffer, COLORS_MAP[0]);
        Arrays.fill(sprBuffer, COLORS_MAP[0]);
    }

    /**
     * Get the correct color of the specified dot, according to the palette.
     *
     * @param pal The palette to get the color from (either of BGP, OBP0, OPB1)
     * @param dot The value of the dot.
     * @return The correct color of the dot.
     */
    private byte getDotColor(int pal, int dot) {
        return (byte) ((pal & (0b11 << (dot * 2))) >> (dot * 2));
    }

    /**
     * Get the correct color of the specified dot, according to the palette.
     *
     * @param pal The palette to get the color from (either of BGP, OBP0, OPB1)
     * @param dot The value of the dot.
     * @return The correct color of the dot.
     */
    private byte getDotColorObj(int pal, int dot) {
        if (dot == 0) return -1;
        return (byte) (((pal & 0xfc) & (0b11 << (dot * 2))) >> (dot * 2));
    }

    /** Color based on index. */
    private static final byte[] COLORS_MAP = {
            (byte) 0xff,
            (byte) 0xaa,
            (byte) 0x55,
            (byte) 0x00,
    };

    public byte[] getMappedFrame() {
        for (int i = 0; i < LCD_SIZE_X * LCD_SIZE_Y * 3; i += 3) {
            byte sprColor = sprBuffer[i / 3];
            byte winColor = winBuffer[i / 3];
            byte bgColor = bgBuffer[i / 3];

            // TODO: Actually handle window dots too.
            if (sprColor != -1) {
                frameBuffer[i] = COLORS_MAP[sprColor];
                frameBuffer[i + 1] = COLORS_MAP[sprColor];
                frameBuffer[i + 2] = COLORS_MAP[sprColor];
            } else if (winColor != -1) {
                frameBuffer[i] = COLORS_MAP[winColor];
                frameBuffer[i + 1] = COLORS_MAP[winColor];
                frameBuffer[i + 2] = COLORS_MAP[winColor];
            } else if (bgColor != -1) {
                frameBuffer[i] = COLORS_MAP[bgColor];
                frameBuffer[i + 1] = COLORS_MAP[bgColor];
                frameBuffer[i + 2] = COLORS_MAP[bgColor];
            } else {
                // Should just be 'white'
                frameBuffer[i] = COLORS_MAP[0];
                frameBuffer[i + 1] = COLORS_MAP[0];
                frameBuffer[i + 2] = COLORS_MAP[0];
            }
        }
        return frameBuffer;
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
    public Tile[] getTilesBlock0() {
        return vram.getTileBlock0();
    }

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
     * @return The BG and Window tilemap tile data.
     */
    public Tile[] getTilesBlockN() {
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
