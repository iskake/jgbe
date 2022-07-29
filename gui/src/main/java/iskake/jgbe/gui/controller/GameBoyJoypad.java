package iskake.jgbe.gui.controller;

import iskake.jgbe.core.gb.joypad.IJoypad;
import iskake.jgbe.core.gb.joypad.Input;

public class GameBoyJoypad implements IJoypad {
    private final boolean[] inputsButton = new boolean[4];
    private final boolean[] inputsDirection = new boolean[4];

    @Override
    public void setButtonActive(Input button) {
        inputsButton[button.bit] = true;
    }

    @Override
    public void setButtonInactive(Input button) {
        inputsButton[button.bit] = false;
    }

    @Override
    public void setDirectionActive(Input direction) {
        inputsDirection[direction.bit] = true;
    }

    @Override
    public void setDirectionInactive(Input direction) {
        inputsDirection[direction.bit] = false;
    }

    @Override
    public boolean[] getButtons() {
        return inputsButton;
    }

    @Override
    public int getButtonsAsInt() {
        int a = inputsButton[Input.BUTTON_A.bit]      ? 0 : 1 << Input.BUTTON_A.bit;
        int b = inputsButton[Input.BUTTON_B.bit]      ? 0 : 1 << Input.BUTTON_B.bit;
        int select = inputsButton[Input.BUTTON_SELECT.bit] ? 0 : 1 << Input.BUTTON_SELECT.bit;
        int start = inputsButton[Input.BUTTON_START.bit]  ? 0 : 1 << Input.BUTTON_START.bit;
        return a | b | select | start;
    }

    @Override
    public boolean[] getDirections() {
        return inputsDirection;
    }

    @Override
    public int getDirectionsAsInt() {
        int right = inputsDirection[Input.DPAD_RIGHT.bit] ? 0 : 1 << Input.DPAD_RIGHT.bit;
        int left = inputsDirection[Input.DPAD_LEFT.bit]   ? 0 : 1 << Input.DPAD_LEFT.bit;
        int up = inputsDirection[Input.DPAD_UP.bit]       ? 0 : 1 << Input.DPAD_UP.bit;
        int down = inputsDirection[Input.DPAD_DOWN.bit]   ? 0 : 1 << Input.DPAD_DOWN.bit;
        return right | left | up | down;
    }

}
