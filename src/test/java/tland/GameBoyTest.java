package tland;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import tland.emu.Emulator;
import tland.emu.Registers;
import tland.emu.Registers.Flags;
import tland.emu.Registers.RegisterIndex;
import tland.emu.cpu.Opcodes;
import tland.emu.mem.ROM;

public class GameBoyTest {
    static final byte[] b = Arrays.copyOf(new byte[0], ROM.ROM_SIZE);
    static Emulator emu = new Emulator(new ROM(b));
    static Registers reg;
    final RegisterIndex A = RegisterIndex.A;
    final RegisterIndex F = RegisterIndex.F;
    final RegisterIndex B = RegisterIndex.B;
    final RegisterIndex C = RegisterIndex.C;
    final RegisterIndex D = RegisterIndex.D;
    final RegisterIndex E = RegisterIndex.E;
    final RegisterIndex H = RegisterIndex.H;
    final RegisterIndex L = RegisterIndex.L;

    final RegisterIndex AF = RegisterIndex.AF;
    final RegisterIndex BC = RegisterIndex.BC;
    final RegisterIndex DE = RegisterIndex.DE;
    final RegisterIndex HL = RegisterIndex.HL;

    @BeforeAll
    static void initGameBoy() {
        emu = new Emulator(new ROM(b));
        reg = emu.reg();
    }

    void loadOpcode(int opcode) {
        Opcodes.getOpcode(opcode).doOp(emu, opcode);
    }

    byte readMem(short address) {
        return emu.readMemoryAddress(address);
    }

    void writeMem(short address, byte value) {
        emu.writeMemoryAddress(address, value);
    }

    @Test
    void registerTest() {
        reg.writeRegisterByte(A, 0xff);
        assertEquals(Bitwise.toByte(0xff), reg.readRegisterByte(A));

        reg.writeRegisterByte(B, 0x4e);
        reg.writeRegisterByte(C, 0x67);
        assertEquals(Bitwise.toShort(0x4e67), reg.readRegisterShort(BC));

        // Invalid byte/short register reading
        assertThrows(IndexOutOfBoundsException.class, () -> reg.readRegisterShort(B));
        assertThrows(IndexOutOfBoundsException.class, () -> reg.readRegisterByte(BC));

        reg.writeRegisterShort(DE, 0x3f5a);
        assertEquals(Bitwise.toShort(0x3f5a), reg.readRegisterShort(DE));

        reg.writeRegisterShort(HL, 0x3f5a);
        assertEquals(Bitwise.toByte(0x5a), reg.readRegisterByte(L));

        reg.writeRegisterShort(HL, 0xc350);
        reg.writeRegisterByte(HL, 0x34);
        assertEquals(readMem(reg.readRegisterShort(HL)), reg.readRegisterByte(HL));
    }

    @Test
    void flagTest() {
        reg.writeRegisterByte(F, (byte) 0b1010_0000);
        reg.setFlag(Flags.C);
        assertEquals((byte) 0b1011_0000, reg.readRegisterByte(F));
        assertTrue(reg.isFlagSet(Flags.C));

        reg.resetFlag(Flags.Z);
        assertEquals((byte) 0b0011_0000, reg.readRegisterByte(F));
        assertFalse(reg.isFlagSet(Flags.Z));

        reg.setFlag(Flags.Z);
        assertTrue(reg.isFlagSet(Flags.Z));
        reg.setFlag(Flags.N);
        reg.setFlag(Flags.H);
        reg.setFlag(Flags.C);
        assertEquals((byte) 0b1111_0000, reg.readRegisterByte(F));

        reg.resetFlag(Flags.Z);
        assertFalse(reg.isFlagSet(Flags.Z));
        assertTrue(reg.isFlagSet(Flags.N));
        reg.resetFlag(Flags.N);
        assertFalse(reg.isFlagSet(Flags.N));
        reg.resetFlag(Flags.H);
        reg.resetFlag(Flags.C);
        assertEquals((byte) 0, reg.readRegisterByte(F));
    }
}
