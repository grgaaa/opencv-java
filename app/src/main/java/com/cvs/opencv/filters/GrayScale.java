package com.cvs.opencv.filters;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;

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
        return null;
    }

    @Override
    public URL docsUrl() {
        try {
            return new URL("http://www.docs.opencv.org/3.1.0/d7/d1b/group__imgproc__misc.html#ga397ae87e1288a81d2363b61574eb8cab");

        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        return this.label().equals(((GrayScale)obj).label());
    }
}
