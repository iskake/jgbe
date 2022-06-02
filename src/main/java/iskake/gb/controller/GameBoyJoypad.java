package iskake.gb.controller;

import iskake.gb.joypad.IJoypad;
import iskake.gb.joypad.Input;

public class GameBoyJoypad implements IJoypad {
    // TODO: everything?

    private boolean[] inputsButton = new boolean[4];
    private boolean[] inputsDirection = new boolean[4];

    @Override
    public void setButtonActive(Input button) {
        inputsButton[button.bit] = true;
    }

    @Override
    public void setButtonInactive(Input button) {
        inputsButton[button.bit] = false;
    }

    @Override
    public void setDirectionActive(Input button) {
        inputsDirection[button.bit] = true;
    }

    @Override
    public void setDirectionInactive(Input button) {
        inputsDirection[button.bit] = false;
    }

    @Override
    public boolean[] getButtons() {
        return inputsButton;
    }

    @Override
    public int getButtonsAsInt() {
        int tmp = 0;
        int a = inputsButton[Input.BUTTON_A.bit] ? 0 : 1 << Input.BUTTON_A.bit;
        int b = inputsButton[Input.BUTTON_A.bit] ? 0 : 1 << Input.BUTTON_B.bit;
        int select = inputsButton[Input.BUTTON_A.bit] ? 0 : 1 << Input.BUTTON_SELECT.bit;
        int start = inputsButton[Input.BUTTON_A.bit] ? 0 : 1 << Input.BUTTON_START.bit;
        tmp = a | b | select | start;
        return tmp;
    }

    @Override
    public boolean[] getDirections() {
        return inputsDirection;
    }

    @Override
    public int getDirectionsAsInt() {
        int right = inputsButton[Input.DPAD_RIGHT.bit] ? 0 : 1 << Input.DPAD_RIGHT.bit;
        int left = inputsButton[Input.DPAD_LEFT.bit] ? 0 : 1 << Input.DPAD_LEFT.bit;
        int up = inputsButton[Input.DPAD_UP.bit] ? 0 : 1 << Input.DPAD_UP.bit;
        int down = inputsButton[Input.DPAD_DOWN.bit] ? 0 : 1 << Input.DPAD_DOWN.bit;
        return right | left | up | down;
    }

}
