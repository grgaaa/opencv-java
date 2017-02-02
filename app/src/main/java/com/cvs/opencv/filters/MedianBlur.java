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
 * Created by gregor.horvat on 27. 07. 2016.
 */
public class MedianBlur implements ImageFilter {

    // The kernel size will determine how many pixels to sample during the convolution
    int kernelSize;

    public MedianBlur(int kernelSize) {
        this.kernelSize = kernelSize;
    }

    public static ImageFilter getDefault() {
        return new MedianBlur(9);
    }

    public Mat applyFilter(Mat image) {
        Mat dst = new Mat();
        Imgproc.medianBlur(image, dst, kernelSize);
        return dst;
    }

    public String label() {
        return "Median blur";
    }

    public Component getSettingsView() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.add(new JLabel("kernel size"));

        DefaultFormatter formatter = new DefaultFormatter();
        formatter.setCommitsOnValidEdit(true);

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
        return panel;
    }

    @Override
    public String filterDocs() {
        return Utils.toHtmlUrl("http://www.docs.opencv.org/3.1.0/d4/d86/group__imgproc__filter.html#ga564869aa33e58769b4469101aac458f9",
            "documentation");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MedianBlur medianBlur = (MedianBlur) o;

        return kernelSize == medianBlur.kernelSize;

    }

    @Override
    public int hashCode() {
        return kernelSize;
    }
}
