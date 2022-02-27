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
}
