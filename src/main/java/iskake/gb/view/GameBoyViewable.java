package iskake.gb.view;

import javax.swing.ImageIcon;

public interface GameBoyViewable {
    byte[] getFrame();
    int[] getFrameInt();
    boolean canGetFrame();
}
