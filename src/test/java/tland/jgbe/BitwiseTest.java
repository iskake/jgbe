package tland.jgbe;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

        assertEquals((byte) 0b10110001, Bitwise.flipBit((byte) 0b10110101, 2));
        assertEquals((byte) 0b01001000, Bitwise.setBit((byte) 0b00001000, 6));
        assertEquals((byte) 0b11011111, Bitwise.clearBit((byte) 0b11111111, 5));
    }
}
