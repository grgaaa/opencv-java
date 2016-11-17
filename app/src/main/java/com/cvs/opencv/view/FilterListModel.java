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

    public void removeFilter(int index) {
        if (filters == null || filters.isEmpty()) {
            return;
        }
        filters.remove(index);
        fireContentsChanged(this, index, index);
    }

    public void switchFilter(int index1, int index2) {
        if (filters == null || filters.isEmpty()) {
            return;
        }
        ImageFilter imageFilter1 = filters.get(index1);
        ImageFilter imageFilter2 = filters.get(index2);
        filters.set(index1, imageFilter2);
        filters.set(index2, imageFilter1);

        fireContentsChanged(this, index1, index2);
    }

    public int getSize() {
        return filters == null ? 0 : filters.size();
    }

    public String getElementAt(int index) {
        return filters.get(index).label();
    }
}
