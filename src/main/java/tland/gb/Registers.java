package tland.gb;

import tland.Bitwise;

public class Registers {
    public enum RegisterIndex {
        // 8-bit byte registers
        A(0), F(1),
        B(2), C(3),
        D(4), E(5),
        H(6), L(7),
        // 16-bit short registers
        SP(8),
        PC(9),
        AF(0xA),
        BC(0xB),
        DE(0xC),
        HL(0xD);

        private final int val;

        RegisterIndex(int val) {
            this.val = val;
        }
    };

    /**
     * The registers' values, with each register having an index
     * of the {@code val} specified in {@code RegisterIndex}.
     */
    private int[] registerValues = new int[8];

    /**
     * Read the the byte at the given 8-bit register specified
     * by the given register.
     * 
     * @param reg Register to be read from
     * @return The byte value of the given register
     */
    public int readRegisterByte(RegisterIndex reg) {
        return registerValues[reg.val];
    }

    /**
     * Writes the given byte {@code value} to the 8-bit register {@code reg}.
     * 
     * @param value Value to be written to the given register
     * @param reg Register to be written to.
     */
    public void writeRegisterByte(RegisterIndex reg, int value) {
        value = Bitwise.toByte(value);
        registerValues[reg.val] = value;
    }

    /**
     * Read the the short at the given 16-bit register {@code reg}
     * 
     * @param reg Register to be read from
     * @return The short value of the given register
     */
    public int readRegisterShort(RegisterIndex reg) {
        // Index gets rounded down, so two values with the same
        // two least significant bits, will become the same:
        // 0b011 -> 0b001
        // 0b010 -> 0b001
        int offset = (reg.val >> 1) << 1;
        int hi = registerValues[offset];
        int lo = registerValues[offset + 1];
        return Bitwise.toShort(hi, lo);
    }

    /**
     * Writes the given {@code value} to the 16-bit register {@code reg}
     * 
     * @param value Value to be written to the given register
     * @param reg Register to be written to.
     */
    public void writeRegisterShort(RegisterIndex reg, int value) {
        value = Bitwise.toShort(value);

        // Index gets rounded down: 0b011 -> 0b001 == 0b010 -> 0b001
        int offset = (reg.val >> 1) << 1;
        int hi = Bitwise.getHighByte(value);
        int lo = Bitwise.getLowByte(value);

        registerValues[offset] = hi;
        registerValues[offset + 1] = lo;
    }

    /* public void printRegisters() {
        System.out.printf("AF: %d\n", readRegisterByte(RegisterIndex.AF));
        System.out.printf("BC: %d\n", readRegisterByte(RegisterIndex.BC));
        System.out.printf("DE: %d\n", readRegisterByte(RegisterIndex.DE));
        System.out.printf("HL: %d\n", readRegisterByte(RegisterIndex.HL));
    } */
}
