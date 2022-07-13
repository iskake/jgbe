package iskake.jgbe.core.gb;

import iskake.jgbe.core.Bitwise;

/**
 * The CPU registers in the modified SM83 processor.
 */
public class Registers {
    /**
     * CPU Registers.
     */
    public enum Register {
        // 8-bit registers. Made up of a half of each 16-bit register.

        /** Accumulator, used as a general register and for arithmetic operations. */
        A(0),
        /** Flags register, holds flags from various operations. */
        F(1),
        B(2), C(3),
        D(4), E(5),
        H(6), L(7),

        // 16-bit registers. Made up of pairs of the 8-bit registers.
        AF(0x8),
        BC(0x9),
        DE(0xA),
        /** Also used in special cases of 8-bit instructions */
        HL(0xB),

        // 'Special' 16-bit registers.

        /** Stack Pointer. Holds the current memory address of the stack. */
        SP(0xC);

        private final int val;

        Register(int val) {
            this.val = val;
        }

        /**
         * The index of the values based on the 3 lsb of the opcodes.
         * <p>
         * For example, an opcode with the lower 3 bits being $4 corresponds to
         * {@code RegisterIndex.H}, such as $44 ({@code ld b, h}) and $ac
         * ({@code xor h})
         */
        public static final Register[] tableIndex = { B, C, D, E, H, L, HL, A };
    };

    /**
     * Flags for the Flags register ({@code Registers.F}).
     * 
     * <p>
     * Flags are stored as the 4 most significant bits of the F register.
     * 
     * @see Register
     */
    public enum Flags {
        /** Carry flag, bit 4 of the F register. */
        C(4),
        /** Half carry flag, bit 5 of the F register. */
        H(5),
        /** Subtraction flag, bit 6 of the F register. */
        N(6),
        /** Zero flag, bit 7 of the F register. */
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
    private final byte[] registerValues = new byte[8];

    private final IGameBoy gb;

    public Registers(IGameBoy gb) {
        this.gb = gb;
    }

    /**
     * Check if the specified bit is set in the register {@code reg}.
     * 
     * @param reg    The register to to check the bit of.
     * @param bitNum The bit to check.
     * @return {@code true} if the bit is set, {@code false} otherwise.
     */
    public boolean isBitSet(Register reg, int bitNum) {
        // Check for [hl] bitwise instructions (HL is the only short register to be
        // used in pointer bitwise opcodes.)
        if (!reg.equals(Register.HL)) {
            checkByteRegister(reg);
        }
        return Bitwise.isBitSet(readByte(reg), bitNum);
    }

    /**
     * Set the specified bit in the register {@code reg} (set bit to 1).
     * 
     * @param bitNum The bit to set.
     */
    public void setBit(Register reg, int bitNum) {
        // Check for [hl] bitwise instructions (HL is the only short register to be
        // used in pointer bitwise opcodes.)
        if (!reg.equals(Register.HL)) {
            checkByteRegister(reg);
        }
        writeByte(reg, Bitwise.setBit(readByte(reg), bitNum));
    }

    /**
     * Reset (clear) the specified bit in the register {@code reg} (set bit to 0).
     * 
     * @param bitNum The bit to reset.
     */
    public void resetBit(Register reg, int bitNum) {

        // Check for [hl] bitwise instructions (HL is the only short register to be
        // used in pointer bitwise opcodes.)
        if (!reg.equals(Register.HL)) {
            checkByteRegister(reg);
        }
        writeByte(reg, Bitwise.clearBit(readByte(reg), bitNum));
    }

    /**
     * Check if the specified flag is set in the F register.
     * 
     * @param flag The flag to check.
     * @return {@code true} if the flag is set, {@code false} otherwise.
     */
    public boolean isFlagSet(Flags flag) {
        return isBitSet(Register.F, flag.val);
    }

    /**
     * Set the specified flag (set the corresponding bit) in the F register.
     * 
     * @param flag The flag to set.
     */
    public void setFlag(Flags flag) {
        setBit(Register.F, flag.val);
    }

    /**
     * Reset the specified flag (clear the corresponding bit) in the F register.
     * 
     * @param flag The flag to reset.
     */
    public void resetFlag(Flags flag) {
        resetBit(Register.F, flag.val);
    }

