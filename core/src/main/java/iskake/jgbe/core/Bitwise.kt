package iskake.jgbe.core

/**
 * Class containing methods for bitwise/number operations.
 */
object Bitwise {
    /**
     * Get the High byte from a short.
     *
     * @param value The short to get the high byte from.
     * @return The high byte of the specified short.
     */
    @JvmStatic
    fun getHighByte(value: Short): Byte {
        return (value.toInt() shr 8).toByte()
    }

    /**
     * Get the Low byte from a short.
     *
     * @param value The short to get the low byte from.
     * @return The low byte of the specified short.
     */
    @JvmStatic
    fun getLowByte(value: Short): Byte {
        return (value.toInt() and 0xff).toByte()
    }

    /**
     * Create a new short from two bytes.
     *
     * @param hi High byte of short.
     * @param lo Low byte of short.
     * @return A new short constructed from the two parameters.
     */
    @JvmStatic
    fun toShort(hi: Byte, lo: Byte): Short {
        return (hi.toInt() shl 8 or java.lang.Byte.toUnsignedInt(lo)).toShort()
    }

    /**
     * Create a new short from an integer.
     *
     * @param value Integer to convert.
     * @return The converted short value.
     */
    @JvmStatic
    fun toShort(value: Int): Short {
        return value.toShort()
    }

    /**
     * Convert an integer into a byte.
     *
     * @param value Value to convert.
     * @return The converted byte value.
     */
    @JvmStatic
    fun toByte(value: Int): Byte {
        return getLowByte(toShort(value))
    }

    /**
     * Convert a short into a byte.
     *
     * @param value Value to convert.
     * @return The converted byte value.
     */
    @JvmStatic
    fun toByte(value: Short): Byte {
        return getLowByte(value)
    }

    /**
     * Make given int value into a byte, without converting it.
     *
     * @param value The value to get as short.
     * @return Value as short.
     */
    @JvmStatic
    fun intAsByte(value: Int): Int {
        return value and 0xff
    }

    /**
     * Make given int value into a short, without converting it.
     *
     * @param value The value to get as short.
     * @return Value as short.
     */
    @JvmStatic
    fun intAsShort(value: Int): Int {
        return value and 0xffff
    }

    /**
     * Set a desired bit in `value`. `bitNum` corresponds to the bit to
     * set from 0 to 7, with 0 being the least significant bit, and 7 the most
     * significant bit.
     *
     * @param value  The value to set a bit of.
     * @param bitNum Bit to set, from 0 to 7.
     * @return `value`, with bit `bitNum` set.
     */
    @JvmStatic
    fun setBit(value: Byte, bitNum: Int): Byte {
        val bit = (1 shl bitNum).toByte()
        return (value.toInt() or bit.toInt()).toByte()
    }

    /**
     * Clear a desired bit in `value`. `bitNum` corresponds to the bit
     * to clear from 0 to 7, with 0 being the least significant bit, and 7 the most
     * significant bit.
     *
     * @param value  The value to clear a bit of.
     * @param bitNum Bit to clear, from 0 to 7.
     * @return `value`, with bit `bitNum` cleared.
     */
    @JvmStatic
    fun clearBit(value: Byte, bitNum: Int): Byte {
        val bit: Int = 1 shl bitNum
        return (value.toUByte() and bit.inv().toUByte()).toByte()
    }

    /**
     * Flip a desired bit in `value`. `bitNum` corresponds to the bit to
     * flip from 0 to 7, with 0 being the least significant bit, and 7 the most
     * significant bit.
     *
     * @param value  The value to flip a bit of.
     * @param bitNum Bit to flip, from 0 to 7.
     * @return `value`, with bit `bitNum` flipped.
     */
    @JvmStatic
    fun flipBit(value: Byte, bitNum: Int): Byte {
        val bit = (1 shl bitNum).toByte()
        return (value.toInt() xor bit.toInt()).toByte()
    }

    /**
     * Check if the desired bit in `value` is set to 1.
     *
     * @param value  The value to check.
     * @param bitNum Bit to check, from 0 to 7.
     * @return `true` if bit is set, `false` otherwise.
     */
    @JvmStatic
    fun isBitSet(value: Byte, bitNum: Int): Boolean {
        val bit = (1 shl bitNum).toByte()
        return value.toInt() and bit.toInt() != 0
    }

    /**
     * Check if the desired bit in `value` is set to 1.
     *
     * @param value  The value to check.
     * @param bitNum Bit to check, from 0 to 31.
     * @return `true` if bit is set, `false` otherwise.
     */
    @JvmStatic
    fun isBitSet(value: Int, bitNum: Int): Boolean {
        val bit = 1 shl bitNum
        return value and bit != 0
    }

    /**
     * Swap the 4 higher and 4 lower bits in the specified byte.
     *
     * @param value The byte to swap the bits of.
     * @return The byte with the bits swapped.
     */
    @JvmStatic
    fun swapBits(value: Byte): Byte {
        val hi = java.lang.Byte.toUnsignedInt(value) shr 4
        val lo = java.lang.Byte.toUnsignedInt(value) and 15
        return (lo shl 4 or hi).toByte()
    }

    /**
     * Decode an integer from a string, optionally starting with $ or %.
     *
     * @param intStr The string to decode the integer from.
     * @return The decoded integer.
     */
    @JvmStatic
    fun decodeInt(intStr: String): Int {
        var str = intStr
        if (str[0] == '$') {
            str = str.replace("$", "0x")
        } else if (str[0] == '%') {
            str = str.replace("%", "0b")
        }
        return if (str.startsWith("0b")) str.toInt(2) else Integer.decode(str)
    }

}