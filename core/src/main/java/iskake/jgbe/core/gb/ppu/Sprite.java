package iskake.jgbe.core.gb.ppu;

import iskake.jgbe.core.gb.mem.MappedByte;
import iskake.jgbe.core.gb.mem.ReadableMemory;
import iskake.jgbe.core.gb.mem.WritableMemory;

/**
 * Represents a sprite (or, 'object') as specified in OAM.
 */
public class Sprite {
    private final MappedByte yPos, xPos, tileIndex, attributes;

    public Sprite(ReadableMemory readFunc, WritableMemory writeFunc, int oamIndexStartAddress) {
        this.yPos =       new MappedByte(oamIndexStartAddress + 0, readFunc, writeFunc);
        this.xPos =       new MappedByte(oamIndexStartAddress + 1, readFunc, writeFunc);
        this.tileIndex =  new MappedByte(oamIndexStartAddress + 2, readFunc, writeFunc);
        this.attributes = new MappedByte(oamIndexStartAddress + 3, readFunc, writeFunc);
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
