package iskake.jgbe.core.gb.ppu;

import iskake.jgbe.core.gb.mem.MappedByte;
import iskake.jgbe.core.gb.mem.ReadableMemory;
import iskake.jgbe.core.gb.mem.WritableMemory;

/**
 * Represents a sprite (or, 'object') as specified in OAM.
 */
public class Sprite {
    public static final int MASK_PALETTE_NO_CGB = 0b0000_0111;
    public static final int MASK_TILE_BANK_CGB  = 0b0000_1000;
    public static final int MASK_PALETTE_NO     = 0b0001_0000;
    public static final int MASK_FLIP_X         = 0b0010_0000;
    public static final int MASK_FLIP_Y         = 0b0100_0000;
    public static final int MASK_BG_WIN_OVER    = 0b1000_0000;

    public static final int BIT_TILE_BANK_CGB  = 3;
    public static final int BIT_PALETTE_NO     = 4;
    public static final int BIT_FLIP_X         = 5;
    public static final int BIT_FLIP_Y         = 6;
    public static final int BIT_BG_WIN_OVER    = 7;

    private final MappedByte yPos, xPos, tileIndex, attributes;

    public Sprite(ReadableMemory readFunc, WritableMemory writeFunc, int oamIndex) {
        this.yPos =       new MappedByte(oamIndex, readFunc, writeFunc);
        this.xPos =       new MappedByte(oamIndex + 1, readFunc, writeFunc);
        this.tileIndex =  new MappedByte(oamIndex + 2, readFunc, writeFunc);
        this.attributes = new MappedByte(oamIndex + 3, readFunc, writeFunc);
    }

    /**
     * Get the sprite's Y position.
     * 
     * @return The Y position of the sprite.
     */
    public byte getYPos() {
        return yPos.get();
    }

    /**
     * Get the sprite's Y position.
     * 
     * @return The X position of the sprite.
     */
    public byte getXPos() {
        return xPos.get();
    }

    /**
     * Get the sprite's tile index.
     * 
     * @return The tile index of the sprite.
     */
    public byte getTileIndex() {
        return tileIndex.get();
    }

    /**
     * Get the sprite's attributes.
     * 
     * @return The attributes of the sprite.
     */
    public byte getAttributes() {
        return attributes.get();
    }

    @Override
    public String toString() {
        return "Sprite { y:" + Byte.toUnsignedInt(getYPos())
                + ", x:" + Byte.toUnsignedInt(getXPos())
                + ", tile:" + Byte.toUnsignedInt(getTileIndex())
                + ", attrs:" + Integer.toBinaryString(getAttributes())
                + " }";
    }
}
