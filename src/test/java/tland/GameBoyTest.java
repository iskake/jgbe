package tland;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import tland.gb.GameBoy;
import tland.gb.Registers;
import tland.gb.Registers.RegisterIndex;

public class GameBoyTest {
    @Test
    void registerTest() {
        Registers reg = new Registers();
        reg.writeRegisterByte(RegisterIndex.A, 0xff);
        assertEquals(Bitwise.toByte(0xff), reg.readRegisterByte(RegisterIndex.A));
        
        reg.writeRegisterByte(RegisterIndex.B, 0x4e);
        reg.writeRegisterByte(RegisterIndex.C, 0x67);
        assertEquals(Bitwise.toShort(0x4e67), reg.readRegisterShort(RegisterIndex.BC));

        // Invalid byte/short register reading
        assertThrows(IndexOutOfBoundsException.class, () -> reg.readRegisterShort(RegisterIndex.B));
        assertThrows(IndexOutOfBoundsException.class, () -> reg.readRegisterByte(RegisterIndex.BC));

        reg.writeRegisterShort(RegisterIndex.DE, 0x3f5a);
        assertEquals(Bitwise.toShort(0x3f5a), reg.readRegisterShort(RegisterIndex.DE));

        reg.writeRegisterShort(RegisterIndex.HL, 0x3f5a);
        assertEquals(Bitwise.toByte(0x5a), reg.readRegisterByte(RegisterIndex.L));
    }

    @Test
    void bitwiseTest() {
        assertEquals((byte) 0xa8, Bitwise.getHighByte((short) 0xa8e0));
        assertEquals((byte) 0xe0, Bitwise.getLowByte((short) 0xa8e0));

        assertEquals((short) 0x1c25, Bitwise.toShort((byte) 0x1c, (byte) 0x25));
        assertEquals((short) 0x1c25, Bitwise.toShort(0x1c25));

        assertEquals((byte)0xce, Bitwise.toByte(0xa5ce));

        assertEquals(0x56, Bitwise.intAsByte(0x123456));
        assertEquals(0x3456, Bitwise.intAsShort(0x123456));

        assertEquals((byte)0b10110001, Bitwise.flipBit((byte)0b10110101, 2));
        assertEquals((byte)0b01001000, Bitwise.setBit((byte)0b00001000, 6));
        assertEquals((byte)0b11011111, Bitwise.clearBit((byte)0b11111111, 5));

        assertEquals("ff", Bitwise.toHexString((byte) 0xff));
        assertEquals("b256", Bitwise.toHexString((short) 0xb256));
        assertEquals("11111111", Bitwise.toBinaryString((byte) 0b11111111));
        assertEquals("1010111110111000", Bitwise.toBinaryString((short) 0b1010111110111000));
    }
}
