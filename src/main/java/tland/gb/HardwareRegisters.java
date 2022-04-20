package tland.gb;

import tland.Bitwise;

import static tland.gb.HardwareRegisters.HardwareRegisterIndex.*;

/**
 * Hardware registers, controls several different parts of the Game Boy
 * hardware.
 */
public class HardwareRegisters {
    public enum HardwareRegisterIndex {
        /** Joypad */
        P1   (0xff00, 0b0011_1111), // TODO (requires input)

        /* Serial data transfer */
        SB   (0xff01, 0b1111_1111), //?
        SC   (0xff02, 0b1111_1111), //?

        /** Divider register */
        DIV  (0xff04, 0b1111_1111),
        /** Timer counter */
        TIMA (0xff05, 0b1111_1111),
        /** Timer Modulo */
        TMA  (0xff06, 0b1111_1111),
        /** Timer control */
        TAC  (0xff07, 0b0000_0111),

        /** Interrupt flag */
        IF   (0xff0f, 0b1111_1111),

        /* Sound control */
        NR10 (0xff10, 0b1111_1111), //? Requires audio
        NR11 (0xff11, 0b1111_1111), //? Requires audio
        NR12 (0xff12, 0b1111_1111), //? Requires audio
        NR13 (0xff13, 0b1111_1111), //? Requires audio
        NR14 (0xff14, 0b1111_1111), //? Requires audio
        NR21 (0xff16, 0b1111_1111), //? Requires audio
        NR22 (0xff17, 0b1111_1111), //? Requires audio
        NR23 (0xff18, 0b1111_1111), //? Requires audio
        NR24 (0xff19, 0b1111_1111), //? Requires audio
        NR30 (0xff1a, 0b1111_1111), //? Requires audio
        NR31 (0xff1b, 0b1111_1111), //? Requires audio
        NR32 (0xff1c, 0b1111_1111), //? Requires audio
        NR33 (0xff1d, 0b1111_1111), //? Requires audio
        NR34 (0xff1e, 0b1111_1111), //? Requires audio
        NR41 (0xff20, 0b1111_1111), //? Requires audio
        NR42 (0xff21, 0b1111_1111), //? Requires audio
        NR43 (0xff22, 0b1111_1111), //? Requires audio
        NR44 (0xff23, 0b1111_1111), //? Requires audio
        NR50 (0xff24, 0b1111_1111), //? Requires audio
        NR51 (0xff25, 0b1111_1111), //? Requires audio
        NR52 (0xff26, 0b1111_1111), //? Requires audio

        /** LCD Control */
        LCDC (0xff40, 0b1111_1111),
        /** LCD STATus */
        STAT (0xff41, 0b1111_1111),
        /** Scroll Y */
        SCY  (0xff42, 0b1111_1111),
        /** Scroll X */
        SCX  (0xff43, 0b1111_1111),
        /** LCD Y Coordinate */
        LY   (0xff44, 0b0000_0000),
        /** LY Compare */
        LYC  (0xff45, 0b1111_1111),

        /** DMA Transfer */
        DMA  (0xff46, 0b1111_1111), // TODO (requires rendering)

        /** BG Palette data */
        BGP  (0xff47, 0b1111_1111), // TODO (requires rendering)
        /** OBJ Palette 0 */
        OBP0 (0xff48, 0b1111_1111), // TODO (requires rendering)
        /** OBJ Palette 1 */
        OBP1 (0xff49, 0b1111_1111), // TODO (requires rendering)

        /** Window Y position */
        WY   (0xff4a, 0b1111_1111), // TODO (requires rendering)
        /** Window X position */
        WX   (0xff4b, 0b1111_1111), // TODO (requires rendering)

        KEY1 (0xff4d, 0b1111_1111), //? CGB only
        VBK  (0xff4f, 0b1111_1111), //? CGB only

        HDMA1(0xff51, 0b1111_1111), //? CGB only
        HDMA2(0xff52, 0b1111_1111), //? CGB only
        HDMA3(0xff53, 0b1111_1111), //? CGB only
        HDMA4(0xff54, 0b1111_1111), //? CGB only
        HDMA5(0xff55, 0b1111_1111), //? CGB only

        RP   (0xff56, 0b1111_1111), //? CGB only

        BCPS (0xff68, 0b1111_1111), //? CGB only
        BCPD (0xff69, 0b1111_1111), //? CGB only
        OCPS (0xff6a, 0b1111_1111), //? CGB only
        OCPD (0xff6b, 0b1111_1111), //? CGB only

