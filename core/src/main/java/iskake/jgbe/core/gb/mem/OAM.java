package iskake.jgbe.core.gb.mem;

import iskake.jgbe.core.gb.ppu.PPUController;
import iskake.jgbe.core.gb.ppu.Sprite;

/**
 * Object Attribute Memory ('Sprite Attribute Table'). 
 * Only accessible in modes 0-1 (STAT register bits 0-1).
 */
public class OAM extends RAM {
    private final PPUController ppuControl;
    private final Sprite[] sprites = new Sprite[40];

    public OAM(int size, PPUController ppuControl) {
        super(size);
        this.ppuControl = ppuControl;
        for (int i = 0; i < sprites.length; i++) {
            sprites[i] = new Sprite(0xffffffff);
        }
    }

    @Override
    public byte read(int address) throws IndexOutOfBoundsException {
        if (!ppuControl.isOAMAccessable()) {
            return (byte) 0xff;
        }
        return super.read(address);
    }

    @Override
    public void write(int address, byte value) throws IndexOutOfBoundsException {
        if (!ppuControl.isOAMAccessable()) {
            return;
        }
        super.write(address, value);
    }

    /**
     * Get all sprites in OAM.
     * 
     * @return All sprites in OAM.
     */
    public Sprite[] getSprites() {
        for (int i = 0; i < sprites.length; i++) {
            int offset = i*4;
            byte YPos = read(offset);
            byte XPos = read(offset+1);
            byte TileIndex = read(offset+2);
            byte Attributes = read(offset+3);
            sprites[i].updateAttributes(YPos, XPos, TileIndex, Attributes);
        }
        return sprites;
    }
}
