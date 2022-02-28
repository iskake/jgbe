package tland;

/**
 * Class containing methods for bitwise operations.
 */
public class Bitwise {
    /**
     * Get the High byte from a short.
     * 
     * @param value
     * @return the high byte of the specified short.
     */
    public static byte getHighByte(short value) {
        return (byte) (value >> 8);
    }

    /**
     * Get the Low byte from a short.
     * 
     * @param value
     * @return the low byte of the specified short.
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
        short value = (short) ((hi << 8) | lo);
        return value;
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
}
