package iskake.jgbe.core.gb;

import iskake.jgbe.core.gb.input.IJoypad;
import iskake.jgbe.core.Bitwise;
import iskake.jgbe.core.gb.timing.Timers;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import static iskake.jgbe.core.gb.HardwareRegisters.HardwareRegister.*;

/**
 * Hardware registers, controls several parts of the Game Boy
 * hardware.
 */
public class HardwareRegisters {
    // TODO: not all registers are implemented
    public enum HardwareRegister {
        // Note: writable bit masks are for DMG (and MGB, SGB), not CGB.
        /** Joypad */
        P1   (0xff00, 0b0011_0000),

        /* Serial data transfer */
        SB   (0xff01, 0b1111_1111), //?
        SC   (0xff02, 0b1000_0001), //? (Bit 1 is CGB only)

        /** Divider register */
        DIV  (0xff04, 0b1111_1111),
        /** Timer counter */
        TIMA (0xff05, 0b1111_1111),
        /** Timer Modulo */
        TMA  (0xff06, 0b1111_1111),
        /** Timer control */
        TAC  (0xff07, 0b0000_0111),

        /** Interrupt flag */
        IF   (0xff0f, 0b0001_1111),

        /* Sound control */
        NR10 (0xff10, 0b0111_1111), //? Requires audio
        NR11 (0xff11, 0b1111_1111), //? Requires audio
        NR12 (0xff12, 0b1111_1111), //? Requires audio
        NR13 (0xff13, 0b1111_1111), //? Requires audio
        NR14 (0xff14, 0b1111_1111), //? Requires audio
        NR21 (0xff16, 0b1111_1111), //? Requires audio
        NR22 (0xff17, 0b1111_1111), //? Requires audio
        NR23 (0xff18, 0b1111_1111), //? Requires audio
        NR24 (0xff19, 0b1111_1111), //? Requires audio
        NR30 (0xff1a, 0b1000_0000), //? Requires audio
        NR31 (0xff1b, 0b1111_1111), //? Requires audio
        NR32 (0xff1c, 0b0110_0000), //? Requires audio
        NR33 (0xff1d, 0b1111_1111), //? Requires audio
        NR34 (0xff1e, 0b1111_1111), //? Requires audio
        NR41 (0xff20, 0b0011_1111), //? Requires audio
        NR42 (0xff21, 0b1111_1111), //? Requires audio
        NR43 (0xff22, 0b1111_1111), //? Requires audio
        NR44 (0xff23, 0b1100_0000), //? Requires audio
        NR50 (0xff24, 0b1111_1111), //? Requires audio
        NR51 (0xff25, 0b1111_1111), //? Requires audio
        NR52 (0xff26, 0b1000_1111), //? Requires audio

        /** LCD Control */
        LCDC (0xff40, 0b1111_1111),
        /** LCD STATus */
        STAT (0xff41, 0b0111_1111),
        /** Scroll Y */
        SCY  (0xff42, 0b1111_1111),
        /** Scroll X */
        SCX  (0xff43, 0b1111_1111),
        /** LCD Y Coordinate */
        LY   (0xff44, 0b0000_0000),
        /** LY Compare */
        LYC  (0xff45, 0b1111_1111),

        /** DMA Transfer */
        DMA  (0xff46, 0b1111_1111),

        /** BG Palette data */
        BGP  (0xff47, 0b1111_1111),
        /** OBJ Palette 0 */
        OBP0 (0xff48, 0b1111_1111),
        /** OBJ Palette 1 */
        OBP1 (0xff49, 0b1111_1111),

        /** Window Y position */
        WY   (0xff4a, 0b1111_1111),
        /** Window X position */
        WX   (0xff4b, 0b1111_1111),

        KEY1 (0xff4d, 0b0000_0000), //? CGB only
        VBK  (0xff4f, 0b0000_0000), //? CGB only

        HDMA1(0xff51, 0b0000_0000), //? CGB only
        HDMA2(0xff52, 0b0000_0000), //? CGB only
        HDMA3(0xff53, 0b0000_0000), //? CGB only
        HDMA4(0xff54, 0b0000_0000), //? CGB only
        HDMA5(0xff55, 0b0000_0000), //? CGB only

        RP   (0xff56, 0b0000_0000), //? CGB only

