package com.cvs.opencv.filters;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.imgproc.Imgproc;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by gregor.horvat on 29. 07. 2016.
 */
// TODO
public class FindSquares implements ImageFilter {


    public Mat applyFilter(Mat image) {
        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        Imgproc.findContours(image, contours, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
        return null;
    }

    public String label() {
        return null;
    }

    public Component getSettingsView() {
        return null;
    }
}
