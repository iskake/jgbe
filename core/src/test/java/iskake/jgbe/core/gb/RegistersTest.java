package iskake.jgbe.core.gb;

import iskake.jgbe.core.gb.GameBoy;
import iskake.jgbe.core.gb.IGameBoy;
import iskake.jgbe.core.gb.Registers;
import iskake.jgbe.core.gb.Registers.Flags;
import iskake.jgbe.core.gb.Registers.RegisterIndex;
import org.junit.jupiter.api.Test;

import static iskake.jgbe.core.gb.Registers.RegisterIndex.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Testing of registers
 */
public class RegistersTest {
    final RegisterIndex[] byteRegisters = { A, F, B, C, D, E, H, L };
    final RegisterIndex[] shortRegisters = { AF, BC, DE, HL, SP };
    IGameBoy gb = new GameBoy(null);
    Registers reg = new Registers(gb);

    @Test
    void byteRegisterTest() {
        reg.clearAll();
        for (RegisterIndex register : byteRegisters) {
            // Check clearAll
            assertEquals(0, reg.readRegisterByte(register));
        }

        for (RegisterIndex register : byteRegisters) {
            assertTrue(Registers.isRegisterByte(register));
        }

        for (RegisterIndex register : shortRegisters) {
            assertFalse(Registers.isRegisterByte(register));
        }

        // Writing and reading
        reg.writeRegisterByte(A, (byte) 0x10);
        assertEquals((byte) 0x10, reg.readRegisterByte(A));
        reg.writeRegisterByte(A, 0x12);
        assertEquals((byte) 0x12, reg.readRegisterByte(A));
        reg.incRegisterByte(A);
        assertEquals((byte) 0x13, reg.readRegisterByte(A));
        reg.decRegisterByte(A);
        assertEquals((byte) 0x12, reg.readRegisterByte(A));

        // Bitwise operations are only for byte registers
        // Note: bit0 = rightmost/lowest bit, bit7 = leftmost/highest bit
        reg.writeRegisterByte(D, (byte) 0b1010_1010);
        assertFalse(reg.isBitSet(D, 0));
        assertTrue(reg.isBitSet(D, 1));

        reg.setBit(D, 0);
        assertTrue(reg.isBitSet(D, 0));

        reg.resetBit(D, 7);
        assertFalse(reg.isBitSet(D, 7));

        // Bit shift operations are only for byte registers
        reg.writeRegisterByte(B, (byte) 0b1000_0001);
        reg.rotateLeft(B, false); // Rotate left without carry (bit0 = bit7 old value)
        assertEquals((byte) 0b0000_0011, reg.readRegisterByte(B));
        assertFalse(reg.isFlagSet(Flags.C));
        reg.rotateRight(B, false); // Rotate right without carry (bit7 = bit0 old value)
        assertEquals((byte) 0b1000_0001, reg.readRegisterByte(B));
        assertFalse(reg.isFlagSet(Flags.C));

        reg.writeRegisterByte(B, (byte) 0b1000_0001);
        reg.setFlag(Flags.C);
        reg.rotateLeft(B, true); // Rotate left WITH carry (bit0 = 1 if Flags.C is set)
        assertEquals((byte) 0b0000_0011, reg.readRegisterByte(B));
        assertTrue(reg.isFlagSet(Flags.C));
        reg.rotateRight(B, true); // Rotate right WITH carry (bit7 = 1 if Flags.C is set)
        assertEquals((byte) 0b1000_0001, reg.readRegisterByte(B));
        assertTrue(reg.isFlagSet(Flags.C));

        reg.writeRegisterByte(B, (byte) 0b1000_0001);
        reg.shiftLeft(B); // Shift left (arithmetically, bit0 = 0)
        assertEquals((byte) 0b0000_0010, reg.readRegisterByte(B));

        reg.writeRegisterByte(B, (byte) 0b1001_1001);
        reg.shiftRight(B, false); // Shift right (arithmetically, bit7 = bit7 old value)
        assertEquals((byte) 0b1100_1100, reg.readRegisterByte(B));

        reg.writeRegisterByte(B, (byte) 0b1001_1001);
        reg.shiftRight(B, true); // Shift right (logically, bit7 = 0)
        assertEquals((byte) 0b0100_1100, reg.readRegisterByte(B));

        reg.writeRegisterByte(B, (byte) 0b1111_0000);
        reg.swap(B); // Swap lower and higher 4 bits.
        assertEquals((byte) 0b0000_1111, reg.readRegisterByte(B));
    }

