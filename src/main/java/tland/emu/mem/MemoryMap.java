package tland.emu.mem;

import tland.Bitwise;

/**
 * Memory map of the emulator.
 * <p>
 * The memory map contains the current addressable memory, with a range of
 * $0000-$ffff ($10000 bytes)
 */
public class MemoryMap implements WritableMemory<Byte>, ReadableMemory<Byte> {
    private ROM rom;
    private RAM virtualMemory;
    private int fixedAddress;

    public MemoryMap(ROM rom) {
        restart(rom);
    }

    /**
     * Restart the memory map, depending on the specified ROM.
     * 
     * @param rom The ROM file to create a memory map from.
     */
    public void restart(ROM rom) {
        if (rom == null) {
            virtualMemory = new RAM(0x10000);
            init();
        } else {
            this.rom = rom;
            virtualMemory = new RAM(0x10000 - rom.data().length);
            init();
        }
    }

    /**
     * Initialize the memory each region.
     */
    public void init() {
        virtualMemory.clear();
    }

    @Override
    public Byte readAddress(int address) {
        Memory<Byte> memory = getMemoryIndex(address);
        if (memory instanceof ReadableMemory<Byte> m) {
            return m.readAddress(fixedAddress);
        } else {
            return 0;
        }
    }

    @Override
    public void writeAddress(int address, Byte value) {
        Memory<Byte> memory = getMemoryIndex(address);
        if (memory instanceof WritableMemory<Byte> m) {
            m.writeAddress(fixedAddress, value);
        }
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
    private Memory<Byte> getMemoryIndex(int address) {
        address = Bitwise.intAsShort(address);
        fixedAddress = address;
        if (rom == null) {
            return virtualMemory;
        } else {
            if (address >= 0 && address < 0x8000) {
                // 0000-7fff
                // ROM
                return rom;
            } else {
                // 8000-ffff
                // RAM
                fixedAddress -= 0x8000;
                return virtualMemory;
            }
        }
    }
}
