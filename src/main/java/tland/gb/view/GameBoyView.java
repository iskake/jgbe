package tland.gb.view;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.*;
import java.awt.image.BufferedImage;
import java.io.IOError;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.ImageIcon;

public class GameBoyView extends JLabel {
    {
        // Run before constructor.
        this.setFocusable(true);
    }

    private GameBoyViewable viewable;

    public GameBoyView(GameBoyViewable viewable) {
        this.viewable = viewable;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        if (viewable.canGetFrame()) {
            int[] b = viewable.getFrameInt();
            int w = 160;
            int h = 144;
            BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
            img.setRGB(0, 0, w, h, b, 0, 144);
            ImageIcon im = new ImageIcon(img);
            this.setIcon(im);
        }
    }
}
