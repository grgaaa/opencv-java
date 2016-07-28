package com.cvs.opencv.filters;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

/**
 * Created by gregor.horvat on 27. 07. 2016.
 */
public class Blur implements ImageFilter {

    // The kernel size will determine how many pixels to sample during the convolution
    int kernelSize;

    public Blur(int kernelSize) {
        this.kernelSize = kernelSize;
    }

    public static ImageFilter getDefault() {
        return new Blur(3);
    }

    public Mat applyFilter(Mat image) {
        Mat dst = new Mat();
        Imgproc.blur(image, dst, new Size(kernelSize, kernelSize));
        return dst;
    }

    public String label() {
        return "Blur";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Blur blur = (Blur) o;

        return kernelSize == blur.kernelSize;

    }

    @Override
    public int hashCode() {
        return kernelSize;
    }
}