        BCPS (0xff68, 0b0000_0000), //? CGB only
        BCPD (0xff69, 0b0000_0000), //? CGB only
        OCPS (0xff6a, 0b0000_0000), //? CGB only
        OCPD (0xff6b, 0b0000_0000), //? CGB only

        SVBK (0xff70, 0b0000_0000), //? CGB only

        /** Interrupt enable */
        IE   (0xffff, 0b1111_1111);

        public final int val;
        public final int writableBits;

        HardwareRegister(int val, int writableBits) {
            this.val = val;
            this. writableBits = writableBits;
        }
    }

    /** The hardware registers as a map. Each address is mapped to its corresponding register. */
    public static final Map<Integer, HardwareRegister> map = Arrays.stream(HardwareRegister.values())
            .collect(Collectors.toMap(r -> r.val, r -> r));

    private final byte[] registerValues = new byte[map.size()];
    private final DMAController dmaControl;
    private final Timers timers;
    private final IJoypad joypad;

    public HardwareRegisters(DMAController dmaControl, Timers timers, IJoypad joypad) {
        this.dmaControl = dmaControl;
        this.timers = timers;
        this.joypad = joypad;
    }

    /**
     * Initialize hardware registers. (Based on DMG reset)
     */
    public void init() {
        write        (P1,    0xCF);
        writeInternal(SB,    0x00);
        writeInternal(SC,    0x7E);
        timers.writeDIVInternal(0xABCC);
        writeInternal(TIMA,  0x00);
        writeInternal(TMA,   0x00);
        writeInternal(TAC,   0xF8);
        write        (IF,    0xE1);
        writeInternal(NR10,  0x80);
        writeInternal(NR11,  0xBF);
        writeInternal(NR12,  0xF3);
        writeInternal(NR13,  0xFF);
        writeInternal(NR14,  0xBF);
        writeInternal(NR21,  0x3F);
        writeInternal(NR22,  0x00);
        writeInternal(NR23,  0xFF);
        writeInternal(NR24,  0xBF);
        writeInternal(NR30,  0x7F);
        writeInternal(NR31,  0xFF);
        writeInternal(NR32,  0x9F);
        writeInternal(NR33,  0xFF);
        writeInternal(NR34,  0xBF);
        writeInternal(NR41,  0xFF);
        writeInternal(NR42,  0x00);
        writeInternal(NR43,  0x00);
        writeInternal(NR44,  0xBF);
        writeInternal(NR50,  0x77);
        writeInternal(NR51,  0xF3);
        writeInternal(NR52,  0xF1);
        writeInternal(LCDC,  0x91);
        writeInternal(STAT,  0x85);
        writeInternal(SCY,   0x00);
        writeInternal(SCX,   0x00);
        writeInternal(LY,    0x00);
        writeInternal(LYC,   0x00);
        writeInternal(DMA,   0xFF);
        writeInternal(BGP,   0xFC);
        writeInternal(OBP0,  0xFF); // '??'
        writeInternal(OBP1,  0xFF); // '??'
        writeInternal(WY,    0x00);
        writeInternal(WX,    0x00);
        writeInternal(KEY1,  0xFF);
        writeInternal(VBK,   0xFF);
        writeInternal(HDMA1, 0xFF);
        writeInternal(HDMA2, 0xFF);
        writeInternal(HDMA3, 0xFF);
        writeInternal(HDMA4, 0xFF);
        writeInternal(HDMA5, 0xFF);
        writeInternal(RP,    0xFF);
        writeInternal(BCPS,  0xFF);
        writeInternal(BCPD,  0xFF);
        writeInternal(OCPS,  0xFF);
        writeInternal(OCPD,  0xFF);
        writeInternal(SVBK,  0xFF);
        write        (IE,    0x00);
    }

    /**
     * Read the value stored in the specified hardware register.
     * 
     * @param hwreg The hardware register to read a value from.
     * @return The value stored in the hardware regsiter. If the register is invalid
     *         ({@code hwreg} is {@code null}), then {@code 0xff} is returned instead.
     */
    public byte read(HardwareRegister hwreg) {
        if (hwreg == null) {
            return (byte) 0xff;
        }

        int value = handleSpecialReads(hwreg);
        if ((byte)value == value) {
            return (byte)value;
        } else if (hwreg.writableBits != 0xff && hwreg != LY) {
            return (byte)(Byte.toUnsignedInt(registerValues[hwreg.ordinal()]) | (~(hwreg.writableBits) & 0xff));
        } else {
            return registerValues[hwreg.ordinal()];
        }
    }

