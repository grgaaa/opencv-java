package com.cvs.opencv;

import org.opencv.core.Core;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

/**
 * Created by gregor.horvat on 26. 07. 2016.
 */
public class MainWindow {

    static {
        System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
    }

    private JPanel mainPanel;
    private JPanel previewPanel;
    private JPanel toolbarPanel;
    private JRadioButton radioButton1;
    private JRadioButton radioButton2;
    private JRadioButton radioButton3;
    private ImagePanel img1;
    private ImagePanel img2;
    private ImagePanel img3;

    public static void main(String[] args) {
        MainWindow mainWindow = new MainWindow();
        final JFrame jFrame = new JFrame("OpenCV");

        mainWindow.previewPanel.setBackground(Color.CYAN);
        mainWindow.toolbarPanel.setBackground(Color.DARK_GRAY);

        mainWindow.img1.addMouseListener(new ImageSelectListener(jFrame, mainWindow.img1));
        mainWindow.img2.addMouseListener(new ImageSelectListener(jFrame, mainWindow.img2));
        mainWindow.img3.addMouseListener(new ImageSelectListener(jFrame, mainWindow.img3));

        jFrame.setContentPane(mainWindow.mainPanel);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jFrame.pack();
        jFrame.setLocationRelativeTo(null);
        jFrame.setVisible(true);
    }

    private static class ImageSelectListener implements MouseListener {
        private JFrame jFrame;
        private ImagePanel imgPanel;
        private final File defaultImagesPath = new File("C:\\Users\\gregor.horvat.CVSMOBILE\\Desktop\\opencv_test");

        private ImageSelectListener(JFrame jFrame, ImagePanel imgPanel) {
            this.jFrame = jFrame;
            this.imgPanel = imgPanel;
        }

        public void mouseClicked(MouseEvent e) {
            JFileChooser chooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG & PNG Images", "jpg", "png");
            chooser.setFileFilter(filter);
            if (defaultImagesPath.exists()) {
                chooser.setCurrentDirectory(defaultImagesPath);
            }

            int returnVal = chooser.showOpenDialog(jFrame);
            if(returnVal == JFileChooser.APPROVE_OPTION) {
                imgPanel.setImgFile(chooser.getSelectedFile());
                imgPanel.repaint();
            }
        }
        public void mousePressed(MouseEvent e) {}
        public void mouseReleased(MouseEvent e) {}
        public void mouseEntered(MouseEvent e) {}
        public void mouseExited(MouseEvent e) {}
    }
}
