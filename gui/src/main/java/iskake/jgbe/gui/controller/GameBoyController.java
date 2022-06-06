// TODO: Rework using LWJGL
/* import iskake.jgbe.core.gb.joypad.IJoypad;
import iskake.jgbe.core.gb.joypad.Input;
import iskake.jgbe.gui.GameBoyGUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GameBoyController implements KeyListener, ActionListener {
    // TODO: correctly handle input

    private IJoypad joypad;
    private GameBoyGUI m;

    public GameBoyController(GameBoyGUI gui, GameBoyGUI view, IJoypad joypad) {
        this.m = gui;
        gui.addKeyListener(this);
        this.joypad = joypad;
    }

    private void setDirection(KeyEvent e, int desired, Input direction) {
        if (e.getKeyCode() == desired) {
            joypad.setDirectionActive(direction);
        } else {
            joypad.setDirectionInactive(direction);
        }
    }

    private void setButton(KeyEvent e, int desired, Input button) {
        if (e.getKeyCode() == desired) {
            joypad.setButtonActive(button);
        } else {
            joypad.setButtonInactive(button);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // ...
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // TODO
        setDirection(e, KeyEvent.VK_UP, Input.DPAD_UP);
        setDirection(e, KeyEvent.VK_DOWN, Input.DPAD_DOWN);
        setDirection(e, KeyEvent.VK_LEFT, Input.DPAD_LEFT);
        setDirection(e, KeyEvent.VK_RIGHT, Input.DPAD_RIGHT);
        setButton(e, KeyEvent.VK_Z, Input.BUTTON_A);
        setButton(e, KeyEvent.VK_X, Input.BUTTON_B);
        setButton(e, KeyEvent.VK_ENTER, Input.BUTTON_START);
        setButton(e, KeyEvent.VK_SHIFT, Input.BUTTON_SELECT);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // ...
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // ...
    }
    
}
 */