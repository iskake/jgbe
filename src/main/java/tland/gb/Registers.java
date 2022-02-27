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
        AF(0x8),
        BC(0x9),
        DE(0xA),
        HL(0xB),

        SP(0xC),
        PC(0xD);

        private final int val;

        RegisterIndex(int val) {
            this.val = val;
        }
    };

    /**
     * The registers' values, with each register having an index
     * of the {@code val} specified in {@code RegisterIndex} (8-bit).
     */
    private byte[] registerValues = new byte[8];

    /**
     * Read the the byte at the given 8-bit register specified
     * by the given register.
     * 
     * @param reg Register to be read from
     * @return The byte value of the given register
     */
    public byte readRegisterByte(RegisterIndex reg) {
        checkRegisterByte(reg);
        return registerValues[reg.val];
    }

    /**
     * Writes the given byte {@code value} to the 8-bit register {@code reg}.
     * 
     * @param value Value to be written to the given register
     * @param reg Register to be written to.
     */
    public void writeRegisterByte(RegisterIndex reg, byte value) {
        checkRegisterByte(reg);
        value = Bitwise.toByte(value);
        registerValues[reg.val] = value;
    }

    /**
     * Writes the given byte {@code value} to the 8-bit register {@code reg}.
     * 
     * @param value Value to be written to the given register
     * @param reg Register to be written to.
     */
    public void writeRegisterByte(RegisterIndex reg, int value) {
        writeRegisterByte(reg, Bitwise.toByte(value));
    }

    /**
     * Read the the short at the given 16-bit register {@code reg}
     * 
     * @param reg Register to be read from
     * @return The short value of the given register
     */
    public short readRegisterShort(RegisterIndex reg) {
        checkRegisterShort(reg);

        // Bit level trickery to avoid switch statement:
        // Get 3 lsb of value of RegisterIndex (e.g. BC = 0b1001 -> 0b001) and store as offset.
        // Shift offset left by 1 (equivalent to (offset * 2)) to get byte register corresponding
        // to short register (e.g. 0b001 << 1 -> 0b010 -> B) and take + 1 for next byte
        // (e.g. B(0b10) -> C(0b11))
        int offset = (reg.val & 0b111) << 1;

        byte hi = registerValues[offset];
        byte lo = registerValues[offset + 1];

        return Bitwise.toShort(hi, lo);
    }

    /**
     * Writes the given {@code value} to the 16-bit register {@code reg}
     * 
     * @param value Value to be written to the given register
     * @param reg Register to be written to.
     */
    public void writeRegisterShort(RegisterIndex reg, short value) {
        checkRegisterShort(reg);

        // Bit level trickery to avoid switch statement:
        // Get 3 lsb of value of RegisterIndex (e.g. BC = 0b1001 -> 0b001) and store as offset.
        // Shift offset left by 1 (equivalent to (offset * 2)) to get byte register corresponding
        // to short register (e.g. 0b001 << 1 -> 0b010 -> B) and take + 1 for next byte
        // (e.g. B(0b10) -> C(0b11))
        int offset = (reg.val & 0b111) << 1;

        byte hi = Bitwise.getHighByte(value);
        byte lo = Bitwise.getLowByte(value);

        registerValues[offset] = hi;
        registerValues[offset + 1] = lo;
    }

    /**
     * Writes the given {@code value} to the 16-bit register {@code reg}
     * 
     * @param value Value to be written to the given register
     * @param reg Register to be written to.
     */
    public void writeRegisterShort(RegisterIndex reg, int value) {
        writeRegisterShort(reg, Bitwise.toShort(value));
    }

    /**
     * Checks if the given register is valid for byte/short read/write operations
     * If it is not, throw exception.
     * @param reg Register to check.
     * @param shortReg Check for short register.
     * @throws IndexOutOfBoundsException
     */
    private void checkRegister(RegisterIndex reg, boolean shortReg) throws IndexOutOfBoundsException {
        if (shortReg) {
            if (reg.val < RegisterIndex.AF.val || reg.val > RegisterIndex.HL.val) {
                throw new IndexOutOfBoundsException(String.format("Register %s is not a valid short register.", reg.name()));
            }
        } else {
            if (reg.val < RegisterIndex.A.val || reg.val > RegisterIndex.L.val) {
                throw new IndexOutOfBoundsException(String.format("Register %s is not a valid byte register.", reg.name()));
            }
        }
    }

    /**
     * Checks if the given register is valid for byte read/write operations.
     * If it is not, throw exception.
     * @param reg Register to check.
     * @throws IndexOutOfBoundsException
     */
    private void checkRegisterByte(RegisterIndex reg) {
        checkRegister(reg, false);
    }

    /**
     * Checks if the given register is valid for short read/write operations.
     * If it is not, throw exception.
     * @param reg Register to check.
     * @throws IndexOutOfBoundsException
     */
    private void checkRegisterShort(RegisterIndex reg) {
        checkRegister(reg, true);
    }
    
    /**
     * Checks if the given register is a 8-bit register.
     * @param reg
     * @return {@code true} if the register is either {@code A},{@code F},{@code B},{@code C},
     * {@code D},{@code E},{@code H} or {@code L}. {@code false} otherwise.
     */
    public boolean isRegisterByte(RegisterIndex reg) {
        return reg.val >= RegisterIndex.A.val && reg.val <= RegisterIndex.L.val;
    }

    /**
     * Checks if the given register is a 16-bit register.
     * Note: this does not include {@code SP} and {@code PC}
     * @param reg
     * @return {@code true} if the register is either {@code AF},{@code BC},{@code DE} or {@code HL}.
     * {@code false} otherwise.
     */
    public boolean isRegisterShort(RegisterIndex reg) {
        return reg.val >= RegisterIndex.AF.val && reg.val <= RegisterIndex.HL.val;
    }

    public void printRegisters() {
        System.out.printf("AF: %d\n", readRegisterShort(RegisterIndex.AF));
        System.out.printf("BC: %d\n", readRegisterShort(RegisterIndex.BC));
        System.out.printf("DE: %d\n", readRegisterShort(RegisterIndex.DE));
        System.out.printf("HL: %d\n", readRegisterShort(RegisterIndex.HL));
    }
}
