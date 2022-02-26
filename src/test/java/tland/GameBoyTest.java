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
        assertEquals(0xff, reg.readRegisterByte(RegisterIndex.A));
        
        reg.writeRegisterByte(RegisterIndex.B, 0x4e);
        reg.writeRegisterByte(RegisterIndex.C, 0x67);
        assertEquals(0x4e67, reg.readRegisterShort(RegisterIndex.BC));

        // Invalid byte/short register reading
        assertThrows(IndexOutOfBoundsException.class, () -> reg.readRegisterShort(RegisterIndex.B));
        assertThrows(IndexOutOfBoundsException.class, () -> reg.readRegisterByte(RegisterIndex.BC));

        reg.writeRegisterShort(RegisterIndex.DE, 0x3f5a);
        assertEquals(0x3f5a, reg.readRegisterShort(RegisterIndex.DE));

        reg.writeRegisterShort(RegisterIndex.HL, 0x3f5a);
        assertEquals(0x5a, reg.readRegisterByte(RegisterIndex.L));
    }
}
