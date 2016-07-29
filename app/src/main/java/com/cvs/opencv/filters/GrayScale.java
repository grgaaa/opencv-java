package com.cvs.opencv.filters;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import javax.swing.*;
import java.awt.*;

/**
 * Created by gregor.horvat on 27. 07. 2016.
 */
public class GrayScale implements ImageFilter {

    public static ImageFilter getDefault() {
        return new GrayScale();
    }

    public Mat applyFilter(Mat image) {
        Mat matDest = new Mat(image.rows(), image.cols(), CvType.CV_8UC1);
        Imgproc.cvtColor(image, matDest, Imgproc.COLOR_RGB2GRAY);
        return matDest;
    }

    public String label() {
        return "Gray scale";
    }

    public Component getSettingsView() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.add(new JLabel("No editable options."));
        return panel;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        return this.label().equals(((GrayScale)obj).label());
    }
}
