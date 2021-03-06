package iskake.jgbe.core.gb.ppu;

import iskake.jgbe.core.gb.HardwareRegisters;
import iskake.jgbe.core.gb.HardwareRegisters.HardwareRegister;
import iskake.jgbe.core.Bitwise;

/**
 * PPU and LCD Control
 */
public class PPUController {
    private final HardwareRegisters hwreg;

    private boolean lcdEnabled;

    public PPUController(HardwareRegisters hwreg) {
        this.hwreg = hwreg;
    }

    /**
     * Helper method to get the value stored in LCDC.
     * 
     * @return The value in LCDC.
     */
    private int getLCDC() {
        return hwreg.readAsInt(HardwareRegister.LCDC);
    }

    /**
     * Check if BG and Window is enabled.
     * <p>
     * (LCDC Bit 0)
     * 
     * @return {@code true} if LCDC bit 0 is set, {@code false} otherwise.
     */
    public boolean isBGAndWindowEnabled() {
        return Bitwise.isBitSet(getLCDC(), 0);
    }

    /**
     * Check if OBJs (sprites) are enabled.
     * <p>
     * (LCDC Bit 1)
     * 
     * @return {@code true} if LCDC bit 1 is set, {@code false} otherwise.
     */
    public boolean areOBJsEnabled() {
        return Bitwise.isBitSet(getLCDC(), 1);
    }

    /**
     * Get the size of OBJs (sprites).
     * <p>
     * (LCDC Bit 2)
     * 
     * @return The OBJ size specified in LCDC, the size will be either 16 or 8.
     */
    public int getOBJSize() {
        return Bitwise.isBitSet(getLCDC(), 2) ? 16 : 8;
    }

    /**
     * Get the VRAM start address for the BG tilemap (or, just 'tilemap').
     * <p>
     * (LCDC Bit 3)
     * 
     * @return The BG area start address specified in LCDC, the address will be
     *         either {@code $9c00} or {$9800}.
     */
    public int getBGTilemapArea() {
        return Bitwise.isBitSet(getLCDC(), 3) ? 0x9c00 : 0x9800;
    }

    /**
     * Get the VRAM start address for the BG and Window tilemaps.
     * <p>
     * (LCDC Bit 4)
     * 
     * @return The index start address specified in LCDC, the address will be
     *         either {@code $8000} or {@code $8800}.
     */
    public int getBGAndWindowTileArea() {
        return Bitwise.isBitSet(getLCDC(), 4) ? 0x8000 : 0x8800;
    }

    /**
     * Get the 'tile block' index for tiles 0-127 in VRAM for the BG and Window
     * tilemaps.
     * <p>
     * (LCDC Bit 4)
     * 
     * @return The index of the 'tile block' specified in LCDC, the index will be
     *         either {0} or {@code 2}.
     */
    public int getBGAndWindowTileBlock() {
        return Bitwise.isBitSet(getLCDC(), 4) ? 0 : 2;
    }

    /**
     * Check if the Window layer is enabled.
     * Note that LCDC bit 0 ({@link PPUController#isBGAndWindowEnabled()}) overrides
     * this check if BG+Window is enabled.
     * <p>
     * (LCDC Bit 5)
     * 
     * @return {@code true} if LCDC bit 5 is set, {@code false} otherwise.
     */
    public boolean isWindowEnabled() {
        return Bitwise.isBitSet(getLCDC(), 5);
    }

    /**
     * Get the VRAM start address for the BG tilemap (or, just 'tilemap').
     * <p>
     * (LCDC Bit 3)
     * 
     * @return The BG area start address specified in LCDC, the address will be
     *         either {@code $9c00} or {$9800}.
     */
    public int getWindowTilemapArea() {
        return Bitwise.isBitSet(getLCDC(), 6) ? 0x9c00 : 0x9800;
    }

    /**
     * Check if the LCD (and PPU) is enabled.
     * <p>
     * (LCDC Bit 7)
     * 
     * @return {@code true} if LCDC bit 7 is set, {@code false} otherwise.
     */
    public boolean isLCDEnabled() {
        return Bitwise.isBitSet(getLCDC(), 7);
    }

    /**
     * Check if the VRAM is accessible.
     * 
     * @return {@code true} if the VRAM is accessible, {@code false} otherwise.
     */
    public boolean isVRAMAccessible() {
        int i = hwreg.readAsInt(HardwareRegister.STAT) & 0b11;
        return (i != 3) || !isLCDEnabled();
    }

    /**
     * Check if the OAM is accessible.
     * 
     * @return {@code true} if the OAM is accessible, {@code false} otherwise.
     */
    public boolean isOAMAccessible() {
        int i = hwreg.readAsInt(HardwareRegister.STAT) & 0b11;
        return (i <= 1) || !isLCDEnabled();
    }

}
