/*
 * @(#)EduLensOn11.java
 *
 * This program is version 2.0 of JDC Education Kit for Optical Experiments.
 * Copyright (C) Jasper Display Corporation 2013.
 * This program is free software; you can redistribute it
 * and/or modify it under the terms of the GNU General Public
 * License version 2.0 as published by the Free Software Foundation. 

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details. 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA. 
 * You may contact Jasper Display Corporation by email at info@jasperdisplay.com
 * or by telephone at +1-408-855-6640.
 * More information is provided at http://www.jasperdisplay.com/.
 */
package com.jasper.ui;

import com.jasper.core.PatternImage;
import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;

/**
 *
 * @author sonnv
 */
public class EduLensOn11 extends JComponent implements MouseMotionListener {
    private JComponent comp;
    private Point point;
    private Dimension mySize;
    private Robot robot;
    public BufferedImage canvas;

    public EduLensOn11(JComponent comp, Dimension size) {
        // flag to say don't draw until we get a MouseMotionEvent
        point = new Point(-1, -1);
        comp.addMouseMotionListener(this);
        this.comp = comp;
        this.mySize = size;
        comp.addMouseMotionListener(this);
        // if we can't get a robot, then we just never
        // paint anything
        try {
            robot = new Robot();
        } catch (AWTException awte) {
            System.err.println("Can't get a Robot");
            awte.printStackTrace();
        }
    }

    public void paint(Graphics g) {
        if ((robot == null) || (point.x == -1)) {
            g.setColor(Color.black);
            g.fillRect(0, 0, mySize.width, mySize.height);
            return;
        }
        PatternImage image = ((EduPatternJPanel) comp).pimage;
        // get width and height of pattent panel
        int patternAreaWidth = comp.getBounds().width;
        int patternAreaHeight = comp.getBounds().height;
        
        int fullScreenWidth = PatternImage.width;
        int fullScreenHeight = PatternImage.height;

        //Mouse location
        int mouseX = (int) point.x * fullScreenWidth / patternAreaWidth;
        int mouseY = (int) point.y * fullScreenHeight / patternAreaHeight;

        //Draw area location
        int grabWidth = (int) ((double) mySize.width);
        int grabHeight = (int) ((double) mySize.height);

        int topLeftX = mouseX - (int) grabWidth/2;
        int topLeftY = mouseY - (int) grabHeight/2;
        int bottomRightX = mouseX + (int) grabWidth/2;
        int bottomRightY = mouseY + (int) grabHeight/2;

        //Caculate bounds
        if(topLeftX < 0) {
            topLeftX = 0;
        }

        if(bottomRightX > fullScreenWidth) {
            topLeftX = fullScreenWidth - grabWidth;
        }
        if(topLeftY < 0) {
            topLeftY = 0;
        }

        if(bottomRightY > fullScreenHeight) {
            topLeftY = fullScreenHeight - grabHeight;
        }

        //Draw buffer with bounds
        BufferedImage grabImg = image.canvas;
        g.drawImage(grabImg, -topLeftX, -topLeftY, null);
        g.dispose();

    }

    public Dimension getPreferredSize() {
        return mySize;
    }

    public Dimension getMinimumSize() {
        return mySize;
    }

    public Dimension getMaximumSize() {
        return mySize;
    }

    // MouseMotionListener implementations
    public void mouseMoved(MouseEvent e) {
        point = e.getPoint();
        repaint();
    }

    public void mouseDragged(MouseEvent e) {
        mouseMoved(e);
    }
}
