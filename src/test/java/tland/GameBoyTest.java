package tland;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import tland.gb.GameBoy;
import tland.gb.Registers;
import tland.gb.Registers.Flags;
import tland.gb.Registers.RegisterIndex;
import tland.gb.cpu.Opcodes;
import tland.gb.mem.CartridgeROM;
import tland.gb.mem.ROMBank;

public class GameBoyTest {
    static final byte[] b = Arrays.copyOf(new byte[0], ROMBank.BANK_SIZE * 2);
    static GameBoy gb = new GameBoy(new CartridgeROM(b));
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
        gb = new GameBoy(new CartridgeROM(b));
        reg = gb.reg;
    }

    void loadOpcode(int opcode) {
        Opcodes.getOpcode(opcode).doOp(gb, opcode);
    }

    byte readMem(short address) {
        return gb.readMemoryAddress(address);
    }

    void writeMem(short address, byte value) {
        gb.writeMemoryAddress(address, value);
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

    @Test
    void LDInstructionTest() {
        // ld h, $93
        writeMem((short) 0xc100, (byte) 0x93);
        gb.setPC((short) 0xc100);
        loadOpcode(0x26);
        assertEquals((byte) 0x93, readMem((short) 0xc100));
        assertEquals((byte) 0x93, reg.readRegisterByte(H));

        // ld a, b
        reg.writeRegisterByte(B, 0x36);
        loadOpcode(0x78);
        assertEquals(reg.readRegisterByte(B), reg.readRegisterByte(A));
        assertEquals(0x36, reg.readRegisterByte(A));

        // ld b, [hl]
        gb.writeMemoryAddress((short) 0xc000, (byte) 0x45);
        reg.writeRegisterShort(HL, 0xc000);
        loadOpcode(0x46);
        assertEquals(readMem((short) 0xc000), reg.readRegisterByte(B));

        // ld de, $0xaf6e
        gb.setPC((short) 0xc100);
        writeMem((short) 0xc100, (byte) 0x6e);
        writeMem((short) 0xc101, (byte) 0xaf);
        loadOpcode(0x11);
        assertEquals((short) 0xaf6e, reg.readRegisterShort(DE));

        // ld a, [bc]
        writeMem((short) 0xc100, (byte) 0xb4);
        reg.writeRegisterShort(BC, (short) 0xc100);
        loadOpcode(0x0a);
        assertEquals((byte) 0xb4, reg.readRegisterByte(A));
    }
}
