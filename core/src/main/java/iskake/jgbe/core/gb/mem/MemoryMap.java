package iskake.jgbe.core.gb.mem;

import iskake.jgbe.core.gb.HardwareRegisters;
import iskake.jgbe.core.Bitwise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Memory map of the Game boy.
 * <p>
 * The memory map contains the current addressable memory, with a range of
 * $0000-$ffff ($10000 bytes)
 */
public class MemoryMap implements WritableMemoryUnrestricted, ReadableMemoryUnrestricted {
    private static final Logger log = LoggerFactory.getLogger(MemoryMap.class);
    private final ROMBank bootROM;
    private CartridgeROM rom;
    private final HardwareRegisters hwreg;
    private final VRAM VRAM;
    private final RAM WRAM1;
    private final RAM WRAM2;
    private final OAM OAM;
    private final ProhibitedMemory prohibited;
    private final HardwareRegisterMap IO;
    private final RAM wavePatternRAM;
    private final RAM HRAM;

    private int fixedAddress;
    private boolean bootROMEnabled;


    public MemoryMap(HardwareRegisters hwreg, VRAM vram, OAM oam, ROMBank bootROM) {
        this.hwreg = hwreg;
        this.bootROM = bootROM;
        VRAM = vram;
        WRAM1 = new RAM(0x1000);
        WRAM2 = new RAM(0x1000);
        OAM = oam;
        IO = new HardwareRegisterMap(hwreg);
        prohibited = new ProhibitedMemory(hwreg);
        HRAM = new RAM(0x200);
        wavePatternRAM = new RAM(0x10);
    }

    /**
     * Initialize the memory each region.
     */
    public void init(CartridgeROM rom) {
        mapBootROM();
        this.rom = rom;
        VRAM.clear();
        OAM.clear();
        wavePatternRAM.clear();

        WRAM1.randomize();
        WRAM2.randomize();
        HRAM.randomize();
    }

    @Override
    public byte read(int address) {
        if (hwreg.isDMATransfer() && Bitwise.intAsShort(address) < 0xff00) {
            log.warn("Attempting to read memory address during DMA: $%04x".formatted(address));
            return (byte) 0xff;
        }
        ReadableMemory memory = (ReadableMemory) getMemoryIndex(address);
        if (memory == null) {
            return (byte) 0xff;
        }
        return memory.read(fixedAddress);
    }

    @Override
    public byte readUnrestricted(int address) {
        ReadableMemory memory = (ReadableMemory) getMemoryIndex(address);
        if (memory instanceof ReadableMemoryUnrestricted memoryUn) {
            return memoryUn.readUnrestricted(fixedAddress);
        } else if (memory != null) {
            return memory.read(fixedAddress);
        } else {
            return (byte) 0xff;
        }
    }

    @Override
    public void write(int address, byte value) {
        if (hwreg.isDMATransfer() && Bitwise.intAsShort(address) < (short)0xff00) {
            throw new RuntimeException("DMA transfer"); // TODO: should this be handled?
        }
        WritableMemory memory = (WritableMemory) getMemoryIndex(address);
        if (memory == null) {
            return;
        }
        memory.write(fixedAddress, value);
    }

    @Override
    public void writeUnrestricted(int address, byte value) {
        WritableMemory memory = (WritableMemory) getMemoryIndex(address);
        if (memory instanceof WritableMemoryUnrestricted memoryUn) {
            memoryUn.writeUnrestricted(fixedAddress, value);
        } else if (memory != null) {
            memory.write(fixedAddress, value);
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
    private Object getMemoryIndex(int address) {
        address = Bitwise.intAsShort(address);
        fixedAddress = address;
        if (address >= 0 && address < 0x8000) {
            // Boot ROM, enabled at start, then disabled at the end of the boot sequence.
            if (bootROMEnabled && address < 0x100)
                return bootROM;
            // 0000-3fff = 4000
            // ROM bank0-n, (note that MBC will control which bank is read from the address.)
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
            try {
                return rom.getRAMBank();
            } catch (NullPointerException e) {
                return null;
            }
        } else if (address < 0xD000) {
            // c000-cfff = 1000
            fixedAddress -= 0xc000;
            return WRAM1;
        } else if (address < 0xE000) {
            // d000-dfff = 1000
            fixedAddress -= 0xd000;
            return WRAM2;
        } else if (address < 0xF000) {
            // e000-f000 = 1000
            // (Mirror of c000-ddff)
            log.warn("Accessing ECHO RAM.");
            fixedAddress -= 0xe000;
            return WRAM1;
        } else if (address < 0xFE00) {
            // e000-fdff = e00
            // (Mirror of c000-ddff)
            log.warn("Accessing ECHO RAM.");
            fixedAddress -= 0xf000;
            return WRAM2;
        } else if (address < 0xFEA0) {
            // fe00-fe9f = a0
            fixedAddress -= 0xfe00;
            return OAM;
        } else if (address < 0xFF00) {
            // fea0-feff = 60
            // ('Not usable/prohibited' memory)
            return prohibited;
        } else if (((address < 0xFF30) || (address < 0xFF80 && address > 0xFF3F)) || address == 0xFFFF) {
            // ff00-ff2f = 30
            // ff40-ff7f = 40
            return IO;
        } else if (address < 0xFF40) {
            // ff30-ff3f = 10
            fixedAddress -= 0xff30;
            return wavePatternRAM;
        } else {
            // ff80-fffe (HRAM)
            fixedAddress -= 0xff80;
            return HRAM;
        }
    }

    public void mapBootROM() {
        bootROMEnabled = bootROM != null;
    }

    public void unmapBootROM() {
        if (bootROM != null) {
            bootROMEnabled = false;
        }
    }
}
