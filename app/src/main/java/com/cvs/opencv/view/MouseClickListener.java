package com.cvs.opencv.view;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created by gregor.horvat on 27. 07. 2016.
 */
public abstract class MouseClickListener implements MouseListener {

    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}

    public abstract void mouseClicked(MouseEvent e);
}
