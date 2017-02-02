package com.cvs.opencv.filters;

import com.cvs.opencv.Utils;
import org.apache.commons.lang3.math.NumberUtils;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;

import javax.swing.*;
import javax.swing.text.DefaultFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Created by gregor.horvat on 30. 01. 2017.
 */
public class MorphologyEx implements ImageFilter {

    private MorphologyType morphologyType = MorphologyType.values()[0];
    private int kernelSize = 3;
    private int anchorX = -1;
    private int anchorY = -1;
    private int iterations = 1;
    // TODO
//    private Constants.BorderType borderType;
//    private int borderValue; // Border value in case of a constant border. The default value has a special meaning.

    private enum MorphologyType {
        MORPH_ERODE(Imgproc.MORPH_ERODE),
        MORPH_DILATE(Imgproc.MORPH_DILATE),
        MORPH_OPEN(Imgproc.MORPH_OPEN),
        MORPH_CLOSE(Imgproc.MORPH_CLOSE),
        MORPH_GRADIENT(Imgproc.MORPH_GRADIENT),
        MORPH_TOPHAT(Imgproc.MORPH_TOPHAT),
        MORPH_BLACKHAT(Imgproc.MORPH_BLACKHAT),
        MORPH_HITMISS(Imgproc.MORPH_HITMISS);

        int type;
        MorphologyType(int type) {
            this.type = type;
        }

        public static String[] names() {
            MorphologyEx.MorphologyType[] values = MorphologyEx.MorphologyType.values();
            String[] names = new String[values.length];
            for (int i = 0; i < values.length; i++) {
                names[i] = values[i].name();
            }
            return names;
        }
    }

    public static MorphologyEx getDefault() {
        return new MorphologyEx();
    }

    @Override
    public Mat applyFilter(Mat image) {
        Mat dst = new Mat();
        Mat kernel;
        if (kernelSize == 0 || kernelSize == 3) {
            kernel = new Mat(); //if element=Mat() , a 3 x 3 rectangular structuring element is used
        } else {
            kernel = new Mat(kernelSize, kernelSize, image.type());
        }
        Imgproc.morphologyEx(image, dst, morphologyType.type, kernel,
                new Point(anchorX, anchorY), iterations);
        return dst;
    }

    @Override
    public String label() {
        return "MorphologyEx";
    }

    public Component getSettingsView() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        DefaultFormatter formatter = new DefaultFormatter();
        formatter.setCommitsOnValidEdit(true);

        panel.add(new JLabel("kernelSize"));
        final JFormattedTextField kernelInput = new JFormattedTextField(formatter);
        kernelInput.setColumns(3);
        kernelInput.setText(""+kernelSize);
        kernelInput.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                if (NumberUtils.isDigits(kernelInput.getText())) {
                    kernelSize = Integer.parseInt(kernelInput.getText());
                }
            }
        });
        panel.add(kernelInput);

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

        panel.add(new JLabel("morphType"));
        final JComboBox morphTypes = new JComboBox(MorphologyEx.MorphologyType.names());
        morphTypes.setSelectedIndex(morphologyType.ordinal());
        morphTypes.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                morphologyType = MorphologyEx.MorphologyType.values()[morphTypes.getSelectedIndex()];
            }
        });
        panel.add(morphTypes);

        return panel;
    }

    @Override
    public String filterDocs() {
        return Utils.toHtmlUrl("http://www.docs.opencv.org/3.1.0/d4/d86/group__imgproc__filter.html#ga67493776e3ad1a3df63883829375201f",
                "documentation");
    }
}
