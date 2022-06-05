package iskake.gb;

import iskake.Bitwise;
import iskake.gb.mem.VRAM;

public class DMAController {
    // TODO: make it work correctly

    private boolean dmaActive;
    private boolean dmaJustStarted;
    private long dmaCyclesLeft;
    private byte address;
    private GameBoy gb;

    public DMAController(GameBoy gb) {
        this.gb = gb;
        dmaActive = false;
        dmaJustStarted = true;
        dmaCyclesLeft = -1;
        address = (byte)0xff;
    }

    public void startDMATransfer(byte address) {
        dmaActive = true;
        dmaJustStarted = true;
        dmaCyclesLeft = 160 * 4;
        this.address = address;
    }

    public void decCycles() {
        if (dmaCyclesLeft > 0) {
            dmaCyclesLeft--;
        } else if (dmaCyclesLeft == 0) {
            dmaActive = false;
            dmaCyclesLeft--;
        }
    }

    public boolean DMAJustStarted() {
        return dmaJustStarted;
    }

    public boolean isDMAActive() {
        return dmaActive;
    }

    public void DMATransfer() {
        for (int i = 0; i < 0x100; i++) {
            short RAMaddress = Bitwise.toShort(address, (byte)i);
            short OAMaddress = Bitwise.toShort((byte)0xfe, (byte)i);
            gb.writeMemoryAddress(OAMaddress, gb.readMemoryNoCycle(RAMaddress));
        }
    }

}
