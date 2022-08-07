package iskake.jgbe.core.gb.ppu;

import iskake.jgbe.core.gb.HardwareRegisters;
import iskake.jgbe.core.gb.HardwareRegisters.HardwareRegister;
import iskake.jgbe.core.gb.mem.OAM;
import iskake.jgbe.core.gb.mem.VRAM;
import iskake.jgbe.core.Bitwise;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

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
    private final ArrayList<Sprite> scanlineSprites;
    /** Buffer used to hold the pixels of the screen mapped to RGB values. */
    private final byte[] frameBuffer;
    private int windowInternalLineCount;

    public boolean drawBG = true;
    public boolean drawWin = true;
    public boolean drawSpr = true;

    public PPU(VRAM vram, OAM oam, HardwareRegisters hwreg, PPUController ppuControl) {
        this.vram = vram;
        this.oam = oam;
        this.hwreg = hwreg;
        this.ppuControl = ppuControl;

        bgBuffer = new byte[LCD_SIZE_Y * LCD_SIZE_X];
        winBuffer = new byte[LCD_SIZE_Y * LCD_SIZE_X];
        sprBuffer = new byte[LCD_SIZE_Y * LCD_SIZE_X];
        frameBuffer = new byte[LCD_SIZE_X * LCD_SIZE_Y * 3];
        scanlineSprites = new ArrayList<>(OAM.NUM_SPRITES);
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
    private void createScanlineBG(int currScanline, int start, int length, Tile[] tilesN, Tile[] tiles1, TileMap bg) {
        if ((start + length) > LCD_SIZE_X)
            return;

        if (!ppuControl.isLCDEnabled() || !ppuControl.isBGAndWindowEnabled()) {
            for (int i = start; i < start + length; i++) {
                // We use -1 for 'invisible' dots.
                bgBuffer[currScanline * LCD_SIZE_X + i] = -1;
            }
            return;
        }

        int scy = hwreg.readAsInt(HardwareRegister.SCY);
        int scx = hwreg.readAsInt(HardwareRegister.SCX);

        int bgp = hwreg.readAsInt(HardwareRegister.BGP);

        for (int i = start; i < start + length; i++) {
            int dx = (scx + i) & 255;
            int dy = (scy + currScanline) & 255;
            Tile t = bg.getTileAtCoordinate(dx, dy, tiles1, tilesN);
            t.decode();
            int colorIndex = Byte.toUnsignedInt(t.getDot(dx % 8, dy % 8));
            bgBuffer[currScanline * LCD_SIZE_X + i] = getDotColor(bgp, colorIndex);
        }
    }

    private void createScanlineWIN(int currScanline, int start, int length, Tile[] tilesN, Tile[] tiles1, TileMap map) {
        if ((start + length) > LCD_SIZE_X)
            return;

        int wy = hwreg.readAsInt(HardwareRegister.WY);
        int wx = hwreg.readAsInt(HardwareRegister.WX);
        int wxScreen = wx - 7;

        boolean coordinateInRange = (wx <= 166) && (wy <= 143);

        if (!ppuControl.isLCDEnabled()
                || currScanline < wy
                || !coordinateInRange
                || !ppuControl.isBGAndWindowEnabled()
                || !ppuControl.isWindowEnabled()) {
            // We use -1 for 'invisible' dots.
            for (int i = start; i < start + length; i++) {
                winBuffer[currScanline * LCD_SIZE_X + i] = -1;
            }
            return;
        }

        int bgp = hwreg.readAsInt(HardwareRegister.BGP);

        for (int i = start; i < start + length; i++) {
            byte dotCol;
            if (i < wxScreen) {
                dotCol = -1;
            } else {
                int dx = (i - wxScreen) & 255;
                int dy = windowInternalLineCount;
                Tile t = map.getTileAtCoordinate(dx, dy, tiles1, tilesN);
                t.decode();
                byte dot = t.getDot(dx % 8, dy % 8);
                dotCol = getDotColor(bgp, dot);
            }
            winBuffer[currScanline * LCD_SIZE_X + i] = dotCol;
        }
        windowInternalLineCount++;
    }

    private void createScanlineSPR(int currScanline, int start, int length, Tile[] tiles0, Tile[] tiles1, Sprite[] sprites) {
        // TODO: implement actual pixel FIFO behaviour for sprites...
        for (int i = 0; i < LCD_SIZE_X; i++) {
            // We use -1 for 'invisible' dots.
            sprBuffer[currScanline * LCD_SIZE_X + i] = -1;
        }

        if (!ppuControl.isLCDEnabled() || !ppuControl.areOBJsEnabled())
            return;

        int obp0 = hwreg.readAsInt(HardwareRegister.OBP0);
        int obp1 = hwreg.readAsInt(HardwareRegister.OBP1);

        int spriteSize = ppuControl.getOBJSize();

        int c = 0;
        scanlineSprites.clear();
        for (Sprite s : sprites) {
            if (currScanline >= (s.getYPosInt() - 16) && currScanline < (s.getYPosInt() - 16 + spriteSize)) {
                scanlineSprites.add(c, s);
                c++;
            }

            if (c == 10)
                break;
        }
        if (c == 0)
            return;

        scanlineSprites.sort(Comparator.comparingInt(Sprite::getXPosInt));

        // TODO: actually handle sprite priority (OAM index) instead of looping backwards
        for (int i = c - 1; i >= 0; i--) {
            Sprite sprite = scanlineSprites.get(i);

            int xPos = sprite.getXPosInt();
            int xPosScreen = (xPos - 8);

            int yPos = sprite.getYPosInt();
            int yPosScreen = (yPos - 16);

            int attr = Byte.toUnsignedInt(sprite.getAttributes());

            boolean sprUnderBGWin = Bitwise.isBitSet(attr, Sprite.BIT_BG_WIN_OVER);
            boolean xFilp = Bitwise.isBitSet(attr, Sprite.BIT_FLIP_X);
            boolean yFilp = Bitwise.isBitSet(attr, Sprite.BIT_FLIP_Y);
            int pal = Bitwise.isBitSet(attr, Sprite.BIT_PALETTE_NO) ? obp1 : obp0;

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
                        int dx = xFilp ? 7 - (j - xPosScreen) : j - xPosScreen;
                        int dy = yFilp ? 7 - ((currScanline - yPosScreen) % 8) : (currScanline - yPosScreen) % 8;
                        byte dot = tile.getDot(dx, dy);
                        byte color = getDotColorObj(pal, dot);
                        // Allow sprite overlap
                        if (color != -1 && j >= 0) {
                            // TODO: priority when s1.x == s2.x
                            // TODO: also... rework sprite layer handling, don't use wierd bit masking on colors...
                            color = sprUnderBGWin ? (byte)(color | 0b1000) : color;
                            sprBuffer[currScanline * LCD_SIZE_X + j] = color;
                        }
                    }
                }
            }
        }
    }

    /**
     * Add a scanline to the current frame.
     * Adds the scanline specified in the LY register, unless the value is >= 144.
     */
    public void addScanline(int start, int length) {
        // TODO: https://gbdev.io/pandocs/Scrolling.html#mid-frame-behavior
        int line = hwreg.readAsInt(HardwareRegister.LY);

        if (line == 0) {
            windowInternalLineCount = 0;
        }

        if (line < LCD_SIZE_Y) {
            Sprite[] sprites = getSprites();
            TileMap BG = getBGTileMap();
            TileMap window = getWindowTileMap();

            Tile[] tilesN = getTilesBlockN();
            Tile[] tiles0 = getTilesBlock0();
            Tile[] tiles1 = getTilesBlock1();

            // Update the scanline buffers.
            createScanlineBG(line, start, length, tilesN, tiles1, BG);
            createScanlineWIN(line, start, length, tilesN, tiles1, window);
            createScanlineSPR(line, start, length, tiles0, tiles1, sprites);
        }
    }

    /**
     * Clear the entire framebuffer.
     * Internally, this sets each of the scanline buffers of the three layers to 'no color'.
     */
    public void clearFrameBuffer() {
        Arrays.fill(bgBuffer,  (byte)-1);
        Arrays.fill(winBuffer, (byte)-1);
        Arrays.fill(sprBuffer, (byte)-1);
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
            // TODO: Handle more colors? (such as on sgb)
            (byte) 0xff,
            (byte) 0xaa,
            (byte) 0x55,
            (byte) 0x00,
    };

    /**
     * Get the current frame mapped to the colors corresponding to the
     * values specified in BGP, OBP0, and OPB1
     *
     * @return The current frame mapped with colors.
     */
    public byte[] getMappedFrame() {
        for (int i = 0; i < LCD_SIZE_X * LCD_SIZE_Y * 3; i += 3) {
            byte sprColor = sprBuffer[i / 3];
            byte winColor = winBuffer[i / 3];
            byte bgColor = bgBuffer[i / 3];

            // TODO: don't use weird color hack to find out if the sprite is underneath...
            boolean spriteUnder = sprColor > 0b11;
            boolean drawSpriteUnder = (winColor <= 0 && bgColor <= 0) || !spriteUnder;

            if (sprColor != -1 && drawSpriteUnder && drawSpr) {
                sprColor = spriteUnder ? (byte)(Byte.toUnsignedInt(sprColor) & 0b11) : sprColor;
                frameBuffer[i] = COLORS_MAP[sprColor];
                frameBuffer[i + 1] = COLORS_MAP[sprColor];
                frameBuffer[i + 2] = COLORS_MAP[sprColor];
            } else if (winColor != -1 && drawWin) {
                frameBuffer[i] = COLORS_MAP[winColor];
                frameBuffer[i + 1] = COLORS_MAP[winColor];
                frameBuffer[i + 2] = COLORS_MAP[winColor];
            } else if (bgColor != -1 && drawBG) {
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

    /**
     * Get 'tile block 0', tiles in the range 0x8000..0x87ff
     *
     * @return The tiles in the correct range.
     */
    public Tile[] getTilesBlock0() {
        return vram.getTileBlock0();
    }

    /**
     * Get 'tile block 1', tiles in the range 0x8800..0x8fff
     *
     * @return The tiles in the correct range.
     */
    public Tile[] getTilesBlock1() {
        return vram.getTileBlock1();
    }

    /**
     * Get 'tile block N (0 or 2)', tiles in the range of either
     * 0x8000..0x87ff or 0x9000..0x97ff depending on the current BG and Window tile range.
     *
     * @return The tiles in the correct range.
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