    @Test
    void shortRegisterTest() {
        reg.clearAll();
        for (RegisterIndex register : shortRegisters) {
            // Check clearAll
            assertEquals(0, reg.readRegisterByte(register));
        }

        for (RegisterIndex register : byteRegisters) {
            assertFalse(Registers.isRegisterShort(register));
        }

        for (RegisterIndex register : shortRegisters) {
            assertTrue(Registers.isRegisterShort(register));
        }

        reg.writeRegisterShort(DE, (short) 0xfefe);
        assertEquals((short) 0xfefe, reg.readRegisterShort(DE));
        reg.writeRegisterShort(DE, 0x1234);
        assertEquals((byte) 0x12, reg.readRegisterByte(D));
        assertEquals((byte) 0x34, reg.readRegisterByte(E));
        assertEquals((short) 0x1234, reg.readRegisterShort(DE));
        reg.incRegisterShort(DE);
        assertEquals((short) 0x1235, reg.readRegisterShort(DE));
        reg.decRegisterShort(DE);
        assertEquals((short) 0x1234, reg.readRegisterShort(DE));

        reg.writeRegisterShort(HL, 0x8000);
        reg.writeRegisterByte(HL, 0x42);
        assertEquals((byte)0x42, reg.readRegisterByte(HL));
        assertEquals((byte)0x42, gb.readMemoryAddress((short)0x8000));
    }

    @Test
    void flagsTest() {
        reg.clearAll();
        reg.setFlag(Flags.Z);
        assertTrue(reg.isFlagSet(Flags.Z));
        // Note: it is not actually possible to manually read the F register
        // (e.g. there is no `ld a, f` instruction) but this is just for testing...
        assertEquals((byte)0b1000_0000, reg.readRegisterByte(F));
        
        reg.setFlag(Flags.C);
        assertTrue(reg.isFlagSet(Flags.Z));
        assertEquals((byte)0b1001_0000, reg.readRegisterByte(F));
        
        reg.resetFlag(Flags.Z);
        assertFalse(reg.isFlagSet(Flags.Z));
        assertEquals((byte)0b0001_0000, reg.readRegisterByte(F));
        
        reg.complementFlag(Flags.H);
        assertTrue(reg.isFlagSet(Flags.H));
        assertEquals((byte)0b0011_0000, reg.readRegisterByte(F));
        
        reg.complementFlag(Flags.H);
        assertFalse(reg.isFlagSet(Flags.H));
        assertEquals((byte)0b0001_0000, reg.readRegisterByte(F));

        reg.setFlag(Flags.Z);
        reg.setFlag(Flags.N);
        reg.setFlag(Flags.H);
        reg.setFlag(Flags.C);
        assertTrue(reg.isFlagSet(Flags.Z));
        assertTrue(reg.isFlagSet(Flags.N));
        assertTrue(reg.isFlagSet(Flags.H));
        assertTrue(reg.isFlagSet(Flags.C));
        assertEquals((byte)0b1111_0000, reg.readRegisterByte(F));
        reg.resetFlag(Flags.Z);
        reg.resetFlag(Flags.N);
        reg.resetFlag(Flags.H);
        reg.resetFlag(Flags.C);
        assertFalse(reg.isFlagSet(Flags.Z));
        assertFalse(reg.isFlagSet(Flags.N));
        assertFalse(reg.isFlagSet(Flags.H));
        assertFalse(reg.isFlagSet(Flags.C));
        assertEquals((byte)0b0000_0000, reg.readRegisterByte(F));

        reg.setFlagConditional(Flags.N, (1 == 2));
        assertFalse(reg.isFlagSet(Flags.N));

        reg.setFlag(Flags.C);
        reg.setFlagConditional(Flags.N, reg.isFlagSet(Flags.C));
        assertTrue(reg.isFlagSet(Flags.N));
    }
}
