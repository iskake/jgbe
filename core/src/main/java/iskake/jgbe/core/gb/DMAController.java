package iskake.jgbe.core.gb;

import iskake.jgbe.core.Bitwise;

public class DMAController {
    private boolean dmaActive;
    private long dmaCyclesLeft;
    private byte address;
    private final GameBoy gb;

    public DMAController(GameBoy gb) {
        this.gb = gb;
        dmaActive = false;
        dmaCyclesLeft = -1;
        address = (byte)0xff;
    }

    public void startDMATransfer(byte address) {
        dmaActive = true;
        dmaCyclesLeft = 160 * 4;
        this.address = address;
    }

    public void decCycles() {
        if (dmaCyclesLeft > 0) {
            dmaCyclesLeft--;
        } else if (dmaCyclesLeft == 0) {
            DMATransfer();
            dmaActive = false;
            dmaCyclesLeft--;
        }
    }

    public boolean isDMAActive() {
        return dmaActive;
    }

    public void DMATransfer() {
        for (int i = 0; i < 160; i++) {
            short ramAddress = Bitwise.toShort(address, (byte)i);
            short oamAddress = Bitwise.toShort((byte)0xfe, (byte)i);
            gb.writeAddressInternal(oamAddress, gb.readAddressInternal(ramAddress));
        }
    }

}
