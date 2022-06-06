// package iskake.jgbe.gb.mem;

// import static org.junit.jupiter.api.Assertions.assertEquals;

// import org.junit.jupiter.api.Test;

// import iskake.jgbe.core.gb.mem.MemoryMap;
// import iskake.jgbe.core.gb.mem.RAM;
// import iskake.jgbe.core.gb.mem.ROM;

// public class MemoryTest {
    
//     @Test
//     void testROM() {
//         byte[] b = new byte[0x8000];
//         b[0] = 1;
//         b[1] = 2;
//         b[2] = 3;
//         ROM rom = new ROM(b);
//         for (int i = 0; i < rom.data().length; i++) {
//             if (i < 3) {
//                 assertEquals((byte)(i+1), rom.data()[i]);
//                 assertEquals((byte)(i+1), rom.readAddress(i));
//             } else {
//                 assertEquals((byte)0, rom.data()[i]);
//                 assertEquals((byte)0, rom.readAddress(i));
//             }
//         }
//     }

//     @Test
//     void testRAM() {
//         RAM ram = new RAM(0x8000);
//         for (int i = 0; i < ram.size; i++) {
//             assertEquals((byte)0, ram.readAddress(i));
//             ram.writeAddress(i, (byte)i);
//         }
//         for (int i = 0; i < ram.size; i++) {
//             assertEquals((byte)i, ram.readAddress(i));
//         }
//     }

//     @Test
//     void testMemoryMap() {
//         // No ROM, so all bytes should be writable
//         MemoryMap memMap = new MemoryMap(null);
//         for (int i = 0; i < 0x10_000; i++) {
//             assertEquals((byte)0, memMap.readAddress(i));
//             memMap.writeAddress(i, (byte)i);
//             assertEquals((byte)i, memMap.readAddress(i));
//         }
//         ROM rom = new ROM(new byte[0x8000]);
//         memMap = new MemoryMap(rom);
//         for (int i = 0; i < 0x10_000; i++) {
//             assertEquals((byte)0, memMap.readAddress(i));
//             memMap.writeAddress(i, (byte)i);
            
//             if (i < 0x8000)
//                 assertEquals((byte)0, memMap.readAddress(i));
//             else
//                 assertEquals((byte)i, memMap.readAddress(i));
//         }
//     }
// }
