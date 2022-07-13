package iskake.jgbe.core.gb;

import iskake.jgbe.core.gb.GameBoy;
import iskake.jgbe.core.gb.IGameBoy;
import iskake.jgbe.core.gb.Registers;
import iskake.jgbe.core.gb.Registers.Flags;
import iskake.jgbe.core.gb.Registers.Register;
import org.junit.jupiter.api.Test;

import static iskake.jgbe.core.gb.Registers.Register.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Testing of registers
 */
public class RegistersTest {
    final Register[] byteRegisters = { A, F, B, C, D, E, H, L };
    final Register[] shortRegisters = { AF, BC, DE, HL, SP };
    IGameBoy gb = new GameBoy(null);
    Registers reg = new Registers(gb);

    @Test
    void byteRegisterTest() {
        reg.clearAll();
        for (Register register : byteRegisters) {
            // Check clearAll
            assertEquals(0, reg.readByte(register));
        }

        for (Register register : byteRegisters) {
            assertTrue(Registers.isByteRegister(register));
        }

        for (Register register : shortRegisters) {
            assertFalse(Registers.isByteRegister(register));
        }

        // Writing and reading
        reg.writeByte(A, (byte) 0x10);
        assertEquals((byte) 0x10, reg.readByte(A));
        reg.writeByte(A, 0x12);
        assertEquals((byte) 0x12, reg.readByte(A));
        reg.incByte(A);
        assertEquals((byte) 0x13, reg.readByte(A));
        reg.decByte(A);
        assertEquals((byte) 0x12, reg.readByte(A));

        // Bitwise operations are only for byte registers
        // Note: bit0 = rightmost/lowest bit, bit7 = leftmost/highest bit
        reg.writeByte(D, (byte) 0b1010_1010);
        assertFalse(reg.isBitSet(D, 0));
        assertTrue(reg.isBitSet(D, 1));

        reg.setBit(D, 0);
        assertTrue(reg.isBitSet(D, 0));

        reg.resetBit(D, 7);
        assertFalse(reg.isBitSet(D, 7));

        // Bit shift operations are only for byte registers
        reg.writeByte(B, (byte) 0b1000_0001);
        reg.rotateLeft(B, false); // Rotate left without carry (bit0 = bit7 old value)
        assertEquals((byte) 0b0000_0011, reg.readByte(B));
        assertFalse(reg.isFlagSet(Flags.C));
        reg.rotateRight(B, false); // Rotate right without carry (bit7 = bit0 old value)
        assertEquals((byte) 0b1000_0001, reg.readByte(B));
        assertFalse(reg.isFlagSet(Flags.C));

        reg.writeByte(B, (byte) 0b1000_0001);
        reg.setFlag(Flags.C);
        reg.rotateLeft(B, true); // Rotate left WITH carry (bit0 = 1 if Flags.C is set)
        assertEquals((byte) 0b0000_0011, reg.readByte(B));
        assertTrue(reg.isFlagSet(Flags.C));
        reg.rotateRight(B, true); // Rotate right WITH carry (bit7 = 1 if Flags.C is set)
        assertEquals((byte) 0b1000_0001, reg.readByte(B));
        assertTrue(reg.isFlagSet(Flags.C));

        reg.writeByte(B, (byte) 0b1000_0001);
        reg.shiftLeft(B); // Shift left (arithmetically, bit0 = 0)
        assertEquals((byte) 0b0000_0010, reg.readByte(B));

        reg.writeByte(B, (byte) 0b1001_1001);
        reg.shiftRight(B, false); // Shift right (arithmetically, bit7 = bit7 old value)
        assertEquals((byte) 0b1100_1100, reg.readByte(B));

        reg.writeByte(B, (byte) 0b1001_1001);
        reg.shiftRight(B, true); // Shift right (logically, bit7 = 0)
        assertEquals((byte) 0b0100_1100, reg.readByte(B));

        reg.writeByte(B, (byte) 0b1111_0000);
        reg.swap(B); // Swap lower and higher 4 bits.
        assertEquals((byte) 0b0000_1111, reg.readByte(B));
    }

    @Test
    void shortRegisterTest() {
        reg.clearAll();
        for (Register register : shortRegisters) {
            // Check clearAll
            assertEquals(0, reg.readByte(register));
        }

        for (Register register : byteRegisters) {
            assertFalse(Registers.isShortRegister(register));
        }

        for (Register register : shortRegisters) {
            assertTrue(Registers.isShortRegister(register));
        }

        reg.writeShort(DE, (short) 0xfefe);
        assertEquals((short) 0xfefe, reg.readShort(DE));
        reg.writeShort(DE, 0x1234);
        assertEquals((byte) 0x12, reg.readByte(D));
        assertEquals((byte) 0x34, reg.readByte(E));
        assertEquals((short) 0x1234, reg.readShort(DE));
        reg.incShort(DE);
        assertEquals((short) 0x1235, reg.readShort(DE));
        reg.decShort(DE);
        assertEquals((short) 0x1234, reg.readShort(DE));

        reg.writeShort(HL, 0x8000);
        reg.writeByte(HL, 0x42);
        assertEquals((byte)0x42, reg.readByte(HL));
        assertEquals((byte)0x42, gb.readAddress((short)0x8000));
    }

    @Test
    void flagsTest() {
        reg.clearAll();
        reg.setFlag(Flags.Z);
        assertTrue(reg.isFlagSet(Flags.Z));
        // Note: it is not actually possible to manually read the F register
        // (e.g. there is no `ld a, f` instruction) but this is just for testing...
        assertEquals((byte)0b1000_0000, reg.readByte(F));
        
        reg.setFlag(Flags.C);
        assertTrue(reg.isFlagSet(Flags.Z));
        assertEquals((byte)0b1001_0000, reg.readByte(F));
        
        reg.resetFlag(Flags.Z);
        assertFalse(reg.isFlagSet(Flags.Z));
        assertEquals((byte)0b0001_0000, reg.readByte(F));
        
        reg.complementFlag(Flags.H);
        assertTrue(reg.isFlagSet(Flags.H));
        assertEquals((byte)0b0011_0000, reg.readByte(F));
        
        reg.complementFlag(Flags.H);
        assertFalse(reg.isFlagSet(Flags.H));
        assertEquals((byte)0b0001_0000, reg.readByte(F));

        reg.setFlag(Flags.Z);
        reg.setFlag(Flags.N);
        reg.setFlag(Flags.H);
        reg.setFlag(Flags.C);
        assertTrue(reg.isFlagSet(Flags.Z));
        assertTrue(reg.isFlagSet(Flags.N));
        assertTrue(reg.isFlagSet(Flags.H));
        assertTrue(reg.isFlagSet(Flags.C));
        assertEquals((byte)0b1111_0000, reg.readByte(F));
        reg.resetFlag(Flags.Z);
        reg.resetFlag(Flags.N);
        reg.resetFlag(Flags.H);
        reg.resetFlag(Flags.C);
        assertFalse(reg.isFlagSet(Flags.Z));
        assertFalse(reg.isFlagSet(Flags.N));
        assertFalse(reg.isFlagSet(Flags.H));
        assertFalse(reg.isFlagSet(Flags.C));
        assertEquals((byte)0b0000_0000, reg.readByte(F));

        reg.setFlagConditional(Flags.N, (1 == 2));
        assertFalse(reg.isFlagSet(Flags.N));

        reg.setFlag(Flags.C);
        reg.setFlagConditional(Flags.N, reg.isFlagSet(Flags.C));
        assertTrue(reg.isFlagSet(Flags.N));
    }
}
