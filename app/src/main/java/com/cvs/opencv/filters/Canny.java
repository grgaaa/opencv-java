package com.cvs.opencv.filters;

import org.apache.commons.lang3.math.NumberUtils;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.DefaultFormatter;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
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
        this.L2gradient = l2gradient;
    }

    public static ImageFilter getDefault() {
        return new Canny(5, 15, 3, true);
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

        DefaultFormatter formatter = new DefaultFormatter();
        formatter.setCommitsOnValidEdit(true);

        panel.add(new JLabel("threshold1"));
        final JFormattedTextField threshold1Input = new JFormattedTextField(formatter);
        threshold1Input.setColumns(3);
        threshold1Input.setText(""+threshold1);
        threshold1Input.addPropertyChangeListener(new SettingsChangeListener(SettingsChangeListener.THRESHOLD_1));
        panel.add(threshold1Input);

        panel.add(new JLabel("threshold2"));
        final JFormattedTextField threshold2Input = new JFormattedTextField(formatter);
        threshold2Input.setColumns(3);
        threshold2Input.setText(""+threshold2);
        threshold2Input.addPropertyChangeListener(new SettingsChangeListener(SettingsChangeListener.THRESHOLD_2));
        panel.add(threshold2Input);

        panel.add(new JLabel("aperture size"));
        final JFormattedTextField apertureSizeInput = new JFormattedTextField(formatter);
        apertureSizeInput.setColumns(2);
        apertureSizeInput.setText(""+apertureSize);
        apertureSizeInput.addPropertyChangeListener(new SettingsChangeListener(SettingsChangeListener.APERTURE_SIZE));
        panel.add(apertureSizeInput);

        final JCheckBox L2gradientCheckbox = new JCheckBox("L2gradient", L2gradient);
        L2gradientCheckbox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                L2gradient = L2gradientCheckbox.isSelected();
            }
        });
        panel.add(L2gradientCheckbox);

        return panel;
    }

    private class SettingsChangeListener implements PropertyChangeListener {
        static final String THRESHOLD_1 = "thr1";
        static final String THRESHOLD_2 = "thr2";
        static final String APERTURE_SIZE = "aps";
        static final String L2_GRADIENT = "l2g";

        private String field;
        public SettingsChangeListener(String field) {
            this.field = field;
        }

        public void propertyChange(PropertyChangeEvent evt) {
            Object source = evt.getSource();

            if (THRESHOLD_1.equals(field)) {
                JFormattedTextField threshold1Input = (JFormattedTextField)source;
                if (NumberUtils.isNumber(threshold1Input.getText())) {
                    threshold1 = Double.parseDouble(threshold1Input.getText());
                }
            } else if (THRESHOLD_2.equals(field)) {
                JFormattedTextField threshold2Input = (JFormattedTextField)source;
                if (NumberUtils.isNumber(threshold2Input.getText())) {
                    threshold2 = Double.parseDouble(threshold2Input.getText());
                }
            } else if (APERTURE_SIZE.equals(field)) {
                JFormattedTextField apertureSizeInput = (JFormattedTextField)source;
                if (NumberUtils.isDigits(apertureSizeInput.getText())) {
                    apertureSize = Integer.parseInt(apertureSizeInput.getText());
                }
            } else if (L2_GRADIENT.equals(field)) {
                JCheckBox L2gradientCheckbox = (JCheckBox)source;
                L2gradient = L2gradientCheckbox.isSelected();
            }
        }
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