    /**
     * Read the value stored in the specified hardware register.
     * 
     * @param hwreg The hardware register to read a value from.
     * @return The value stored in the hardware regsiter as an integer. If the
     *         register is invalid ({@code hwreg} is {@code null}), then
     *         {@code 0xff} is returned instead.
     */
    public int readAsInt(HardwareRegister hwreg) {
        return Byte.toUnsignedInt(read(hwreg));
    }

    /**
     * Handle 'special reads' on registers.
     *
     * @param hwreg The register to potentially handle a special write to.
     * @return The correct byte value, or an integer larger than 255 (no special reads).
     */
    private int handleSpecialReads(HardwareRegister hwreg) {
        return switch (hwreg) {
            case DIV -> timers.readDIV();
            case TIMA -> timers.readTIMA();
            case TMA -> timers.readTMA();
            case TAC -> timers.readTAC();
            case P1 -> {
                if (joypad == null)
                    yield 0x100;

                yield joypad.read();
            }
            default -> 0x100;
        };
    }

    /**
     * Write the specified value to the specified hardware register.
     * 
     * @param hwreg The hardware register to write a value to.
     * @param value The value to write.
     * @return {@code true} if the write was successful, {@code false} otherwise.
     */
    public boolean write(HardwareRegister hwreg, byte value) {
        if (hwreg == null) {
            return false;
        }
        if (handleSpecialWrites(hwreg, value)) {
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
    public boolean write(HardwareRegister hwreg, int value) {
        return write(hwreg, Bitwise.toByte(value));
    }

    /**
     * Write the specified value to the specified hardware register.
     * 
     * @param hwreg The hardware register to write a value to.
     * @param value The value to write.
     * @return {@code true} if the write was successful, {@code false} otherwise.
     */
    public boolean writeInternal(HardwareRegister hwreg, byte value) {
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
    private boolean writeInternal(HardwareRegister hwreg, int value) {
        return writeInternal(hwreg, Bitwise.toByte(value));
    }

    /**
     * Handle 'special writes' on registers.
     * 
     * @param hwreg The register to potentially handle a special write to.
     * @return {@code true} if the specified register needed special writes,
     *         {@code false} otherwise.
     */
    private boolean handleSpecialWrites(HardwareRegister hwreg, byte value) {
        switch (hwreg) {
            case DIV -> timers.writeDIV();
            case TIMA -> timers.writeTIMA();
            case TMA -> timers.writeTMA(value);
            case TAC -> timers.writeTAC(value);
            case DMA -> {
                writeInternal(hwreg, value);
                dmaControl.startDMATransfer(value);
            }
            case P1 -> {
                // TODO: create a "no joypad" class?
                if (joypad == null) {
                    break;
                }

                joypad.write(value);
            }
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
    public boolean inc(HardwareRegister hwreg) {
        if (hwreg == null) {
            return false;
        }
        return writeInternal(hwreg, read(hwreg) + 1);
    }

    /**
     * Set the bit in the specified register.
     *
     * @param hwreg The register to set.
     * @param bit   The bit to set.
     */
    public void setBit(HardwareRegister hwreg, int bit) {
        if (hwreg == null) {
            return;
        }
        write(hwreg, Bitwise.setBit(read(hwreg), bit));
    }

    /**
     * Reset the bit in the specified register.
     *
     * @param hwreg The register to reset.
     * @param bit   The bit to reset.
     */
    public void resetBit(HardwareRegister hwreg, int bit) {
        if (hwreg == null) {
            return;
        }
        write(hwreg, Bitwise.clearBit(read(hwreg), bit));
    }


    /**
     * Set the bit in the specified register.
     *
     * @param hwreg     The register to set/reset.
     * @param bit       The bit to set
     * @param condition Condition to set bit to.
     */
    public void setBitConditional(HardwareRegister hwreg, int bit, boolean condition) {
        if (condition) {
            setBit(hwreg, bit);
        } else {
            resetBit(hwreg, bit);
        }
    }

    /**
     * Checks if a DMA transfer is currently active.
     *
     * @return {@code true} if a DMA transfer is active, {@code false} otherwise.
     */
    public boolean isDMATransfer() {
        return dmaControl.isDMAActive();
    }
}
