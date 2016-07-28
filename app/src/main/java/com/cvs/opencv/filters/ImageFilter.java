package com.cvs.opencv.filters;

import org.opencv.core.Mat;

/**
 * Created by gregor.horvat on 26. 07. 2016.
 */
public interface ImageFilter {

    Mat applyFilter(Mat image);

    String label();
}
