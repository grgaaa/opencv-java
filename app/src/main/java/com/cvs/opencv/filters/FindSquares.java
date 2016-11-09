package com.cvs.opencv.filters;

import com.cvs.opencv.OpenCV;
import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by gregor.horvat on 29. 07. 2016.
 */
// TODO
public class FindSquares implements ImageFilter {


    public Mat applyFilter(Mat image) {

        Point[] largestSquare = findLargestSquare(image);

        if (largestSquare == null) {
            return drawNoSquareDetected(image);
        }
        return drawSquare(image, largestSquare);
    }

    public static FindSquares getDefault() {
        return new FindSquares();
    }

    public String label() {
        return "Find largest square";
    }

    public Component getSettingsView() {
        return null;
    }

    private Mat drawSquare(Mat image, Point[] squarePoints) {
        BufferedImage bufferedImage = OpenCV.matToImage(image);

        Graphics2D graphics = bufferedImage.createGraphics();
        graphics.setColor(Color.RED);
        BasicStroke stroke = new BasicStroke(10);
        graphics.setStroke(stroke);

        Polygon polygon = new Polygon();
        for (Point squarePoint : squarePoints) {
            polygon.addPoint((int)squarePoint.x, (int)squarePoint.y);
        }
        graphics.drawPolygon(polygon);
        graphics.dispose();

        return OpenCV.imageToMat(bufferedImage);
    }

    private Mat drawNoSquareDetected(Mat image) {
  /*      Mat bgr = new Mat();
        image.convertTo(bgr, BufferedImage.TYPE_4BYTE_ABGR);*/
        Core.line(image,
                new Point(0,0), new Point(image.width(), image.height()),
                new Scalar(0,0,255), 5);
        return image;
    }

    // from c++ implementation in https://github.com/jhansireddy/AndroidScannerDemo
    private Point[] findLargestSquare(Mat image) {

        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        Imgproc.findContours(image, contours, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);

        // Test contours
        MatOfPoint2f approx = new MatOfPoint2f();
        List<MatOfPoint> squares = new ArrayList<MatOfPoint>();

        for (MatOfPoint contour : contours) {
            // approximate contour with accuracy proportional
            // to the contour perimeter
            Imgproc.approxPolyDP(new MatOfPoint2f(contour.toArray()),
                    approx,
                    Imgproc.arcLength(new MatOfPoint2f(contour.toArray()), true)*0.02,
                    true);

            org.opencv.core.Point[] points = approx.toArray();
            // Note: absolute value of an area is used because
            // area may be positive or negative - in accordance with the
            // contour orientation
            if (points.length == 4
                    && Math.abs(Imgproc.contourArea(approx)) > 1000
                    && Imgproc.isContourConvex(new MatOfPoint(points))) {

                double maxCosine = 0.;

                for (int i = 2; i < 5; i++) {
                    double cosine = Math.abs(angle(points[i%4], points[i-2], points[i-1]));
                    maxCosine = Math.max(maxCosine, cosine);
                }

                if (maxCosine < 0.3) {
                    squares.add(contour);
                }
            }
        }
        if (squares.isEmpty()) {
            return null;
        }

        MatOfPoint largestSquare = null;
        double largestSquareContourArea = 0;

        for (MatOfPoint square : squares) {
            if (largestSquare == null) {
                largestSquare = square;
                largestSquareContourArea = Imgproc.contourArea(square, false);
            } else {
                double contourArea = Imgproc.contourArea(square, false);
                if (contourArea > largestSquareContourArea) {
                    largestSquare = square;
                }
            }
        }
        return largestSquare.toArray();
    }

    private double angle(Point pt0, Point pt1, Point pt2) {
        double dx1 = pt1.x - pt0.x;
        double dy1 = pt1.y - pt0.y;
        double dx2 = pt2.x - pt0.x;
        double dy2 = pt2.y - pt0.y;
        return (dx1*dx2 + dy1*dy2)/Math.sqrt((dx1*dx1 + dy1*dy1)*(dx2*dx2 + dy2*dy2) + 1e-10);
    }
}
