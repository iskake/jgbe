package iskake.gb.ppu;

/**
 * Represents a sprite (or, 'object') as specified in OAM.
 */
public class Sprite {
    private byte YPos, XPos, TileIndex, Attributes;

    public Sprite(byte YPos, byte XPos, byte TileIndex, byte Attributes) {
        this.YPos = YPos;
        this.XPos = XPos;
        this.TileIndex = TileIndex;
        this.Attributes = Attributes;
    }

    public Sprite(int oamData) {
        this.YPos = (byte) (oamData >> 24);
        this.XPos = (byte) ((oamData >> 16) & 0xff);
        this.TileIndex = (byte) ((oamData >> 8) & 0xff);
        this.Attributes = (byte) (oamData & 0xff);
    }

    /**
     * Update the sprite's attributes
     * 
     * @param YPos       The Y position of the sprite.
     * @param XPos       The X position of the sprite.
     * @param TileIndex  The Tile index of the sprite
     * @param Attributes The Attributes of the sprite
     */
    public void updateAttributes(Byte YPos, Byte XPos, Byte TileIndex, Byte Attributes) {
        this.YPos = YPos;
        this.XPos = XPos;
        this.TileIndex = TileIndex;
        this.Attributes = Attributes;
    }

    /**
     * Update the sprite's attributes
     * 
     * @param oamData The data of the sprite, stored in a 32-bit int (4 bytes).
     */
    public void updateAttributes(int oamData) {
        this.YPos = (byte) (oamData >> 24);
        this.XPos = (byte) ((oamData >> 16) & 0xff);
        this.TileIndex = (byte) ((oamData >> 8) & 0xff);
        this.Attributes = (byte) (oamData & 0xff);
    }

    /**
     * Get the sprite's Y position.
     * 
     * @return The Y position of the sprite.
     */
    public byte getYPos() {
        return YPos;
    }

    /**
     * Get the sprite's Y position.
     * 
     * @return The X position of the sprite.
     */
    public byte getXPos() {
        return XPos;
    }

    /**
     * Get the sprite's tile index.
     * 
     * @return The tile index of the sprite.
     */
    public byte getTileIndex() {
        return TileIndex;
    }

    /**
     * Get the sprite's attributes.
     * 
     * @return The attributes of the sprite.
     */
    public byte getAttributes() {
        return Attributes;
    }

     @Override
    public String toString() {
        return "Sprite { y:" + Byte.toUnsignedInt(YPos)
                + ", x:" + Byte.toUnsignedInt(XPos)
                + ", index:" + Byte.toUnsignedInt(TileIndex)
                + ", attrs:" + Integer.toBinaryString(Attributes)
                + " }";
    }
}
