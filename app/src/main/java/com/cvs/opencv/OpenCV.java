package com.cvs.opencv;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

/**
 * Created by gregor.horvat on 26. 07. 2016.
 */
public class OpenCV {

    public static Mat imageToMat(BufferedImage bufferedImage) {
        return imageToMat(bufferedImage, CvType.CV_8UC3);
    }

    public static Mat imageToMat(BufferedImage bufferedImage, int cvType) {
        byte[] pixels = ((DataBufferByte) bufferedImage.getRaster().getDataBuffer()).getData();
        Mat matImage = new Mat(bufferedImage.getHeight(), bufferedImage.getWidth(), cvType);
        matImage.put(0, 0, pixels);
        return matImage;
    }

    public static BufferedImage matToImage(Mat mat) {
        int channels = mat.channels();
        if (channels == 1) {
            return matToImage(mat, BufferedImage.TYPE_BYTE_GRAY);
        }
        return matToImage(mat, BufferedImage.TYPE_3BYTE_BGR);
    }

    public static BufferedImage matToImage(Mat mat, int bufferedImageType) {
        // Create an empty image in matching format
        BufferedImage bufferedImage = new BufferedImage(mat.width(), mat.height(), bufferedImageType);

        // Get the BufferedImage's backing array and copy the pixels directly into it
        byte[] data = ((DataBufferByte) bufferedImage.getRaster().getDataBuffer()).getData();
        mat.get(0, 0, data);

        return bufferedImage;
    }

    //http://stackoverflow.com/questions/13605248/java-converting-image-to-bufferedimage
    public static BufferedImage toBufferedImage(Image img) {
        if (img instanceof BufferedImage) {
            return (BufferedImage) img;
        }

        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_3BYTE_BGR);

        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        // Return the buffered image
        return bimage;
    }
}