        SVBK (0xff70, 0b1111_1111), //? CGB only

        /** Interrupt enable */
        IE   (0xffff, 0b1111_1111);

        public final int val;
        public final int writableBits;

        private HardwareRegisterIndex(int val, int writableBits) {
            this.val = val;
            this. writableBits = writableBits;
        }

        public static HardwareRegisterIndex getRegisterFromAddress(int address) {
            for (HardwareRegisterIndex hwr : HardwareRegisterIndex.values()) {
                if (hwr.val == address) {
                    return hwr;
                }
            }

            return null;
        }
    }

    private byte[] registerValues = new byte[HardwareRegisterIndex.values().length];

    /**
     * Initialize hardware registers. (Based on DMG reset)
     */
    public void init() {
        writeRegisterInternal(P1,    0xCF);
        writeRegisterInternal(SB,    0x00);
        writeRegisterInternal(SC,    0x7E);
        writeRegisterInternal(DIV,   0xAB);
        writeRegisterInternal(TIMA,  0x00);
        writeRegisterInternal(TMA,   0x00);
        writeRegisterInternal(TAC,   0xF8);
        writeRegisterInternal(IF,    0xE1);
        writeRegisterInternal(NR10,  0x80);
        writeRegisterInternal(NR11,  0xBF);
        writeRegisterInternal(NR12,  0xF3);
        writeRegisterInternal(NR13,  0xFF);
        writeRegisterInternal(NR14,  0xBF);
        writeRegisterInternal(NR21,  0x3F);
        writeRegisterInternal(NR22,  0x00);
        writeRegisterInternal(NR23,  0xFF);
        writeRegisterInternal(NR24,  0xBF);
        writeRegisterInternal(NR30,  0x7F);
        writeRegisterInternal(NR31,  0xFF);
        writeRegisterInternal(NR32,  0x9F);
        writeRegisterInternal(NR33,  0xFF);
        writeRegisterInternal(NR34,  0xBF);
        writeRegisterInternal(NR41,  0xFF);
        writeRegisterInternal(NR42,  0x00);
        writeRegisterInternal(NR43,  0x00);
        writeRegisterInternal(NR44,  0xBF);
        writeRegisterInternal(NR50,  0x77);
        writeRegisterInternal(NR51,  0xF3);
        writeRegisterInternal(NR52,  0xF1);
        writeRegisterInternal(LCDC,  0x91);
        writeRegisterInternal(STAT,  0x85);
        writeRegisterInternal(SCY,   0x00);
        writeRegisterInternal(SCX,   0x00);
        writeRegisterInternal(LY,    0x00);
        writeRegisterInternal(LYC,   0x00);
        writeRegisterInternal(DMA,   0xFF);
        writeRegisterInternal(BGP,   0xFC);
        writeRegisterInternal(OBP0,  0xFF); // '??'
        writeRegisterInternal(OBP1,  0xFF); // '??'
        writeRegisterInternal(WY,    0x00);
        writeRegisterInternal(WX,    0x00);
        writeRegisterInternal(KEY1,  0xFF);
        writeRegisterInternal(VBK,   0xFF);
        writeRegisterInternal(HDMA1, 0xFF);
        writeRegisterInternal(HDMA2, 0xFF);
        writeRegisterInternal(HDMA3, 0xFF);
        writeRegisterInternal(HDMA4, 0xFF);
        writeRegisterInternal(HDMA5, 0xFF);
        writeRegisterInternal(RP,    0xFF);
        writeRegisterInternal(BCPS,  0xFF);
        writeRegisterInternal(BCPD,  0xFF);
        writeRegisterInternal(OCPS,  0xFF);
        writeRegisterInternal(OCPD,  0xFF);
        writeRegisterInternal(SVBK,  0xFF);
        writeRegisterInternal(IE,    0x00);
    }

    /**
     * Read the value stored in the specified hardware register.
     * 
     * @param hwreg The hardware register to read a value from.
     * @return The value stored in the hardware regsiter. If the register is invalid
     *         ({@code hwreg} is {@code null}), then {@code 0xff} is returned instead.
     */
    public byte readRegister(HardwareRegisterIndex hwreg) {
        if (hwreg == null) {
            return (byte) 0xff;
        }
        return registerValues[hwreg.ordinal()];
    }

