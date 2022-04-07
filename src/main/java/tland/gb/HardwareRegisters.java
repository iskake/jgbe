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
        P1(0xff00), // TODO (requires input)

        /* Serial data transfer */
        SB(0xff01), // ?
        SC(0xff02), // ?

        /** Divider register */
        DIV(0xff04), // TODO (requires timers)
        /** Timer counter */
        TIMA(0xff05), // TODO (requires timers)
        /** Timer Modulo */
        TMA(0xff06), // TODO (requires timers)
        /** Timer control */
        TAC(0xff07), // TODO (requires timers)

        /** Interrupt flag */
        IF(0xff0f), // TODO (requires interrupts)

        /* Sound control */
        NR10(0xff10), // ? Requires audio
        NR11(0xff11), // ? Requires audio
        NR12(0xff12), // ? Requires audio
        NR13(0xff13), // ? Requires audio
        NR14(0xff14), // ? Requires audio
        NR21(0xff16), // ? Requires audio
        NR22(0xff17), // ? Requires audio
        NR23(0xff18), // ? Requires audio
        NR24(0xff19), // ? Requires audio
        NR30(0xff1a), // ? Requires audio
        NR31(0xff1b), // ? Requires audio
        NR32(0xff1c), // ? Requires audio
        NR33(0xff1d), // ? Requires audio
        NR34(0xff1e), // ? Requires audio
        NR41(0xff20), // ? Requires audio
        NR42(0xff21), // ? Requires audio
        NR43(0xff22), // ? Requires audio
        NR44(0xff23), // ? Requires audio
        NR50(0xff24), // ? Requires audio
        NR51(0xff25), // ? Requires audio
        NR52(0xff26), // ? Requires audio

        /** LCD Control */
        LCDC(0xff40), // TODO (requires rendering)
        /** LCD STATus */
        STAT(0xff41), // TODO (requires rendering)
        /** Scroll Y */
        SCY(0xff42), // TODO (requires interrupts)
        /** Scroll X */
        SCX(0xff43), // TODO (requires interrupts)
        /** LCD Y Coordinate */
        LY(0xff44), // TODO (requires rendering)
        /** LY Compare */
        LYC(0xff45), // TODO (requires rendering)

        /** DMA Transfer */
        DMA(0xff46), // TODO (requires rendering)

        /** BG Palette data */
        BGP(0xff47), // TODO (requires rendering)
        /** OBJ Palette 0 */
        OBP0(0xff48), // TODO (requires rendering)
        /** OBJ Palette 1 */
        OBP1(0xff49), // TODO (requires rendering)

        /** Window Y position */
        WY(0xff4a), // TODO (requires rendering)
        /** Window X position */
        WX(0xff4b), // TODO (requires rendering)

        KEY1(0xff4d), // ? CGB only
        VBK(0xff4f), // ? CGB only

        HDMA1(0xff51), // ? CGB only
        HDMA2(0xff52), // ? CGB only
        HDMA3(0xff53), // ? CGB only
        HDMA4(0xff54), // ? CGB only
        HDMA5(0xff55), // ? CGB only

        RP(0xff56), // ? CGB only

        BCPS(0xff68), // ? CGB only
        BCPD(0xff69), // ? CGB only
        OCPS(0xff6a), // ? CGB only
        OCPD(0xff6b), // ? CGB only

        SVBK(0xff70), // ? CGB only

        /** Interrupt enable */
        IE(0xffff); // TODO (requires interrupts)

        public final int val;

        private HardwareRegisterIndex(int val) {
            this.val = val;
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

    HardwareRegisters() {
        System.out.println(registerValues.length);
    }

    /**
     * Initialize hardware registers. (Based on DMG reset)
     */
    public void init() {
        writeRegister(P1, 0xCF);
        writeRegister(SB, 0x00);
        writeRegister(SC, 0x7E);
        writeRegister(DIV, 0xAB);
        writeRegister(TIMA, 0x00);
        writeRegister(TMA, 0x00);
        writeRegister(TAC, 0xF8);
        writeRegister(IF, 0xE1);
        writeRegister(NR10, 0x80);
        writeRegister(NR11, 0xBF);
        writeRegister(NR12, 0xF3);
        writeRegister(NR13, 0xFF);
        writeRegister(NR14, 0xBF);
        writeRegister(NR21, 0x3F);
        writeRegister(NR22, 0x00);
        writeRegister(NR23, 0xFF);
        writeRegister(NR24, 0xBF);
        writeRegister(NR30, 0x7F);
        writeRegister(NR31, 0xFF);
        writeRegister(NR32, 0x9F);
        writeRegister(NR33, 0xFF);
        writeRegister(NR34, 0xBF);
        writeRegister(NR41, 0xFF);
        writeRegister(NR42, 0x00);
        writeRegister(NR43, 0x00);
        writeRegister(NR44, 0xBF);
        writeRegister(NR50, 0x77);
        writeRegister(NR51, 0xF3);
        writeRegister(NR52, 0xF1);
        writeRegister(LCDC, 0x91);
        writeRegister(STAT, 0x85);
        writeRegister(SCY, 0x00);
        writeRegister(SCX, 0x00);
        writeRegister(LY, 0x00);
        writeRegister(LYC, 0x00);
        writeRegister(DMA, 0xFF);
        writeRegister(BGP, 0xFC);
        writeRegister(OBP0, 0xFF); // '??'
        writeRegister(OBP1, 0xFF); // '??'
        writeRegister(WY, 0x00);
        writeRegister(WX, 0x00);
        writeRegister(KEY1, 0xFF);
        writeRegister(VBK, 0xFF);
        writeRegister(HDMA1, 0xFF);
        writeRegister(HDMA2, 0xFF);
        writeRegister(HDMA3, 0xFF);
        writeRegister(HDMA4, 0xFF);
        writeRegister(HDMA5, 0xFF);
        writeRegister(RP, 0xFF);
        writeRegister(BCPS, 0xFF);
        writeRegister(BCPD, 0xFF);
        writeRegister(OCPS, 0xFF);
        writeRegister(OCPD, 0xFF);
        writeRegister(SVBK, 0xFF);
        writeRegister(IE, 0x00);
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
     * Write the specified value to the specified hardware register.
     * 
     * @param hwreg The hardware register to write a value to.
     * @param value The value to write.
     */
    public boolean writeRegister(HardwareRegisterIndex hwreg, byte value) {
        if (hwreg == null) {
            return false;
        }
        registerValues[hwreg.ordinal()] = value;
        return true;
    }

    /**
     * Write the specified value to the specified hardware register.
     * 
     * @param hwreg The hardware register to write a value to.
     * @param value The value to write.
     */
    public boolean writeRegister(HardwareRegisterIndex hwreg, int value) {
        return writeRegister(hwreg, Bitwise.toByte(value));
    }
}
