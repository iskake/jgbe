// TODO: Replace with LWJGL

package iskake.jgbe.gui.view;

import javax.swing.*;

import iskake.jgbe.core.gb.GameBoyDisplayable;

import java.awt.*;
import java.awt.image.BufferedImage;

public class GameBoyView extends JLabel {
    {
        // Run before constructor.
        this.setFocusable(true);
    }

    private GameBoyDisplayable viewable;

    public GameBoyView(GameBoyDisplayable viewable) {
        this.viewable = viewable;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        if (viewable.canGetFrame()) {
            int[] b = viewable.getFrameMapped();
            int w = 160;
            int h = 144;
            BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
            img.setRGB(0, 0, w, h, b, 0, 144);
            ImageIcon im = new ImageIcon(img);
            this.setIcon(im);
        }
    }
}
