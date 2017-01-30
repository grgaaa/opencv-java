package com.cvs.opencv;

import com.cvs.opencv.filters.*;
import com.cvs.opencv.view.FilterAddView;
import com.cvs.opencv.view.FilterListModel;
import com.cvs.opencv.view.ImagePanel;
import com.cvs.opencv.view.MouseClickListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.opencv.core.Core;
import org.opencv.core.Mat;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.basic.BasicArrowButton;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private JButton retryBtn;
    private JButton saveBtn;
    private JPanel filtersPanel;
    private JButton upArrowBtn;
    private JPanel filterActionPanel;
    private JButton downArrowBtn;
    private JButton deleteFilterBtn;

    private FilterListModel filterListModel = new FilterListModel();

    public static final File DEFAULT_WORKING_DIR = new File("C:\\Users\\gregor.horvat.CVSMOBILE\\Desktop\\opencv_test");

    public static void main(String[] args) {
        final MainWindow mainWindow = new MainWindow();
        final JFrame jFrame = new JFrame("OpenCV");

        mainWindow.filterList.setModel(mainWindow.filterListModel);
        mainWindow.filterList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        final ListSelectionModel selectionModel = mainWindow.filterList.getSelectionModel();

        selectionModel.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                int index = mainWindow.filterList.getSelectedIndex();
                if (index >= 0) {
                    mainWindow.showFilterSettings(mainWindow.filterListModel.getFilters().get(index));
                } else {
                    mainWindow.clearFilterSettings();
                }
            }
        });
        mainWindow.deleteFilterBtn.addMouseListener(new MouseClickListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedIndex = mainWindow.filterList.getSelectedIndex();
                int previousIndex = Math.max(0, selectedIndex-1);

                mainWindow.filterList.setSelectedIndex(previousIndex);
                ((FilterListModel)mainWindow.filterList.getModel()).removeFilter(selectedIndex);
            }
        });
        mainWindow.upArrowBtn.addMouseListener(new FilterMoveListener(mainWindow, FilterMoveListener.Direction.UP));
        mainWindow.downArrowBtn.addMouseListener(new FilterMoveListener(mainWindow, FilterMoveListener.Direction.DOWN));

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
        mainWindow.retryBtn.addMouseListener(new ApplyFiltersClickListener(mainWindow) {
            @Override
            public void mouseClicked(MouseEvent e) {
                mainWindow.img1.reloadSourceImage();
                mainWindow.img2.reloadSourceImage();
                mainWindow.img3.reloadSourceImage();

                super.mouseClicked(e);
            }
        });
        mainWindow.saveBtn.addMouseListener(new MouseClickListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                fileChooser.setSelectedFile(DEFAULT_WORKING_DIR);
                FileNameExtensionFilter filter = new FileNameExtensionFilter("opencv-java config", "config");
                fileChooser.setFileFilter(filter);

                if (DEFAULT_WORKING_DIR.exists()) {
                    fileChooser.setCurrentDirectory(DEFAULT_WORKING_DIR);
                    String selectedFileName = "opencv-java.config";

                    String[] filesList = DEFAULT_WORKING_DIR.list();
                    Integer fileCopyNumber = null;

                    if (filesList != null) {
                        for (String fileName : filesList) {

                            Matcher matcher = Pattern.compile("opencv-java(\\((\\d+)\\))?.config").matcher(fileName);

                            if (matcher.find()) {
                                String copyVersion = matcher.group(2);
                                if (copyVersion != null) {
                                    Integer copyVerInt = Integer.valueOf(copyVersion);
                                    if (fileCopyNumber == null || fileCopyNumber < copyVerInt) {
                                        selectedFileName = fileName.replace(Character.forDigit(copyVerInt, Character.MAX_RADIX),
                                                                            Character.forDigit(copyVerInt+1, Character.MAX_RADIX));
                                        fileCopyNumber = copyVerInt;
                                    }
                                } else if (fileCopyNumber == null){
                                    selectedFileName = "opencv-java(1).config";
                                }
                            }
                        }
                    }
                    File selectedFile = new File(DEFAULT_WORKING_DIR, selectedFileName);
                    fileChooser.setSelectedFile(selectedFile);
                }

                if (fileChooser.showSaveDialog(mainWindow.mainPanel) == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    try {
                        if (selectedFile.exists() || selectedFile.createNewFile()) {
                            try (BufferedWriter writer = new BufferedWriter(new FileWriter(selectedFile))) {
                                Gson gson = new GsonBuilder().create();
                                List<ImageFilter> filters = mainWindow.filterListModel.getFilters();

                                for (ImageFilter imageFilter : filters) {
                                    String filterJson = gson.toJson(imageFilter);
                                    writer.write(imageFilter.label());
                                    writer.write(": ");
                                    writer.write(filterJson);
                                    writer.newLine();
                                }
                                writer.flush();
                            }
                        }
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                }
            }
        });

        mainWindow.filtersPanel.add(new FilterAddView(GrayScale.getDefault(), mainWindow), filterConstraints(0));
        mainWindow.filtersPanel.add(new FilterAddView(MedianBlur.getDefault(), mainWindow), filterConstraints(1));
        mainWindow.filtersPanel.add(new FilterAddView(Canny.getDefault(), mainWindow), filterConstraints(2));
        mainWindow.filtersPanel.add(new FilterAddView(Dilate.getDefault(), mainWindow), filterConstraints(3));
        mainWindow.filtersPanel.add(new FilterAddView(Threshold.getDefault(), mainWindow), filterConstraints(4));
        mainWindow.filtersPanel.add(new FilterAddView(FindSquares.getDefault(), mainWindow), filterConstraints(5));

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
        clearFilterSettings();
        if (filter != null) {
            Component settingsView = filter.getSettingsView();

            if (settingsView == null) {
                JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                panel.add(new JLabel("No editable options."));
                settingsView = panel;
            }
            filterSettings.add(settingsView);
        }
        filterSettings.revalidate();
        filterSettings.repaint();
    }

    private void clearFilterSettings() {
        filterSettings.removeAll();
    }

    private void createUIComponents() {
        upArrowBtn = new BasicArrowButton(BasicArrowButton.NORTH);
        downArrowBtn = new BasicArrowButton(BasicArrowButton.SOUTH);
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

    private static class FilterMoveListener extends MouseClickListener {
        enum Direction {UP, DOWN}
        private Direction direction;
        private MainWindow mainWindow;

        public FilterMoveListener(MainWindow mainWindow, Direction direction) {
            this.mainWindow = mainWindow;
            this.direction = direction;
        }

        public void mouseClicked(MouseEvent e) {
            int fromIndex = mainWindow.filterList.getSelectedIndex();
            int toIndex = Direction.UP == direction ? fromIndex-1 : fromIndex+1;

            int listSize = mainWindow.filterList.getModel().getSize();
            if (toIndex >= 0 && toIndex < listSize) {
                ((FilterListModel)mainWindow.filterList.getModel()).switchFilter(fromIndex, toIndex);
                mainWindow.filterList.setSelectedIndex(toIndex);
            }
        }
    }

    private static class ImageSelectListener extends MouseClickListener {
        private JFrame jFrame;
        private ImagePanel imgPanel;

        private ImageSelectListener(JFrame jFrame, ImagePanel imgPanel) {
            this.jFrame = jFrame;
            this.imgPanel = imgPanel;
        }

        public void mouseClicked(MouseEvent e) {
            JFileChooser chooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG & PNG Images", "jpg", "png");
            chooser.setFileFilter(filter);
            if (DEFAULT_WORKING_DIR.exists()) {
                chooser.setCurrentDirectory(DEFAULT_WORKING_DIR);
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
