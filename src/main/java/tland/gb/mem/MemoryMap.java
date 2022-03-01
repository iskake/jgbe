package tland.gb.mem;

import tland.Bitwise;
import tland.gb.GameBoy;

/**
 * Game boy memory map.
 */
public class MemoryMap implements WritableMemory, ReadableMemory {
    private final CartridgeROM rom;

    private final ROMBank bank0;
    private ROMBank bankX;
    private RAM VRAM;
    private RAM ExtRAM;
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
        bank0 = rom.getBank(0);
        bankX = rom.getBank(1);
        VRAM = new RAM(0x2000);
        ExtRAM = rom.getRAMBank(0);
        WRAM1 = new RAM(0x1000);
        WRAM2 = new RAM(0x1000);
        // OAM = new RAM(0xa0);
        // IO = new RAM(0x80);
        HRAM = new RAM(0x200);
    }

    @Override
    public byte readByte(int address) {
        ReadableMemory memory = getMemoryIndex(address);
        return memory.readByte(fixedAddress);
    }

    @Override
    public void writeByte(int address, byte value) {
        if (writableAddress(address)) {
            WritableMemory memory = (WritableMemory)getMemoryIndex(address);
            memory.writeByte(fixedAddress, value);
        } else {
            throw new IndexOutOfBoundsException("Attempting to write to ROM.");
        }
    }

    @Override
    public short readShort(int address) {
        address = Bitwise.intAsShort(address);

        byte lo = readByte(address);
        byte hi = readByte(address + 1);

        return Bitwise.toShort(hi, lo);
    }

    @Override
    public void writeShort(int address, short value) {
        address = Bitwise.intAsShort(address);

        byte lo = Bitwise.getLowByte(value);
        byte hi = Bitwise.getHighByte(value);

        writeByte(address, lo);
        writeByte(address + 1, hi);
    }

    public void mapRomBank(int index) {
        bankX = rom.getBank(index);
    }

    private ReadableMemory getMemoryIndex(int address) {
        address = Bitwise.intAsShort(address);
        fixedAddress = address;
        if (address >= 0 && address < 0x4000) {
            // 0000-3fff = 4000
            // Bank 0 of ROM
            return bank0;
        } else if (address < 0x8000) {
            // 4000-7fff = 4000
            // Bank 1-n of ROM
            return bankX;
        } else if (address < 0xA000) {
            // 8000-9fff = 2000
            // VRAM 
            fixedAddress -= 0x8000;
            return VRAM;
        } else if (address < 0xC000) {
            // a000-bfff = 2000
            // Optional switchable bank from Cartridge
            fixedAddress -= 0xa000;
            return ExtRAM;
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

    // TODO MBCs allow writing?
    private boolean writableAddress(int address) {
        address = Bitwise.intAsShort(address);
        if (address < 0x8000) {
            return false;
        }
        return true;
    }
}
