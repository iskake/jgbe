package iskake.jgbe.emu.pointer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Random;

import org.junit.jupiter.api.Test;

import iskake.Bitwise;
import iskake.gb.GameBoy;
import iskake.gb.IGameBoy;
import iskake.gb.pointer.Pointer;
import iskake.gb.pointer.ProgramCounter;
import iskake.gb.pointer.StackPointer;

public class PointerTest {

    // Test that normal pointer operations work.
    void simplePointerTest(Pointer p, short startAddress) {
        short oldValue = p.get();
        assertEquals(startAddress, oldValue);

        p.set((short)0x2032);
        assertEquals((short)0x2032, p.get());

        oldValue = p.get();
        assertEquals(oldValue, p.inc()); // pc.inc() is post-increment (e.g. i++)
        assertEquals(oldValue + 1, p.get());

        oldValue = p.get();
        assertEquals(oldValue, p.dec()); // pc.dec() is post-decrement (e.g. i--)
        assertEquals(oldValue - 1, p.get());

        p.set(startAddress);
        assertEquals(startAddress, p.get());
    }

    @Test
    void testProgramCounter() {
        simplePointerTest(new ProgramCounter((short)0x100), (short)0x100);
    }

    // Test that push and pop function properly
    void checkStackPointerPopPush(IGameBoy emu, StackPointer sp, short valueToPush) {
        short initialAddress = sp.get();
        sp.push(valueToPush); // push decrements twice and writes the specified value. 
        byte lo = Bitwise.getLowByte(valueToPush);
        byte hi = Bitwise.getHighByte(valueToPush);
        assertEquals(hi, emu.readMemoryAddress((short)(initialAddress - 1)));
        assertEquals(lo, emu.readMemoryAddress((short)(initialAddress - 2)));
        assertEquals((short)(initialAddress - 2), sp.get());
        assertEquals(valueToPush, sp.pop());
    }

    @Test
    void testStackPointer() {
        IGameBoy emu = new GameBoy(null);
        StackPointer sp = new StackPointer(emu, (short)0xfffe);
        simplePointerTest(sp, (short)0xfffe);

        // Test for specific addresses
        sp.set((short)0xfffe);
        checkStackPointerPopPush(emu, sp, (short)0x1234);
        assertEquals((byte)0x12, emu.readMemoryAddress((short)(0xfffe - 1)));
        assertEquals((byte)0x34, emu.readMemoryAddress((short)(0xfffe - 2)));

        sp.set((short)0x6781);
        checkStackPointerPopPush(emu, sp, (short)0x5678);
        assertEquals((byte)0x56, emu.readMemoryAddress((short)(0x6781 - 1)));
        assertEquals((byte)0x78, emu.readMemoryAddress((short)(0x6781 - 2)));

        sp.set((short)0x4945);
        checkStackPointerPopPush(emu, sp, (short)0x90ab);
        assertEquals((byte)0x90, emu.readMemoryAddress((short)(0x4945 - 1)));
        assertEquals((byte)0xab, emu.readMemoryAddress((short)(0x4945 - 2)));

        sp.set((short)0xefff);
        checkStackPointerPopPush(emu, sp, (short)0xcdef);
        assertEquals((byte)0xcd, emu.readMemoryAddress((short)(0xefff - 1)));
        assertEquals((byte)0xef, emu.readMemoryAddress((short)(0xefff - 2)));

        sp.set((short)0xffff);
        checkStackPointerPopPush(emu, sp, (short)0xdead);
        assertEquals((byte)0xde, emu.readMemoryAddress((short)(0xffff - 1)));
        assertEquals((byte)0xad, emu.readMemoryAddress((short)(0xffff - 2)));

        sp.set((short)0x0000);
        checkStackPointerPopPush(emu, sp, (short)0xbeef);
        assertEquals((byte)0xbe, emu.readMemoryAddress((short)(0x0000 - 1)));
        assertEquals((byte)0xef, emu.readMemoryAddress((short)(0x0000 - 2)));

        // Test for random addresses
        Random rand = new Random();
        for (int i = 0; i < 10_000_000; i++) {
            sp.set((short)rand.nextInt(0,0x10_000));
            checkStackPointerPopPush(emu, sp, (short)rand.nextInt(0,0x10_000));
        }
    }
}