    /**
     * Read the value stored in the specified hardware register.
     * 
     * @param hwreg The hardware register to read a value from.
     * @return The value stored in the hardware regsiter as an integer. If the
     *         register is invalid ({@code hwreg} is {@code null}), then
     *         {@code 0xff} is returned instead.
     */
    public int readRegisterInt(HardwareRegisterIndex hwreg) {
        return Byte.toUnsignedInt(readRegister(hwreg));
    }

    /**
     * Write the specified value to the specified hardware register.
     * 
     * @param hwreg The hardware register to write a value to.
     * @param value The value to write.
     * @return {@code true} if the write was successful, {@code false} otherwise.
     */
    public boolean writeRegister(HardwareRegisterIndex hwreg, byte value) {
        if (hwreg == null) {
            return false;
        }
        if (handleSpecialWrites(hwreg)) {
            return true;
        }
        registerValues[hwreg.ordinal()] = (byte)(Byte.toUnsignedInt(value) & hwreg.writableBits);
        return true;
    }

    /**
     * Write the specified value to the specified hardware register.
     * 
     * @param hwreg The hardware register to write a value to.
     * @param value The value to write.
     * @return {@code true} if the write was successful, {@code false} otherwise.
     */
    public boolean writeRegister(HardwareRegisterIndex hwreg, int value) {
        return writeRegister(hwreg, Bitwise.toByte(value));
    }

    /**
     * Write the specified value to the specified hardware register.
     * 
     * @param hwreg The hardware register to write a value to.
     * @param value The value to write.
     * @return {@code true} if the write was successful, {@code false} otherwise.
     */
    private boolean writeRegisterInternal(HardwareRegisterIndex hwreg, byte value) {
        registerValues[hwreg.ordinal()] = value;
        return true;
    }

    /**
     * Write the specified value to the specified hardware register.
     * 
     * @param hwreg The hardware register to write a value to.
     * @param value The value to write.
     * @return {@code true} if the write was successful, {@code false} otherwise.
     */
    private boolean writeRegisterInternal(HardwareRegisterIndex hwreg, int value) {
        return writeRegisterInternal(hwreg, Bitwise.toByte(value));
    }

    /**
     * Handle 'special writes' on registers.
     * 
     * @param hwreg
     * @return {@code true} if the specified register needed special writes,
     *         {@code false} otherwise.
     */
    private boolean handleSpecialWrites(HardwareRegisterIndex hwreg) {
        switch (hwreg) {
            case DIV -> writeRegisterInternal(hwreg, (byte)0x00);
            case DMA -> writeRegisterInternal(hwreg, (byte)0x00);
            default -> { return false; }
        }
        return true;
    }

    /**
     * Increment the value stored in the register (if writable).
     * 
     * @param hwreg The register to increment.
     * @return {@code true} if the write was successful, {@code false} otherwise.
     */
    public boolean incRegister(HardwareRegisterIndex hwreg) {
        if (hwreg == null) {
            return false;
        }
        return writeRegisterInternal(hwreg, readRegister(hwreg) + 1);
    }

    /**
     * Set the bit in the specified register.
     * 
     * @param hwreg The register to set.
     * @param bit   The bit to set.
     * @return {@code true} if the write was successful, {@code false} otherwise.
     */
    public boolean setBit(HardwareRegisterIndex hwreg, int bit) {
        if (hwreg == null) {
            return false;
        }
        return writeRegister(hwreg, Bitwise.setBit(readRegister(hwreg), bit));
    }

    /**
     * Reset the bit in the specified register.
     * 
     * @param hwreg The register to reset.
     * @param bit   The bit to reset.
     * @return {@code true} if the write was successful, {@code false} otherwise.
     */
    public boolean resetBit(HardwareRegisterIndex hwreg, int bit) {
        if (hwreg == null) {
            return false;
        }
        return writeRegister(hwreg, Bitwise.clearBit(readRegister(hwreg), bit));
    }


    /**
     * Set the bit in the specified register.
     * 
     * @param hwreg   The register to set/reset.
     * @param bit     The bit to set
     * @param boolean Condition to set bit to.
     * @return {@code true} if the write was successful, {@code false} otherwise.
     */
    public boolean setBitConditional(HardwareRegisterIndex hwreg, int bit, boolean condition) {
        if (condition) {
            return setBit(hwreg, bit);
        } else {
            return resetBit(hwreg, bit);
        }
    }
}
