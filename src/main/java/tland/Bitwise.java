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
    public static int getHighByte(int value) {
        return value >> 8;
    }

    /**
     * Get the Low byte from a short.
     * 
     * @param value
     * @return the low byte of the specified short.
     */
    public static int getLowByte(int value) {
        return value & 0xff;
    }

    /**
     * Create a new short from two bytes
     * 
     * @param hi High byte of short.
     * @param lo Low byte of short.
     * @return A new short constructed from the two parameters.
     */
    public static int toShort(int hi, int lo) {
        int value = (hi << 8) | lo;
        return value;
    }

    /**
     * Convert an integer into a (unsigned) byte (0x00 to 0xff).
     * 
     * @param value Value to convert.
     * @return The converted integer value.
     */
    public static int toByte(int value) {
        return getLowByte(value);
    }

    /**
     * Convert an integer into a (unsigned) short (0x0000 to 0xffff).
     * 
     * @param value Value to convert.
     * @return The converted integer value.
     */
    public static int toShort(int value) {
        value = value & 0xffff;
        return value;
    }
}
