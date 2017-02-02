package com.cvs.opencv.filters;

import com.cvs.opencv.Utils;
import org.apache.commons.lang3.math.NumberUtils;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import javax.swing.*;
import javax.swing.text.DefaultFormatter;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Created by gregor.horvat on 30. 01. 2017.
 */
public class Erode implements ImageFilter {

    private int anchorX = -1;
    private int anchorY = -1;
    private int kernelSize = 3;
    private int iterations = 1;
// TODO
//    private int borderType;
//    private Scalar borderValue;


    public static ImageFilter getDefault() {
        return new Erode();
    }

    public Mat applyFilter(Mat image) {
        Mat dst = new Mat();
        Mat kernel;
        if (kernelSize == 0 || kernelSize == 3) {
            kernel = new Mat(); //if element=Mat() , a 3 x 3 rectangular structuring element is used
        } else {
            kernel = new Mat(kernelSize, kernelSize, image.type());
        }
        Imgproc.erode(image, dst, kernel);
        return dst;
    }

    public String label() {
        return "Erode";
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

        panel.add(new JLabel("anchorX"));
        final JFormattedTextField anchorXInput = new JFormattedTextField(formatter);
        anchorXInput.setColumns(2);
        anchorXInput.setText(""+anchorX);
        anchorXInput.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                if (NumberUtils.isDigits(anchorXInput.getText())) {
                    anchorX = Integer.parseInt(anchorXInput.getText());
                }
            }
        });
        panel.add(anchorXInput);

        panel.add(new JLabel("anchorY"));
        final JFormattedTextField anchorYInput = new JFormattedTextField(formatter);
        anchorYInput.setColumns(2);
        anchorYInput.setText(""+anchorY);
        anchorYInput.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                if (NumberUtils.isDigits(anchorYInput.getText())) {
                    anchorY = Integer.parseInt(anchorYInput.getText());
                }
            }
        });
        panel.add(anchorYInput);

        panel.add(new JLabel("iterations"));
        final JFormattedTextField iterationsInput = new JFormattedTextField(formatter);
        iterationsInput.setColumns(2);
        iterationsInput.setText(""+iterations);
        iterationsInput.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                if (NumberUtils.isDigits(iterationsInput.getText())) {
                    iterations = Integer.parseInt(iterationsInput.getText());
                }
            }
        });
        panel.add(iterationsInput);

        JLabel borderType = new JLabel("borderType");
        borderType.setEnabled(false);
        JLabel borderValue = new JLabel("borderValue");
        borderValue.setEnabled(false);

        panel.add(borderType);
        panel.add(borderValue);

        return panel;
    }

    @Override
    public String filterDocs() {
        return Utils.toHtmlUrl("http://www.docs.opencv.org/3.1.0/d4/d86/group__imgproc__filter.html#gaeb1e0c1033e3f6b891a25d0511362aeb",
                "documentation");
    }
}
