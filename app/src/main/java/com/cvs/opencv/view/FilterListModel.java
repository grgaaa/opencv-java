package com.cvs.opencv.view;

import com.cvs.opencv.filters.ImageFilter;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gregor.horvat on 28. 07. 2016.
 */
public class FilterListModel extends AbstractListModel<String> {

    private List<ImageFilter> filters;


    public void clear() {
        if (filters != null) {
            filters.clear();
        }
        filters = null;
        fireContentsChanged(this, 0, getSize());
    }

    public List<ImageFilter> getFilters() {
        return filters;
    }

    public void setFilters(List<ImageFilter> filters) {
        this.filters = filters;
        fireContentsChanged(this, 0, getSize());
    }

    public void addFilter(ImageFilter filter) {
        if (filters == null) {
            filters = new ArrayList<ImageFilter>();
        }
        filters.add(filter);
        fireContentsChanged(this, 0, getSize());
    }

    public int getSize() {
        return filters == null ? 0 : filters.size();
    }

    public String getElementAt(int index) {
        return filters.get(index).label();
    }
}