    /**
     * Set the specified flag in the F register if the condition returns
     * {@code true}, otherwise reset the flag.
     * 
     * @param flag      The flag to set or reset.
     * @param condition The condition to set the flag based on.
     */
    public void setFlagConditional(Flags flag, boolean condition) {
        if (condition) {
            setFlag(flag);
        } else {
            resetFlag(flag);
        }
    }

    /**
     * Complement the specified flag (flip the corresponding bit) in the F register.
     * 
     * @param flag The flag to complement.
     */
    public void complementFlag(Flags flag) {
        int i = Register.F.val;
        registerValues[i] = Bitwise.flipBit(registerValues[i], flag.val);
    }

    /**
     * Helper method for left bit shifting.
     * 
     * @param reg   The register to shift the value of.
     * @param shift If the bit shifting should be a (arithmetic) shift or rotation.
     * @param carry If the bit shifting should use carry.
     * @return The overflow bit for carry.
     */
    private int bitShiftLeft(Register reg, boolean shift, boolean carry) {
        // Check for [hl] shift/rotate instructions (HL is the only short register to be
        // used in pointer shift/rotate opcodes.)
        if (!reg.equals(Register.HL)) {
            checkByteRegister(reg);
        }
        int oldValue = Byte.toUnsignedInt(readByte(reg));
        int c;
        if (carry) {
            c = isFlagSet(Flags.C) ? 1 : 0;
        } else if (shift) {
            c = 0;
        } else {
            c = oldValue >> 7;
        }
        byte newValue = Bitwise.toByte((oldValue << 1) | c);
        writeByte(reg, newValue);
        return oldValue >> 7;
    }

    /**
     * Helper method for right bit shifting.
     * 
     * @param reg     The register to shift the value of.
     * @param shift   If the bit shifting should be a shift or rotation.
     * @param logical If the bit shifting should be a logical shift or arithmetic.
     * @param carry   If the bit shifting should use carry.
     * @return The overflow bit for carry.
     */
    private int bitShiftRight(Register reg, boolean shift, boolean logical, boolean carry) {
        // Check for [hl] shift/rotate instructions (HL is the only short register to be
        // used in pointer shift/rotate opcodes.)
        if (!reg.equals(Register.HL)) {
            checkByteRegister(reg);
        }
        int oldValue = Byte.toUnsignedInt(readByte(reg));
        int c;
        if (carry) {
            c = isFlagSet(Flags.C) ? 1 : 0;
        } else if (shift) {
            c = logical ? 0 : oldValue >> 7;
        } else {
            c = oldValue & 1;
        }
        byte newValue = Bitwise.toByte((oldValue >> 1) | (c << 7));
        writeByte(reg, newValue);
        return oldValue & 1;
    }

    /**
     * Rotate the bits in the specified register (or the byte pointed to by the
     * register, in the case of [hl]) left by 1.
     * 
     * @param reg   The register to rotate left.
     * @param carry Wether to rotate thought carry or not.
     * @return The overflow bit for carry.
     */
    public int rotateLeft(Register reg, boolean carry) {
        return bitShiftLeft(reg, false, carry);
    }

    /**
     * Rotate the bits in the specified register (or the byte pointed to by the
     * register, in the case of [hl]) right by 1.
     * 
     * @param reg   The register to rotate right.
     * @param carry Wether to rotate thought carry or not.
     * @return The overflow bit for carry.
     */
    public int rotateRight(Register reg, boolean carry) {
        return bitShiftRight(reg, false, false, carry);
    }

    /**
     * Shift (arithmetic) the bits in the specified register (or the byte pointed to
     * by the register, in the case of [hl]) left by 1.
     * 
     * @param reg The register to shift left.
     * @return The overflow bit for carry.
     */
    public int shiftLeft(Register reg) {
        return bitShiftLeft(reg, true, false);
    }

    /**
     * Shift the bits in the specified register (or the byte pointed to by the
     * register, in the case of [hl]) right by 1.
     * 
     * @param reg     The register to shift right.
     * @param logical Wether to use logical shifting or not.
     * @return The overflow bit for carry.
     */
    public int shiftRight(Register reg, boolean logical) {
        return bitShiftRight(reg, true, logical, false);
    }

    /**
     * Swap the higher and lower 4 bits in the (or the byte pointed to by the
     * register, in the case of [hl]) specified register.
     * 
     * @param reg The register to swap the bits in.
     * @return 0 (Note: this is used for consistency with the other bitwise
     *         shift/rotate instructions)
     */
    public int swap(Register reg) {
        writeByte(reg, Bitwise.swapBits(readByte(reg)));
        return 0;
    }

