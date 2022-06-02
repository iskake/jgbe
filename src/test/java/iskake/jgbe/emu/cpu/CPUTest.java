package iskake.jgbe.emu.cpu;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import iskake.gb.GameBoy;
import iskake.gb.Registers.RegisterIndex;
import iskake.gb.cpu.CPU;
import iskake.gb.mem.ROM;

/*
 * Note: due to the complexity of (and amount of) instructions,
 * instructions have to be tested manually (see src/test/asm for instruction tests).
 * Instead, this file only tests very simple stepping of instructions.
 */
public class CPUTest {

    @Test
    void testStep() {
        byte[] b = new byte[0x8000];
        b[0] = (byte)0x00; // nop          - 1 byte
        b[1] = (byte)0x00; // nop          - 1 byte
        b[2] = (byte)0x3e; // ld a, $12    - 2 bytes
        b[3] = (byte)0x12;
        b[4] = (byte)0x01; // ld bc, $1234 - 3 bytes
        b[5] = (byte)0x34; //   note: little endian (upper byte stored first)
        b[6] = (byte)0x12; //   note: little endian (lower byte stored last)
        b[7] = (byte)0xc5; // push bc      - 1 byte
        b[8] = (byte)0xd1; // push bc      - 1 byte
        b[9] = (byte)0x10; // stop         - 1 byte

        GameBoy emu = new GameBoy(new ROM(b));
        CPU cpu = new CPU(emu);

        assertTrue(emu.isRunning());

        emu.pc().set((short)0);
        assertEquals((short)0, emu.pc().get());

        cpu.step(); // pc -> nop
        assertEquals((short)0x1, emu.pc().get());

        cpu.step(); // pc -> nop
        assertEquals((short)0x2, emu.pc().get());

        cpu.step(); // pc -> ld a, $12
        assertEquals((short)0x4, emu.pc().get());
        assertEquals((byte)0x12, emu.reg().readRegisterByte(RegisterIndex.A));

        cpu.step(); // pc -> ld bc, $1234
        assertEquals((short)0x7, emu.pc().get());
        assertEquals((short)0x1234, emu.reg().readRegisterShort(RegisterIndex.BC));

        assertEquals((short)0xfffe, emu.sp().get());
        cpu.step(); // pc -> push bc
        assertEquals((short)0x8, emu.pc().get());
        assertEquals((short)0x1234, emu.reg().readRegisterShort(RegisterIndex.BC));
        assertEquals((short)0xfffc, emu.sp().get());

        cpu.step(); // pc -> pop, de
        assertEquals((short)0x9, emu.pc().get());
        assertEquals((short)0x1234, emu.reg().readRegisterShort(RegisterIndex.DE));
        assertEquals((short)0xfffe, emu.sp().get());

        assertTrue(emu.isRunning());
        cpu.step(); // pc -> stop
        assertEquals((short)0xa, emu.pc().get());

        assertFalse(emu.isRunning());
    }
}
