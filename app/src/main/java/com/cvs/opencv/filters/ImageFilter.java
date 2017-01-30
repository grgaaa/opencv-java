package com.cvs.opencv.filters;

import org.opencv.core.Mat;

import java.awt.*;
import java.net.URL;

/**
 * Created by gregor.horvat on 26. 07. 2016.
 */
public interface ImageFilter {

    Mat applyFilter(Mat image);

    String label();

    Component getSettingsView();

    URL docsUrl();
}
