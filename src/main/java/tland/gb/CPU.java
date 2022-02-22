package tland.gb;

import tland.gb.Registers.RegisterIndex;

public class CPU {

    public Registers reg;

    public CPU() {
        reg = new Registers();
    }

    public void run(ROM rom) {
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
