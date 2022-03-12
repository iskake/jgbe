package tland.gb.cpu;

import tland.gb.GameBoy;

public class CPU {
    private final GameBoy gb;

    public CPU(GameBoy gameBoy) {
        this.gb = gameBoy;
    }

    public void step() {
        byte lastOpcode = gb.readNextByte();
        // TODO: add size (and cycles?) constant to opcodes?
        // (e.g. xor b -> 1 byte, ld c, $n8 -> 2 bytes, ld de, $n16 -> 3 bytes)
        System.out.printf("%02x -> %s\n", Byte.toUnsignedInt(lastOpcode), String.format(Opcodes.getOpcode(lastOpcode).getName(), gb.readMemoryAddress(gb.getPC()), gb.readMemoryAddress((short) (gb.getPC() + 1))));
        Opcodes.getOpcode(lastOpcode).doOp(gb, Byte.toUnsignedInt(lastOpcode));
        gb.reg.printRegisters();
    }

}
