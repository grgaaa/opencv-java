package com.cvs.opencv.filters;

import org.apache.commons.lang3.math.NumberUtils;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import javax.swing.*;
import javax.swing.text.DefaultFormatter;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Created by gregor.horvat on 29. 07. 2016.
 */
public class Dilate implements ImageFilter {

    private int kernelSize;
// TODO
//    private org.opencv.core.Point anchor = new org.opencv.core.Point(-1,-1);
//    private int iterations;
//    private int borderType;
//    private Scalar borderValue;

    public Dilate(int kernelSize) {
        this.kernelSize = kernelSize;
    }

    public static ImageFilter getDefault() {
        return new Dilate(3);
    }

    public Mat applyFilter(Mat image) {
        Mat dst = new Mat();
        Mat kernel;
        if (kernelSize == 0 || kernelSize == 3) {
            kernel = new Mat(); //if element=Mat() , a 3 x 3 rectangular structuring element is used
        } else {
            kernel = new Mat(kernelSize, kernelSize, image.type());
        }
        Imgproc.dilate(image, dst, kernel);
        return dst;
    }

    public String label() {
        return "Dilate";
    }

    public Component getSettingsView() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        DefaultFormatter formatter = new DefaultFormatter();
        formatter.setCommitsOnValidEdit(true);

        panel.add(new JLabel("kernel size"));
        final JFormattedTextField kernelSizeInput = new JFormattedTextField(formatter);
        kernelSizeInput.setColumns(2);
        kernelSizeInput.setText(""+kernelSize);
        kernelSizeInput.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                if (NumberUtils.isDigits(kernelSizeInput.getText())) {
                    kernelSize = Integer.parseInt(kernelSizeInput.getText());
                }
            }
        });
        panel.add(kernelSizeInput);

        JLabel anchorLabel = new JLabel("anchor");
        anchorLabel.setEnabled(false);
        JLabel iterations = new JLabel("iterations");
        iterations.setEnabled(false);
        JLabel borderType = new JLabel("borderType");
        borderType.setEnabled(false);
        JLabel borderValue = new JLabel("borderValue");
        borderValue.setEnabled(false);

        panel.add(anchorLabel);
        panel.add(iterations);
        panel.add(borderType);
        panel.add(borderValue);

        return panel;
    }

    @Override
    public URL docsUrl() {
        try {
            return new URL("http://www.docs.opencv.org/3.1.0/d4/d86/group__imgproc__filter.html#ga4ff0f3318642c4f469d0e11f242f3b6c");

        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
