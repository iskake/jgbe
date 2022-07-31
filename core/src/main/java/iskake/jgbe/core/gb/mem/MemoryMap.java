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
public class MemoryMap implements WritableMemory, ReadableMemory {
    private static final Logger log = LoggerFactory.getLogger(MemoryMap.class);
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


    public MemoryMap(HardwareRegisters hwreg, VRAM vram, OAM oam) {
        this.hwreg = hwreg;
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
        this.rom = rom;
        VRAM.clear();
        WRAM1.randomize();
        WRAM2.randomize();
        OAM.randomize();
        HRAM.randomize();
        wavePatternRAM.clear();
    }

    @Override
    public byte read(int address) {
        // TODO: DMA
        if (hwreg.isDMATransfer() && Bitwise.intAsShort(address) < (short)0xff00) {
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
    public void write(int address, byte value) {
        // TODO: DMA
        if (hwreg.isDMATransfer() && Bitwise.intAsShort(address) < (short)0xff00) {
            throw new RuntimeException("DMA transfer");
        }
        WritableMemory memory = (WritableMemory) getMemoryIndex(address);
        if (memory == null) {
            return;
        }
        memory.write(fixedAddress, value);
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
}
