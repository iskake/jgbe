package iskake.jgbe.core.gb.mem;

import iskake.jgbe.core.gb.ppu.PPUController;
import iskake.jgbe.core.gb.ppu.Sprite;

/**
 * Object Attribute Memory ('Sprite Attribute Table'). 
 * Only accessible in modes 0-1 (STAT register bits 0-1).
 */
public class OAM extends RAM implements ReadableMemoryUnrestricted, WritableMemoryUnrestricted {
    private final PPUController ppuControl;
    private final Sprite[] sprites = new Sprite[40];

    public OAM(int size, PPUController ppuControl) {
        super(size);
        this.ppuControl = ppuControl;
        for (int i = 0; i < sprites.length; i++) {
            sprites[i] = new Sprite(this::readUnrestricted, this::writeUnrestricted, i*4);
        }
    }

    @Override
    public byte read(int address) throws IndexOutOfBoundsException {
        if (!ppuControl.isOAMAccessible()) {
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
        if (!ppuControl.isOAMAccessible()) {
            return;
        }
        super.write(address, value);
    }

    @Override
    public void writeUnrestricted(int address, byte value) throws IndexOutOfBoundsException {
        super.write(address, value);
    }

    /**
     * Get all sprites in OAM.
     * 
     * @return All sprites in OAM.
     */
    public Sprite[] getSprites() {
        return sprites;
    }
}
