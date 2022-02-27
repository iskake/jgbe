package tland.gb;

public class CPU {
    private final GameBoy gb;

    public CPU(GameBoy gameBoy) {
        this.gb = gameBoy;
    }

    public void run() {
        byte lastOpcode = gb.readNextByte();
        Opcodes.getOpcode(lastOpcode).doOp(gb, gb.readNextByte());
    }

}
