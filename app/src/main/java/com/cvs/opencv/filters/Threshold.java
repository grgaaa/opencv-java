package com.cvs.opencv.filters;

import org.apache.commons.lang3.math.NumberUtils;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import javax.swing.*;
import javax.swing.text.DefaultFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Created by gregor.horvat on 1. 08. 2016.
 */
public class Threshold implements ImageFilter {

    private double thresh;
    private double maxVal;
    private ThresholdType thresholdType;

    private enum ThresholdType {
        THRESH_BINARY(Imgproc.THRESH_BINARY),
        THRESH_BINARY_INV(Imgproc.THRESH_BINARY_INV),
        THRESH_TRUNC(Imgproc.THRESH_TRUNC),
        THRESH_TOZERO(Imgproc.THRESH_TOZERO),
        THRESH_TOZERO_INV(Imgproc.THRESH_TOZERO_INV),
        THRESH_OTSU(Imgproc.THRESH_OTSU);

        int type;
        ThresholdType(int type) {
            this.type = type;
        }

        public static String[] names() {
            ThresholdType[] values = ThresholdType.values();
            String[] names = new String[values.length];
            for (int i = 0; i < values.length; i++) {
                names[i] = values[i].name();
            }
            return names;
        }
    }

    public Threshold(double thresh, double maxVal, ThresholdType thresholdType) {
        this.thresh = thresh;
        this.maxVal = maxVal;
        this.thresholdType = thresholdType;
    }

    public static ImageFilter getDefault() {
        return new Threshold(0, 255, ThresholdType.THRESH_OTSU);
    }

    public Mat applyFilter(Mat image) {
        Mat dest = new Mat();
        Imgproc.threshold(image, dest, thresh, maxVal, Imgproc.THRESH_OTSU);
        return dest;
    }

    public String label() {
        return "Threshold";
    }

    public Component getSettingsView() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        DefaultFormatter formatter = new DefaultFormatter();
        formatter.setCommitsOnValidEdit(true);

        panel.add(new JLabel("thresh"));
        final JFormattedTextField threshInput = new JFormattedTextField(formatter);
        threshInput.setColumns(3);
        threshInput.setText(""+thresh);
        threshInput.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                if (NumberUtils.isDigits(threshInput.getText())) {
                    thresh = Double.parseDouble(threshInput.getText());
                }
            }
        });
        panel.add(threshInput);

        panel.add(new JLabel("maxVal"));
        final JFormattedTextField maxValInput = new JFormattedTextField(formatter);
        maxValInput.setColumns(3);
        maxValInput.setText(""+maxVal);
        maxValInput.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                if (NumberUtils.isDigits(maxValInput.getText())) {
                    maxVal = Double.parseDouble(maxValInput.getText());
                }
            }
        });
        panel.add(maxValInput);

        panel.add(new JLabel("threshType"));
        final JComboBox threshTypes = new JComboBox(ThresholdType.names());
        threshTypes.setSelectedIndex(thresholdType.ordinal());
        threshTypes.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                thresholdType = ThresholdType.values()[threshTypes.getSelectedIndex()];
            }
        });
        panel.add(threshTypes);

        return panel;
    }
}
