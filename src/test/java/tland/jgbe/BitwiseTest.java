package tland.jgbe;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import tland.Bitwise;

public class BitwiseTest {
    @Test
    void bitwiseTest() {
        assertEquals((byte) 0xa8, Bitwise.getHighByte((short) 0xa8e0));
        assertEquals((byte) 0xe0, Bitwise.getLowByte((short) 0xa8e0));

        assertEquals((short) 0x1c25, Bitwise.toShort((byte) 0x1c, (byte) 0x25));
        assertEquals((short) 0x1c25, Bitwise.toShort(0x1c25));

        assertEquals((byte) 0xce, Bitwise.toByte(0xa5ce));
        assertEquals((byte) 0xce, Bitwise.toByte((short) 0xa5ce));

        assertEquals(0x56, Bitwise.intAsByte(0x123456));
        assertEquals(0x3456, Bitwise.intAsShort(0x123456));

        assertEquals((byte) 0b01001000, Bitwise.setBit((byte) 0b00001000, 6));
        assertEquals((byte) 0b11011111, Bitwise.clearBit((byte) 0b11111111, 5));
        assertEquals((byte) 0b10110001, Bitwise.flipBit((byte) 0b10110101, 2));
        assertTrue(Bitwise.isBitSet((byte)0b1000_0000, 7));
        assertFalse(Bitwise.isBitSet((byte)0b1111_1110, 0));
        assertEquals((byte)0b1111_0000, Bitwise.swapBits((byte)0b0000_1111));

        assertEquals(1234, Bitwise.decodeInt("1234"));
        assertEquals(0xa5e4, Bitwise.decodeInt("$a5e4"));
        assertEquals(0xa5e4f9, Bitwise.decodeInt("0xa5e4f9"));
        assertEquals(0b1101, Bitwise.decodeInt("%1101"));
        assertEquals(0b110101011011000101001, Bitwise.decodeInt("0b110101011011000101001"));
        assertEquals(1214605601, Bitwise.decodeInt("0b01001000011001010110100100100001"));
    }
}
