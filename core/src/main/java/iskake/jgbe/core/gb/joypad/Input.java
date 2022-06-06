package iskake.jgbe.core.gb.joypad;

public enum Input {
    DPAD_RIGHT(0),
    DPAD_LEFT(1),
    DPAD_UP(2),
    DPAD_DOWN(3),

    BUTTON_A(0),
    BUTTON_B(1),
    BUTTON_SELECT(2),
    BUTTON_START(3);

    public final int bit;

    private Input(int bit) {
        this.bit = bit;
    }
}
