package tland.gb;

public class CPU {
    private final GameBoy gb;

    public CPU(GameBoy gameBoy) {
        this.gb = gameBoy;
    }

    public void run() {
        byte lastOpcode = gb.readNextByte();
        System.out.println(Integer.toHexString(Byte.toUnsignedInt(lastOpcode)) + " -> " + Opcodes.getOpcode(lastOpcode).getName());
        Opcodes.getOpcode(lastOpcode).doOp(gb, lastOpcode);
    }

}
