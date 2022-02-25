package tland.gb;

import tland.gb.Registers.RegisterIndex;

public class CPU {
    GameBoy gameBoy;

    public Registers reg;

    public CPU(GameBoy gameBoy) {
        this.gameBoy = gameBoy;
        reg = new Registers();
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

}
