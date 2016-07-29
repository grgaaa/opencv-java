package com.cvs.opencv.view;

import com.cvs.opencv.MouseClickListener;
import com.cvs.opencv.filters.ImageFilter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * Created by gregor.horvat on 27. 07. 2016.
 */
public class FilterAddView extends JPanel {

    private ImageFilter filter;
    private OnAddFilterClickListener onAddFilterClickListener;

    public FilterAddView(final ImageFilter filter, final OnAddFilterClickListener onAddFilterClickListener) {
        super(new FlowLayout(FlowLayout.LEFT), true);

        this.filter = filter;
        this.onAddFilterClickListener = onAddFilterClickListener;

        JLabel filterTitleJText = new JLabel(filter.label());
        JButton addToFilterListJButton = new JButton("+");
        addToFilterListJButton.addMouseListener(new MouseClickListener() {
            public void mouseClicked(MouseEvent e) {
                if (onAddFilterClickListener != null) {
                    onAddFilterClickListener.onAddFilterClick(filter);
                }
            }
        });

        add(addToFilterListJButton);
        add(filterTitleJText);
    }

    public void setOnAddFilterClickListener(OnAddFilterClickListener onAddFilterClickListener) {
        this.onAddFilterClickListener = onAddFilterClickListener;
    }

    public interface OnAddFilterClickListener {
        void onAddFilterClick(ImageFilter filter);
    }
}
