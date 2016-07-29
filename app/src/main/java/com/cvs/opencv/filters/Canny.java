package com.cvs.opencv.filters;

import org.apache.commons.lang3.math.NumberUtils;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Created by gregor.horvat on 27. 07. 2016.
 */
public class Canny implements ImageFilter {

    private double threshold1, threshold2;
    private int apertureSize;
    private boolean L2gradient;

    public Canny(double threshold1, double threshold2, int apertureSize, boolean l2gradient) {
        this.threshold1 = threshold1;
        this.threshold2 = threshold2;
        this.apertureSize = apertureSize;
        L2gradient = l2gradient;
    }

    public static ImageFilter getDefault() {
        return new Canny(10, 30, 3, false);
    }

    public Mat applyFilter(Mat image) {
        Mat detectedEdges = new Mat();
        Imgproc.Canny(image, detectedEdges, threshold1, threshold2, apertureSize, L2gradient);
        return detectedEdges;
    }

    public String label() {
        return "Canny";
    }

    public Component getSettingsView() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        panel.add(new JLabel("threshold1"));
        final JFormattedTextField threshold1Input = new JFormattedTextField(""+threshold1);
        threshold1Input.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                if (NumberUtils.isNumber(threshold1Input.getText())) {
                    threshold1 = Double.parseDouble(threshold1Input.getText());
                }
            }
        });
        panel.add(threshold1Input);

        panel.add(new JLabel("threshold2"));
        final JFormattedTextField threshold2Input = new JFormattedTextField(""+threshold2);
        threshold2Input.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                if (NumberUtils.isNumber(threshold2Input.getText())) {
                    threshold2 = Double.parseDouble(threshold2Input.getText());
                }
            }
        });
        panel.add(threshold2Input);

        panel.add(new JLabel("aperture size"));
        final JFormattedTextField apertureSizeInput = new JFormattedTextField(""+apertureSize);
        apertureSizeInput.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                if (NumberUtils.isDigits(apertureSizeInput.getText())) {
                    apertureSize = Integer.parseInt(apertureSizeInput.getText());
                }
            }
        });
        panel.add(apertureSizeInput);

        return panel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Canny canny = (Canny) o;

        if (Double.compare(canny.threshold1, threshold1) != 0) return false;
        if (Double.compare(canny.threshold2, threshold2) != 0) return false;
        if (apertureSize != canny.apertureSize) return false;
        return L2gradient == canny.L2gradient;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(threshold1);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(threshold2);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + apertureSize;
        result = 31 * result + (L2gradient ? 1 : 0);
        return result;
    }
}
