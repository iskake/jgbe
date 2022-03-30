package tland.gb.mem;

import tland.Bitwise;

/**
 * Game boy memory map.
 */
public class MemoryMap implements WritableMemory, ReadableMemory {
    private final CartridgeROM rom;

    private RAM VRAM;
    private RAM WRAM1;
    private RAM WRAM2;
    // private RAM OAM;
    // private RAM prohibited;
    // private RAM IO;
    private RAM HRAM;
    // private RAM IE;

    private int fixedAddress;

    public MemoryMap(CartridgeROM rom) {
        this.rom = rom;
        VRAM = new RAM(0x2000);
        WRAM1 = new RAM(0x1000);
        WRAM2 = new RAM(0x1000);
        // OAM = new RAM(0xa0);
        // IO = new RAM(0x80);
        HRAM = new RAM(0x200);
    }

    @Override
    public byte readByte(int address) {
        ReadableMemory memory = (ReadableMemory)getMemoryIndex(address);
        return memory.readByte(fixedAddress);
    }

    @Override
    public void writeByte(int address, byte value) {
        WritableMemory memory = (WritableMemory)getMemoryIndex(address);
        memory.writeByte(fixedAddress, value);
    }

    /**
     * Get the 'correct memory' mapped based on the specified address.
     * <p>
     * Note that this will also set {@code fixedAddress} to the correct relative
     * address for indexing the mapped memory.
     * 
     * @param address The address to get the memory from.
     * @return The correct memory at the specified address.
     */
    private Object getMemoryIndex(int address) {
        address = Bitwise.intAsShort(address);
        fixedAddress = address;
        if (address >= 0 && address < 0x8000) {
            // 0000-3fff = 4000
            // ROM bank0-n, (note that MBC will control )
            return rom;
        } else if (address < 0xA000) {
            // 8000-9fff = 2000
            // VRAM 
            fixedAddress -= 0x8000;
            return VRAM;
        } else if (address < 0xC000) {
            // a000-bfff = 2000
            // Optional switchable bank from Cartridge
            fixedAddress -= 0xa000;
            return rom.getRAMBank();
        } else if (address < 0xD000) {
            // c000-cfff = 1000
            fixedAddress -= 0xc000;
            return WRAM1;
        } else if (address < 0xE000) {
            // d000-dfff = 1000
            fixedAddress -= 0xd000;
            return WRAM2;
        } else if (address < 0xFE00) {
            // e000-fdff = 1e00
            // (Mirror of c000-ddff)
            // System.out.println("Accessing ECHO RAM.");
            fixedAddress -= 0xe000;
            return WRAM1;
        // TODO
        /* } else if (address < 0xFEA0) {
            // fe00-fe9f = a0
            fixedAddress -= 0xfe00;
            return OAM;
        } else if (address < 0xFF00) {
            // fea0-feff = 60
            // ('prohibited access')
            fixedAddress -= 0xfea0;
            return prohibited;
        } else if (address < 0xFF80) {
            // ff00-ff7f = 80
            fixedAddress -= 0xff00;
            return IO;
        } else if (address < 0xFFFF) {
            // ff80-fffe
            fixedAddress -= 0xff80;
            return HRAM; */
        } else {
            // fe00-ffff
            fixedAddress -= 0xfe00;
            return HRAM;
        }
    }
}
