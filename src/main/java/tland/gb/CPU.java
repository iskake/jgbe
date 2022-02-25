package tland.gb;

public class CPU {
    GameBoy gameBoy;

    public CPU(GameBoy gameBoy) {
        this.gameBoy = gameBoy;
    }

    public void run() {
        int i = 0;
        while (true) {
            i++;
            if (i > 10) {
                break;
            }
        }
        // printRegisters();
    }

    public int readNextByte() {
        // TODO
        return 0;
    }

}
