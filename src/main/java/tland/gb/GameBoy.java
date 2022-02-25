package tland.gb;

public class GameBoy {
    ROM rom;
    CPU cpu;
    int pc;
    int sp;
    public Registers reg;

    public GameBoy(ROM rom) {
        this.rom = rom;
        reg = new Registers();
        cpu = new CPU(this);
    }

    public void run() {
        for (int i = 0; i < 0x100; i++) {
            System.out.println(Opcodes.getOpcode(i).getName());

        }
        cpu.run();
    }

    public int readNextByte() {
        return cpu.readNextByte();
    }
}
