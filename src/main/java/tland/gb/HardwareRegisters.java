package tland.gb;

/**
 * Hardware registers, controls several different parts of the Game Boy hardware.
 */
public class HardwareRegisters {
    public enum HardwareRegisterIndex {
        P1   (0xff00),
        SB   (0xff01),
        SC   (0xff02),
        //    0xff03
        DIV  (0xff04),
        TIMA (0xff05),
        TMA  (0xff06),
        TAC  (0xff07),
        //    0xff08
        //    0xff09
        //    0xff0a
        //    0xff0b
        //    0xff0c
        //    0xff0d
        //    0xff0e
        IF   (0xff0f),
        NR10 (0xff10),
        NR11 (0xff11),
        NR12 (0xff12),
        NR13 (0xff13),
        NR14 (0xff14),
        //    0xff15
        NR21 (0xff16),
        NR22 (0xff17),
        NR23 (0xff18),
        NR24 (0xff19),
        NR30 (0xff1a),
        NR31 (0xff1b),
        NR32 (0xff1c),
        NR33 (0xff1d),
        NR34 (0xff1e),
        //    0xff1f
        NR41 (0xff20),
        NR42 (0xff21),
        NR43 (0xff22),
        NR44 (0xff23),
        NR50 (0xff24),
        NR51 (0xff25),
        NR52 (0xff26),
        //    0xff1f
        //    0xff2f
        //    0xff3f
        LCDC (0xff40),
        STAT (0xff41),
        SCY  (0xff42),
        SCX  (0xff43),
        LY   (0xff44),
        LYC  (0xff45),
        DMA  (0xff46),
        BGP  (0xff47),
        OBP0 (0xff48),
        OBP1 (0xff49),
        WY   (0xff4a),
        WX   (0xff4b),
        //    0xff4c
        KEY1 (0xff4d),
        VBK  (0xff4f),
        HDMA1(0xff51),
        HDMA2(0xff52),
        HDMA3(0xff53),
        HDMA4(0xff54),
        HDMA5(0xff55),
        RP   (0xff56),
        //    0xff57-0xff67
        BCPS (0xff68),
        BCPD (0xff69),
        OCPS (0xff6a),
        OCPD (0xff6b),
        //    0xff6c-0xff6f
        SVBK (0xff70),
        //    0xff71-0xff7f
        //    0xff80-0xfffe (HRAM)
        IE   (0xffff);

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

    HardwareRegisters(){
        System.out.println(registerValues.length);
    }

    public byte readRegister(HardwareRegisterIndex hwreg) {
        if (hwreg == null) {
            return (byte) 0xff;
        }
        return registerValues[hwreg.ordinal()];
    }

    public boolean writeRegister(HardwareRegisterIndex hwreg, byte value) {
        if (hwreg == null) {
            return false;
        }
        registerValues[hwreg.ordinal()] = value;
        System.out.println("Wrote " + registerValues[hwreg.ordinal()] + " to regsiter " + hwreg.name());
        return true;
    }
}
