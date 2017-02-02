package com.cvs.opencv.filters;

import org.opencv.core.Mat;

import java.awt.*;

/**
 * Created by gregor.horvat on 26. 07. 2016.
 */
public interface ImageFilter {

    Mat applyFilter(Mat image);

    String label();

    Component getSettingsView();

    String filterDocs();
}
