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
        reg.writeRegisterByte(0xff, RegisterIndex.A);
        assertEquals(0xff, reg.readRegisterByte(RegisterIndex.A));
        
        reg.writeRegisterByte(0x4e, RegisterIndex.B);
        reg.writeRegisterByte(0x67, RegisterIndex.C);
        assertEquals(0x4e67, reg.readRegisterShort(RegisterIndex.B));
        assertEquals(0x4e67, reg.readRegisterShort(RegisterIndex.C));

        reg.writeRegisterShort(0x3f5a, RegisterIndex.D);
        assertEquals(0x3f5a, reg.readRegisterShort(RegisterIndex.D));
        reg.writeRegisterShort(0x3f5a, RegisterIndex.E);
        assertEquals(0x3f5a, reg.readRegisterShort(RegisterIndex.D));
    }
}
