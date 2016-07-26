package com.cvs.opencv;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.DimensionUIResource;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created by gregor.horvat on 26. 07. 2016.
 */
public class MainWindow {
    private JPanel mainPanel;
    private JPanel previewPanel;
    private JPanel toolbarPanel;
    private JRadioButton radioButton1;
    private JRadioButton radioButton2;
    private JRadioButton radioButton3;
    private JPanel img1;
    private JPanel img2;
    private JPanel img3;

    public static void main(String[] args) {
        MainWindow mainWindow = new MainWindow();
        final JFrame jFrame = new JFrame("OpenCV");

        mainWindow.img1.setBackground(Color.CYAN);
        mainWindow.img1.addMouseListener(new ImageSelectListener(jFrame));
        mainWindow.img1.setSize(200, 200);
        mainWindow.img2.setBackground(Color.DARK_GRAY);
        mainWindow.img3.setBackground(Color.DARK_GRAY);

        jFrame.setContentPane(mainWindow.mainPanel);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jFrame.setSize(new DimensionUIResource(800, 800));
        jFrame.setVisible(true);
    }

    private static class ImageSelectListener implements MouseListener {
        private JFrame jFrame;

        public ImageSelectListener(JFrame jFrame) {
            this.jFrame = jFrame;
        }

        public void mouseClicked(MouseEvent e) {
            JFileChooser chooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG & GIF Images", "jpg", "gif");
            chooser.setFileFilter(filter);

            int returnVal = chooser.showOpenDialog(jFrame);
            if(returnVal == JFileChooser.APPROVE_OPTION) {
                System.out.println("You chose to open this file: " + chooser.getSelectedFile().getAbsolutePath());
            }
        }
        public void mousePressed(MouseEvent e) {}
        public void mouseReleased(MouseEvent e) {}
        public void mouseEntered(MouseEvent e) {}
        public void mouseExited(MouseEvent e) {}
    }
}
