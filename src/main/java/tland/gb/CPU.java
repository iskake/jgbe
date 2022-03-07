package tland.gb;

public class CPU {
    private final GameBoy gb;

    public CPU(GameBoy gameBoy) {
        this.gb = gameBoy;
    }

    public void step() {
        byte lastOpcode = gb.readNextByte();
        System.out.printf("%02x -> %s\n", Byte.toUnsignedInt(lastOpcode), String.format(Opcodes.getOpcode(lastOpcode).getName(), gb.readMemoryAddress(gb.getPC()), gb.readMemoryAddress((short)(gb.getPC() + 1))));
        Opcodes.getOpcode(lastOpcode).doOp(gb, Byte.toUnsignedInt(lastOpcode));
        gb.reg.printRegisters();
    }

}
