package tland;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
        assertEquals(0x4e67, reg.readRegisterShort(RegisterIndex.B));
        assertEquals(0x4e67, reg.readRegisterShort(RegisterIndex.C));

        reg.writeRegisterShort(RegisterIndex.D, 0x3f5a);
        assertEquals(0x3f5a, reg.readRegisterShort(RegisterIndex.D));
        reg.writeRegisterShort(RegisterIndex.E, 0x3f5a);
        assertEquals(0x3f5a, reg.readRegisterShort(RegisterIndex.D));
    }
}
