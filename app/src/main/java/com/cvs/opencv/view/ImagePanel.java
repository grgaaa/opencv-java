package com.cvs.opencv.view;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by gregor.horvat on 26. 07. 2016.
 */
public class ImagePanel extends JPanel {

    private BufferedImage bufferedImage;
    private File imageSourceFile;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (bufferedImage == null)
            return;

        Image scaledImage = bufferedImage;
        if (bufferedImage.getWidth() != this.getWidth() || bufferedImage.getHeight() != this.getHeight()) {
            scaledImage = bufferedImage.getScaledInstance(this.getWidth(), this.getHeight(), Image.SCALE_SMOOTH);
        }
        g.drawImage(scaledImage, 0, 0, null);
    }

    public BufferedImage getBufferedImage() {
        return bufferedImage;
    }

    public void setBufferedImage(BufferedImage bufferedImage) {
        this.bufferedImage = bufferedImage;
    }

    public File getImageSourceFile() {
        return imageSourceFile;
    }

    public void setImageSourceFile(File imageSourceFile) {
        this.imageSourceFile = imageSourceFile;
    }

    public void reloadSourceImage() {
        bufferedImage = null;

        if (imageSourceFile != null) {
            try {
                bufferedImage = ImageIO.read(imageSourceFile);
                repaint();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        repaint();
    }
}
