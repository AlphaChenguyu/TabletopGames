package gui;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JComponent {
    Image background;
    float opacity = 0.3f;

    public void setBackground(Image background) {
        this.background = background;
    }

    public void setOpacity(float opacity) {
        this.opacity = opacity;
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (background != null) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setComposite(AlphaComposite.SrcOver.derive(opacity));

            Rectangle r = this.getBounds();
            int h = r.height;
            int w = r.width;

            int picW = background.getWidth(null);
            int picH = background.getHeight(null);
            double scale = w*1.0/picW;
            double s2 = h*1.0/picH;
            if (s2 > scale) scale = s2;
            g2d.drawImage(background, 0, 0, (int)(picW * scale), (int)(picH * scale), null);
            g2d.dispose();
        }
    }
}
