package tland;

/**
 * Class containing methods for bitwise/number operations.
 */
public class Bitwise {
    /**
     * Get the High byte from a short.
     * 
     * @param value The short to get the high byte from.
     * @return The high byte of the specified short.
     */
    public static byte getHighByte(short value) {
        return (byte) (value >> 8);
    }

    /**
     * Get the Low byte from a short.
     * 
     * @param value The short to get the low byte from.
     * @return The low byte of the specified short.
     */
    public static byte getLowByte(short value) {
        return (byte) (value & 0xff);
    }

    /**
     * Create a new short from two bytes.
     * 
     * @param hi High byte of short.
     * @param lo Low byte of short.
     * @return A new short constructed from the two parameters.
     */
    public static short toShort(byte hi, byte lo) {
        return (short) ((hi << 8) | Byte.toUnsignedInt(lo));
    }

    /**
     * Create a new short from an integer.
     * 
     * @param value Integer to convert.
     * @return The converted short value.
     */
    public static short toShort(int value) {
        return (short) value;
    }

    /**
     * Convert an integer into a byte.
     * 
     * @param value Value to convert.
     * @return The converted byte value.
     */
    public static byte toByte(int value) {
        return getLowByte(toShort(value));
    }

    /**
     * Convert a short into a byte.
     * 
     * @param value Value to convert.
     * @return The converted byte value.
     */
    public static byte toByte(short value) {
        return getLowByte(value);
    }

    /**
     * Make given int value into a byte, without converting it.
     * 
     * @param value The value to get as short.
     * @return Value as short.
     */
    public static int intAsByte(int value) {
        return value & 0xff;
    }

    /**
     * Make given int value into a short, without converting it.
     * 
     * @param value The value to get as short.
     * @return Value as short.
     */
    public static int intAsShort(int value) {
        return value & 0xffff;
    }

    /**
     * Set a desired bit in {@code value}. {@code bitNum} corresponds to the bit to
     * set from 0 to 7, with 0 being the least significant bit, and 7 the most
     * significant bit.
     * 
     * @param value  The value to set a bit of.
     * @param bitNum Bit to set, from 0 to 7.
     * @return {@code value}, with bit {@code bitNum} set.
     */
    public static byte setBit(byte value, int bitNum) {
        byte bit = (byte) (0b1 << bitNum);
        return (byte) (value | bit);
    }

    /**
     * Clear a desired bit in {@code value}. {@code bitNum} corresponds to the bit
     * to clear from 0 to 7, with 0 being the least significant bit, and 7 the most
     * significant bit.
     * 
     * @param value  The value to clear a bit of.
     * @param bitNum Bit to clear, from 0 to 7.
     * @return {@code value}, with bit {@code bitNum} cleared.
     */
    public static byte clearBit(byte value, int bitNum) {
        byte bit = (byte) (0b1 << bitNum);
        return (byte) (value & ~bit);
    }

    /**
     * Flip a desired bit in {@code value}. {@code bitNum} corresponds to the bit to
     * flip from 0 to 7, with 0 being the least significant bit, and 7 the most
     * significant bit.
     * 
     * @param value  The value to flip a bit of.
     * @param bitNum Bit to flip, from 0 to 7.
     * @return {@code value}, with bit {@code bitNum} flipped.
     */
    public static byte flipBit(byte value, int bitNum) {
        byte bit = (byte) (0b1 << bitNum);
        return (byte) (value ^ bit);
    }

    /**
     * Check if the desired bit in {@code value} is set to 1.
     * 
     * @param value  The value to check.
     * @param bitNum Bit to check, from 0 to 7.
     * @return {@code true} if bit is set, {@code false} otherwise.
     */
    public static boolean isBitSet(byte value, int bitNum) {
        byte bit = (byte) (0b1 << bitNum);
        return (value & bit) != 0;
    }

    /**
     * Check if the desired bit in {@code value} is set to 1.
     * 
     * @param value  The value to check.
     * @param bitNum Bit to check, from 0 to 31.
     * @return {@code true} if bit is set, {@code false} otherwise.
     */
    public static boolean isBitSet(int value, int bitNum) {
        int bit = (0b1 << bitNum);
        return (value & bit) != 0;
    }

    /**
     * Swap the 4 higher and 4 lower bits in the specified byte.
     * 
     * @param value The byte to swap the bits of.
     * @return The byte with the bits swapped.
     */
    public static byte swapBits(byte value) {
        int hi = Byte.toUnsignedInt(value) >> 4;
        int lo = Byte.toUnsignedInt(value) & 0b00001111;
        return (byte) ((lo << 4) | (hi));
    }

    /**
     * Convert byte value to hex string. Same as writing
     * {@code Integer.toHexString(Byte.toUnsignedInt(value))}
     * 
     * @param value The value to get as hex string.
     * @return Value in hex as a string.
     */
    public static String toHexString(byte value) {
        int val = Byte.toUnsignedInt(value);
        return Integer.toHexString(val);
    }

    /**
     * Convert short value to hex string. Same as writing
     * {@code Integer.toHexString(Short.toUnsignedInt(value))}
     * 
     * @param value The value to get as hex string.
     * @return Value in hex as a string.
     */
    public static String toHexString(short value) {
        int val = Short.toUnsignedInt(value);
        return Integer.toHexString(val);
    }

    /**
     * Convert byte value to binary string. Same as writing
     * {@code Integer.toBinaryString(Byte.toUnsignedInt(value))}
     * 
     * @param value The value to get as hex string.
     * @return Value in hex as a string.
     */
    public static String toBinaryString(byte value) {
        int val = Byte.toUnsignedInt(value);
        return Integer.toBinaryString(val);
    }

    /**
     * Convert short value to binary string. Same as writing
     * {@code Integer.toBinaryString(Short.toUnsignedInt(value))}
     * 
     * @param value The value to get as hex string.
     * @return Value in hex as a string.
     */
    public static String toBinaryString(short value) {
        int val = Short.toUnsignedInt(value);
        return Integer.toBinaryString(val);
    }

    /**
     * Check if two byte arrays are equal.
     * 
     * @param a The first byte array.
     * @param b The second byte array.
     * @return {@code true} if the array elemetns match, {@code false} otherwise.
     */
    public static boolean byteArrayEquals(byte[] a, byte[] b) {
        if (a.length != b.length) {
            return false;
        }
        for (int i = 0; i < b.length; i++) {
            if (a[i] != b[i]) {
                return false;
            }
        }
        return true;
    }

    /**
     * Decode an integer from a string, optionally starting with $ or %.
     * 
     * @param str The string to decode the integer from.
     * @return The decoded integer.
     */
    public static int decodeInt(String str) {
        if (str.charAt(0) == '$') {
            str = str.replace("$", "0x");
        } else if (str.charAt(0) == '%') {
            str = str.replace("%", "0b");
        }
        return Integer.decode(str);
    }
}
