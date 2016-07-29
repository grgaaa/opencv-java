package com.cvs.opencv;

import com.cvs.opencv.filters.*;
import com.cvs.opencv.view.FilterAddView;
import com.cvs.opencv.view.FilterListModel;
import com.cvs.opencv.view.ImagePanel;
import com.cvs.opencv.view.MouseClickListener;
import org.opencv.core.Core;
import org.opencv.core.Mat;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by gregor.horvat on 26. 07. 2016.
 */
public class MainWindow implements FilterAddView.OnAddFilterClickListener {

    static {
        System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
    }

    private JPanel mainPanel;
    private JPanel previewPanel;
    private JPanel toolbarPanel;
    private ImagePanel img1;
    private ImagePanel img2;
    private ImagePanel img3;
    private JList filterList;
    private JPanel filterSettings;
    private JButton applyBtn;
    private JButton clearBtn;
    private JPanel filtersPanel;

    private FilterListModel filterListModel = new FilterListModel();

    public static void main(String[] args) {
        final MainWindow mainWindow = new MainWindow();
        final JFrame jFrame = new JFrame("OpenCV");

        mainWindow.filterList.setModel(mainWindow.filterListModel);
        mainWindow.filterList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        final ListSelectionModel selectionModel = mainWindow.filterList.getSelectionModel();
        selectionModel.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                int index = selectionModel.getMinSelectionIndex();
                mainWindow.showFilterSettings(mainWindow.filterListModel.getFilters().get(index));
            }
        });

        mainWindow.previewPanel.setBackground(Color.CYAN);
        mainWindow.toolbarPanel.setBackground(Color.DARK_GRAY);

        mainWindow.img1.addMouseListener(new ImageSelectListener(jFrame, mainWindow.img1));
        mainWindow.img2.addMouseListener(new ImageSelectListener(jFrame, mainWindow.img2));
        mainWindow.img3.addMouseListener(new ImageSelectListener(jFrame, mainWindow.img3));

        mainWindow.applyBtn.addMouseListener(new ApplyFiltersClickListener(mainWindow));
        mainWindow.clearBtn.addMouseListener(new MouseClickListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mainWindow.filterListModel.clear();
                mainWindow.showFilterSettings(null);

                mainWindow.img1.reloadSourceImage();
                mainWindow.img2.reloadSourceImage();
                mainWindow.img3.reloadSourceImage();
            }
        });

        mainWindow.filtersPanel.add(new FilterAddView(GrayScale.getDefault(), mainWindow), filterConstraints(0));
        mainWindow.filtersPanel.add(new FilterAddView(Blur.getDefault(), mainWindow), filterConstraints(1));
        mainWindow.filtersPanel.add(new FilterAddView(Canny.getDefault(), mainWindow), filterConstraints(2));
        mainWindow.filtersPanel.add(new FilterAddView(Dilate.getDefault(), mainWindow), filterConstraints(3));

        jFrame.setContentPane(mainWindow.mainPanel);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jFrame.pack();
        jFrame.setLocationRelativeTo(null);
        jFrame.setVisible(true);
    }

    private static GridBagConstraints filterConstraints(int row) {
        return new GridBagConstraints(
                0, row, 1, 1, 1.0, 0.,
                GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                new Insets(0,0,0,0), 0,0);
    }

    public void onAddFilterClick(ImageFilter filter) {
        filterListModel.addFilter(filter);
        filterList.setSelectedIndex(filterListModel.getSize()-1);
    }

    private void showFilterSettings(ImageFilter filter) {
        filterSettings.removeAll();
        if (filter != null) {
            filterSettings.add(filter.getSettingsView());
        }
        filterSettings.revalidate();
        filterSettings.repaint();
    }

    private static class ApplyFiltersClickListener extends MouseClickListener {

        private MainWindow mainWindow;

        private ApplyFiltersClickListener(MainWindow mainWindow) {
            this.mainWindow = mainWindow;
        }

        public void mouseClicked(MouseEvent e) {
            List<ImageFilter> filters = mainWindow.filterListModel.getFilters();

            if (filters == null || filters.isEmpty())
                return;

            applyFilters(mainWindow.img1, filters);
            applyFilters(mainWindow.img2, filters);
            applyFilters(mainWindow.img3, filters);
        }

        private void applyFilters(final ImagePanel imagePanel, final List<ImageFilter> imageFilters) {
            if (imagePanel.getBufferedImage() == null)
                return;

            new Thread(new Runnable() {
                public void run() {
                    BufferedImage bufferedImage = imagePanel.getBufferedImage();
                    Mat imageMat = OpenCV.imageToMat(OpenCV.toBufferedImage(bufferedImage));

                    for (ImageFilter imageFilter : imageFilters) {
                        imageMat = imageFilter.applyFilter(imageMat);
                    }
                    imagePanel.setBufferedImage(OpenCV.matToImage(imageMat));
                    imagePanel.repaint();
                }
            }).start();
        }
    }

    private static class ImageSelectListener extends MouseClickListener {
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
                try {
                    File imageSourceFile = chooser.getSelectedFile();
                    imgPanel.setImageSourceFile(imageSourceFile);

                    BufferedImage image = ImageIO.read(imageSourceFile);
                    imgPanel.setBufferedImage(image);
                    imgPanel.repaint();

                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
}
