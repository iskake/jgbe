package tland.gb.mem;

/**
 * Game boy memory map.
 */
public class MemoryMap implements Memory {
    private final CartridgeROM rom;
    private final ROMBank bank0;
    private ROMBank bankX;

    public MemoryMap(CartridgeROM rom) {
        this.rom = rom;
        this.bank0 = rom.getBank(0);
        this.bankX = rom.getBank(1);
    }

    @Override
    public int readByte(int index) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeByte(int index, int value) {
        // TODO Auto-generated method stub
    }

    @Override
    public int readShort(int index) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeShort(int index, int value) {
        // TODO Auto-generated method stub
    }
}
