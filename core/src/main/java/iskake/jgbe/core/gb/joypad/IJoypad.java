package iskake.jgbe.core.gb.joypad;

/**
 * Represents a joypad, as 
 */
public interface IJoypad {

    /**
     * Read the value of the joypad, for use in the P1 register.
     *
     * @param value The value to write to the P1 register.
     */
    void write(byte value);

    /**
     * Read the value of the joypad, for use in the P1 register.
     *
     * @return The value of the P1 register.
     */
    byte read();

    /**
     * Set the specified button to be active (pressed).
     * 
     * @param button The button to be set as active.
     */
    void setButtonActive(Input button);

    /**
     * Set the specified button to be inactive (not pressed).
     * 
     * @param button The button to be set as inactive.
     */
    void setButtonInactive(Input button);

    /**
     * Set the specified direction to be active (pressed).
     * 
     * @param direction The direction to be set as active.
     */
    void setDirectionActive(Input direction);

    /**
     * Set the specified direction to be inactive (not pressed).
     * 
     * @param direction The direction to be set as inactive.
     */
    void setDirectionInactive(Input direction);

    /**
     * Get all buttons, stored as an array of booleans, where true is pressed, and
     * false is not pressed.
     * 
     * @return A boolean array representing the buttons.
     */
    boolean[] getButtons();

    /**
     * Get all buttons, stored as an integer, where a bit with the value of 0 is
     * pressed, and a bit with a value of 1 is not pressed.
     * 
     * @return An integer representing the buttons.
     */
    int getButtonsAsInt();

    /**
     * Get all directions, stored as an array of booleans, where true is pressed,
     * and false is not pressed.
     * 
     * @return A boolean array representing the dicections.
     */
    boolean[] getDirections();

    /**
     * Get all directions, stored as an integer, where a bit with the value of 0 is
     * pressed, and a bit with a value of 1 is not pressed.
     * 
     * @return An integer representing the buttons.
     */
    int getDirectionsAsInt();
}