    /**
     * Read value at address pointed to by the value in {@code reg}.
     * 
     * @param reg Register to be read from
     * @return Value pointed to by {@code reg}.
     */
    private byte readPointer(Register reg) {
        checkShortRegister(reg);
        return gb.readAddress(readShort(reg));
    }

    /**
     * Write {@code value} to the address pointed to by the value in {@code reg}.
     * 
     * @param reg   Register to be read from
     * @param value Value to write at address pointed to by {@code reg}.
     */
    private void writePointer(Register reg, byte value) {
        checkShortRegister(reg);
        gb.writeAddress(readShort(reg), value);
    }

    /**
     * Read the the byte at the given register {@code reg} if the specified register
     * is an 8-bit register. If the specified register is a 16-bit register, the
     * byte pointed to by that register is is read instead.
     * 
     * @param reg Register to be read from
     * @return The byte value of the given register
     */
    public byte readByte(Register reg) {
        if (isShortRegister(reg)) {
            return readPointer(reg);
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
    public void writeByte(Register reg, byte value) {
        if (isShortRegister(reg)) {
            writePointer(reg, value);
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
    public void writeByte(Register reg, int value) {
        writeByte(reg, Bitwise.toByte(value));
    }

    /**
     * Read the the short at the given 16-bit register {@code reg}
     * 
     * @param reg Register to be read from
     * @return The short value of the given register
     */
    public short readShort(Register reg) {
        checkShortRegister(reg);

        if (reg.equals(Register.SP)) {
            return gb.sp().get();
        }

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
    public void writeShort(Register reg, short value) {
        checkShortRegister(reg);

        if (reg.equals(Register.SP)) {
            gb.sp().set(value);
            return;
        }

        int index = getShortRegisterIndex(reg);

        byte hi = Bitwise.getHighByte(value);
        byte lo = Bitwise.getLowByte(value);

        registerValues[index] = hi;
        if ((index + 1) == 1) {
            // Used in `pop af` only
            registerValues[index + 1] = Bitwise.toByte(lo & 0b11110000);
        } else {
            registerValues[index + 1] = lo;
        }
    }

    /**
     * Writes the given {@code value} to the 16-bit register {@code reg}
     * 
     * @param value Value to be written to the given register
     * @param reg   Register to be written to.
     */
    public void writeShort(Register reg, int value) {
        writeShort(reg, Bitwise.toShort(value));
    }

    /**
     * Clear all register values. This will set all registers (including the F
     * register) to {@code $00}.
     */
    public void clearAll() {
        writeShort(Register.AF, 0x0000);
        writeShort(Register.BC, 0x0000);
        writeShort(Register.DE, 0x0000);
        writeShort(Register.HL, 0x0000);
    }

    /**
     * Increment the value stored in the 8-bit register {@code reg}.
     * 
     * @param reg The register to increment the value of.
     */
    public void incByte(Register reg) {
        if (isShortRegister(reg)) {
            if (reg.equals(Register.HL)) {
                byte val = readByte(Register.HL);
                writeByte(reg, ++val);
                return;
            } else {
                // There are no `INC` instructions for incrementing the address stored in a
                // short register other than HL (opcode 0x34).
                checkByteRegister(reg);
            }
        }
        registerValues[reg.val]++;
    }

    /**
     * Decrement the value stored in the 8-bit register {@code reg}.
     * 
     * @param reg The register to decrement the value of.
     */
    public void decByte(Register reg) {
        if (isShortRegister(reg)) {
            if (reg.equals(Register.HL)) {
                byte val = readByte(Register.HL);
                writeByte(reg, --val);
                return;
            } else {
                // There are no `DEC` instructions for decrementing the address stored in a
                // short register other than HL (opcode 0x35).
                checkByteRegister(reg);
            }
        }
        registerValues[reg.val]--;
    }

    /**
     * Increment the value stored in the 16-bit register {@code reg}.
     * 
     * @param reg The register to increment the value of.
     */
    public void incShort(Register reg) {
        checkShortRegister(reg);

        if (reg.equals(Register.SP)) {
            gb.sp().inc();
            return;
        }

        int msbOffset = getShortRegisterIndex(reg);
        int lsbOffset = msbOffset + 1;

        registerValues[lsbOffset]++;
        if (registerValues[lsbOffset] == 0) {
            registerValues[msbOffset]++;
        }
    }

    /**
     * Decrement the value stored in the 16-bit register {@code reg}.
     * 
     * @param reg The register to decrement the value of.
     */
    public void decShort(Register reg) {
        checkShortRegister(reg);

        if (reg.equals(Register.SP)) {
            gb.sp().dec();
            return;
        }

        int msbOffset = getShortRegisterIndex(reg);
        int lsbOffset = msbOffset + 1;

        registerValues[lsbOffset]--;
        if (registerValues[lsbOffset] == (byte) 0xff) {
            registerValues[msbOffset]--;
        }
    }

    /**
     * Get the index of the byte register corresponding to the most significant
     * byte of {@code reg}.
     * <p>
     * Use with both {@code registerValues[index]} and
     * {@code registerValues[index + 1]}.
     * 
     * @param reg The register to get the correct index from.
     * @return The index of the most significant byte of {@code reg} in
     *         {@code registerValues}
     */
    private int getShortRegisterIndex(Register reg) {
        // Get 3 least significant bits of value of RegisterIndex
        // (e.g. BC = 0b1001 -> 0b001) and store as index.
        // Shift index left by 1 (equivalent to (index * 2)) to get byte
        // register corresponding to short register (e.g. (0b001 << 1) = 0b010 = B).
        // Note: to get the next register, take index + 1 (e.g. B(0b10) -> C(0b11))
        return (reg.val & 0b111) << 1;
    }

    /**
     * Checks if the given register is valid for byte/short read/write operations
     * If it is not, throw exception.
     * 
     * @param reg      Register to check.
     * @param shortReg Check for short register.
     * @throws IndexOutOfBoundsException if the register is not the correct format
     *                                   (8/16-bit).
     */
    private void checkRegister(Register reg, boolean shortReg) {
        if (shortReg) {
            if (reg.val < Register.AF.val || reg.val > Register.SP.val) {
                throw new IndexOutOfBoundsException(
                        String.format("Register %s is not a valid short register.", reg.name()));
            }
        } else {
            if (reg.val < Register.A.val || reg.val > Register.L.val) {
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
     * @throws IndexOutOfBoundsException if the register is not an 8-bit register.
     */
    private int checkByteRegister(Register reg) {
        checkRegister(reg, false);
        return 0;
    }

    /**
     * Checks if the given register is valid for short read/write operations.
     * If it is not, throw exception.
     * 
     * @param reg Register to check.
     * @throws IndexOutOfBoundsException if the register is not an 16-bit register.
     */
    private void checkShortRegister(Register reg) {
        checkRegister(reg, true);
    }

    /**
     * Checks if the given register is a 8-bit register.
     * 
     * @param reg The register to check.
     * @return {@code true} if the register is either
     *         {@code A},{@code F},{@code B},{@code C},
     *         {@code D},{@code E},{@code H} or {@code L}. {@code false} otherwise.
     */
    public static boolean isByteRegister(Register reg) {
        return reg.val >= Register.A.val && reg.val <= Register.L.val;
    }

    /**
     * Checks if the given register is a 16-bit register.
     * Note: this does not include {@code SP} and {@code PC}
     * 
     * @param reg The register to check.
     * @return {@code true} if the register is either
     *         {@code AF},{@code BC},{@code DE} or {@code HL}.
     *         {@code false} otherwise.
     */
    public static boolean isShortRegister(Register reg) {
        return reg.val >= Register.AF.val && reg.val <= Register.SP.val;
    }

    public void printValues() {
        System.out.printf("AF: %04x  ", readShort(Register.AF));
        char Z = isFlagSet(Flags.Z) ? 'Z' : '-';
        char N = isFlagSet(Flags.N) ? 'N' : '-';
        char H = isFlagSet(Flags.H) ? 'H' : '-';
        char C = isFlagSet(Flags.C) ? 'C' : '-';
        System.out.printf("Flags: %c%c%c%c\n", Z, N, H, C);
        System.out.printf("BC: %04x\n", readShort(Register.BC));
        System.out.printf("DE: %04x\n", readShort(Register.DE));
        System.out.printf("HL: %04x\n", readShort(Register.HL));
        System.out.printf("SP: %04x\n", gb.sp().get());
        System.out.printf("PC: %04x\n", gb.pc().get());
    }
}
