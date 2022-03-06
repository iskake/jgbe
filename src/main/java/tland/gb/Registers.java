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

    public enum Flags {
        /**
         * Carry flag, bit 4 of the F register.
         */
        C(4),
        /**
         * Half carry flag, bit 5 of the F register.
         */
        H(5),
        /**
         * Subtraction flag, bit 6 of the F register.
         */
        N(6),
        /**
         * Zero flag, bit 7 of the F register.
         */
        Z(7);

        private final int val;

        Flags(int val) {
            this.val = val;
        }
    }

    /**
     * The registers' values, with each register having an index
     * of the {@code val} specified in {@code RegisterIndex} (8-bit).
     */
    private byte[] registerValues = new byte[8];

    private final GameBoy gb;

    public Registers(GameBoy gb) {
        this.gb = gb;
    }

    /**
     * Check if the specified flag is set in the F register.
     * 
     * @param flag The falg to check.
     * @return {@code true} if the flag is set, {@code false} otherwise.
     */
    public boolean isFlagSet(Flags flag) {
        int i = RegisterIndex.F.val;
        return Bitwise.isBitSet(registerValues[i], flag.val);
    }

    /**
     * Check if the specified flag is set in the F register.
     * 
     * @param flag The falg to check.
     * @return {@code true} if the flag is set, {@code false} otherwise.
     */
    public void setFlag(Flags flag) {
        int i = RegisterIndex.F.val;
        registerValues[i] = Bitwise.setBit(registerValues[i], flag.val);
    }

    /**
     * Check if the specified flag is set in the F register.
     * 
     * @param flag The falg to check.
     * @return {@code true} if the flag is set, {@code false} otherwise.
     */
    public void resetFlag(Flags flag) {
        int i = RegisterIndex.F.val;
        registerValues[i] = Bitwise.clearBit(registerValues[i], flag.val);
    }

    /**
     * Read value at address pointed to by the value in {@code reg}.
     * 
     * @param reg Register to be read from
     * @return Value pointed to by {@code reg}.
     */
    public byte readRegisterPtr(RegisterIndex reg) {
        checkRegisterShort(reg);
        return gb.readMemoryAddress(readRegisterShort(reg));
    }

    /**
     * Write {@code value} to the address pointed to by the value in {@code reg}.
     * 
     * @param reg   Register to be read from
     * @param value Value to write at address pointed to by {@code reg}.
     */
    public void writeRegisterPtr(RegisterIndex reg, byte value) {
        checkRegisterShort(reg);
        gb.writeMemoryAddress(readRegisterShort(reg), value);
    }

    /**
     * Read the the byte at the given register {@ reg} if the specified register is
     * an 8-bit register. If the specified register is a 16-bit register, the byte
     * pointed to by that register is is read instead.
     * 
     * @param reg Register to be read from
     * @return The byte value of the given register
     */
    public byte readRegisterByte(RegisterIndex reg) {
        if (isRegisterShort(reg)) {
            return readRegisterPtr(reg);
        }
        return registerValues[reg.val];
    }

    /**
     * Writes the given byte {@code value} to the register {@code reg} if the
     * specified register is an 8-bit register. If the specified register is a
     * 16-bit register, then the byte pointed to by that register is written to
     * instead.
     * 
     * @param value Value to be written to the given register
     * @param reg   Register to be written to.
     */
    public void writeRegisterByte(RegisterIndex reg, byte value) {
        if (isRegisterShort(reg)) {
            writeRegisterPtr(reg, value);
            return;
        }
        registerValues[reg.val] = value;
    }

    /**
     * Writes the given byte {@code value} to the register {@code reg} if the
     * specified register is an 8-bit register. If the specified register is (HL),
     * then the byte pointed to by (HL) is written to instead.
     * 
     * @param value Value to be written to the given register
     * @param reg   Register to be written to.
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

        int index = getShortRegisterIndex(reg);

        byte hi = registerValues[index];
        byte lo = registerValues[index + 1];

        return Bitwise.toShort(hi, lo);
    }

    /**
     * Writes the given {@code value} to the 16-bit register {@code reg}
     * 
     * @param value Value to be written to the given register
     * @param reg   Register to be written to.
     */
    public void writeRegisterShort(RegisterIndex reg, short value) {
        checkRegisterShort(reg);

        int index = getShortRegisterIndex(reg);

        byte hi = Bitwise.getHighByte(value);
        byte lo = Bitwise.getLowByte(value);

        registerValues[index] = hi;
        registerValues[index + 1] = lo;
    }

    /**
     * Writes the given {@code value} to the 16-bit register {@code reg}
     * 
     * @param value Value to be written to the given register
     * @param reg   Register to be written to.
     */
    public void writeRegisterShort(RegisterIndex reg, int value) {
        writeRegisterShort(reg, Bitwise.toShort(value));
    }

    /**
     * Increment the value stored in the 8-bit register {@code reg}.
     * 
     * @param reg The register to increment the value of.
     */
    public void incRegisterByte(RegisterIndex reg) {
        if (isRegisterShort(reg)) {
            if (reg.val == RegisterIndex.HL.val) {

            } else {
                // There are no `INC` instructions for incrementing the address stored in a
                // short register other than HL (opcode 0x34).
                checkRegisterByte(reg);
            }
        }
        registerValues[reg.val]++;
    }

    /**
     * Increment the value stored in the 16-bit register {@code reg}.
     * 
     * @param reg The register to increment the value of.
     */
    public void incRegisterShort(RegisterIndex reg) {
        checkRegisterShort(reg);

        int msbOffset = getShortRegisterIndex(reg);
        int lsbOffset = msbOffset + 1;

        registerValues[lsbOffset]++;
        if (registerValues[lsbOffset] == 0) {
            registerValues[msbOffset]++;
        }
    }

    /**
     * Get the index of the byte register corresponding to the most significant
     * byte of {@code reg}.
     * <p>
     * Use with both {@code registerValues[index]} and
     * {@code registerValues[index + 1]}.
     * 
     * @param reg The register to get
     * @return The index of the most significant byte of {@code reg} in
     *         {@code registerValues}
     */
    private int getShortRegisterIndex(RegisterIndex reg) {
        // Bit level trickery to get correct index:
        // Get 3 least significant bits of value of RegisterIndex (e.g. BC = 0b1001 ->
        // 0b001) and store as
        // index. Shift index left by 1 (equivalent to (index * 2)) to get byte
        // register corresponding to short register (e.g. (0b001 << 1) = 0b010 = B).
        // Note: to get the next register, take index + 1 (e.g. B(0b10) -> C(0b11))
        int offset = (reg.val & 0b111) << 1;
        return offset;
    }

    /**
     * Checks if the given register is valid for byte/short read/write operations
     * If it is not, throw exception.
     * 
     * @param reg      Register to check.
     * @param shortReg Check for short register.
     * @throws IndexOutOfBoundsException
     */
    private void checkRegister(RegisterIndex reg, boolean shortReg) throws IndexOutOfBoundsException {
        if (shortReg) {
            if (reg.val < RegisterIndex.AF.val || reg.val > RegisterIndex.HL.val) {
                throw new IndexOutOfBoundsException(
                        String.format("Register %s is not a valid short register.", reg.name()));
            }
        } else {
            if (reg.val < RegisterIndex.A.val || reg.val > RegisterIndex.L.val) {
                throw new IndexOutOfBoundsException(
                        String.format("Register %s is not a valid byte register.", reg.name()));
            }
        }
    }

    /**
     * Checks if the given register is valid for byte read/write operations.
     * If it is not, throw exception.
     * 
     * @param reg Register to check.
     * @throws IndexOutOfBoundsException
     */
    private int checkRegisterByte(RegisterIndex reg) {
        checkRegister(reg, false);
        return 0;
    }

    /**
     * Checks if the given register is valid for short read/write operations.
     * If it is not, throw exception.
     * 
     * @param reg Register to check.
     * @throws IndexOutOfBoundsException
     */
    private void checkRegisterShort(RegisterIndex reg) {
        checkRegister(reg, true);
    }

    /**
     * Checks if the given register is a 8-bit register.
     * 
     * @param reg
     * @return {@code true} if the register is either
     *         {@code A},{@code F},{@code B},{@code C},
     *         {@code D},{@code E},{@code H} or {@code L}. {@code false} otherwise.
     */
    public static boolean isRegisterByte(RegisterIndex reg) {
        return reg.val >= RegisterIndex.A.val && reg.val <= RegisterIndex.L.val;
    }

    /**
     * Checks if the given register is a 16-bit register.
     * Note: this does not include {@code SP} and {@code PC}
     * 
     * @param reg
     * @return {@code true} if the register is either
     *         {@code AF},{@code BC},{@code DE} or {@code HL}.
     *         {@code false} otherwise.
     */
    public static boolean isRegisterShort(RegisterIndex reg) {
        return reg.val >= RegisterIndex.AF.val && reg.val <= RegisterIndex.HL.val;
    }

    public void printRegisters() {
        System.out.printf("AF: %04x\n", readRegisterShort(RegisterIndex.AF));
        System.out.printf("BC: %04x\n", readRegisterShort(RegisterIndex.BC));
        System.out.printf("DE: %04x\n", readRegisterShort(RegisterIndex.DE));
        System.out.printf("HL: %04x\n", readRegisterShort(RegisterIndex.HL));
        System.out.printf("SP: %04x\n", gb.getSP());
        System.out.printf("PC: %04x\n", gb.getPC());
    }
}
