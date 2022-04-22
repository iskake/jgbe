package tland.gb.mem;

import tland.Bitwise;

/**
 * Memory map of the Game boy.
 * <p>
 * The memory map contains the current addressable memory, with a range of
 * $0000-$ffff ($10000 bytes)
 */
public class MemoryMap implements WritableMemory, ReadableMemory {
    private CartridgeROM rom;
    private RAM virtualMemory;
    private int fixedAddress;

    public MemoryMap(CartridgeROM rom) {
        this.rom = rom;

        if (rom == null) {
            virtualMemory = new RAM(0x10000);
        } else {
            virtualMemory = new RAM(0x8000);
        }

        init();
    }

    /**
     * Restart the memory map, depending on the specified ROM.
     * 
     * @param rom The ROM file to create a memory map from.
     */
    public void restart(CartridgeROM rom) {
        if (rom == null) {
            init();
        } else {
            this.rom = rom;
            virtualMemory = new RAM(0x8000);
        }
    }

    /**
     * Initialize the memory each region.
     */
    public void init() {
        virtualMemory.clear();
    }

    @Override
    public byte readByte(int address) {
        ReadableMemory memory = (ReadableMemory) getMemoryIndex(address);
        return memory.readByte(fixedAddress);
    }

    @Override
    public void writeByte(int address, byte value) {
        WritableMemory memory = (WritableMemory) getMemoryIndex(address);
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
        if (rom == null) {
            return virtualMemory;
        } else {
            if (address >= 0 && address < 0x8000) {
                // 0000-7fff
                // ROM bank0-n,
                return rom;
            } else {
                // 8000-ffff
                fixedAddress -= 0x8000;
                return virtualMemory;
            }
        }
    }
}
