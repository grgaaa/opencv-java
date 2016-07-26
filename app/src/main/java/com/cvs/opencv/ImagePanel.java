package com.cvs.opencv;

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

    private File imgFile;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (imgFile == null) { return; }
        try {
            BufferedImage image = ImageIO.read(imgFile);
            Image scaledImage = image.getScaledInstance(this.getWidth(), this.getHeight(), Image.SCALE_SMOOTH);
            g.drawImage(scaledImage, 0, 0, null);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public File getImgFile() {
        return imgFile;
    }

    public void setImgFile(File imgFile) {
        this.imgFile = imgFile;
    }
}
