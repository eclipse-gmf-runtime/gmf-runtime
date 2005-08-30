/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.internal.figures;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

import org.eclipse.gmf.runtime.draw2d.ui.figures.FigureUtilities;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.MapMode;


/**
 * Page Breaks Figure.  A rectangle divided into subrectangles 
 * which are usually based on the printable page size.  This figure
 * supports two different states.  One for the regular figure and one used
 * as a feedback figure. 
 *  
 * @author jcorchis
 * @canBeSeenBy %level1
 */
public class PageBreaksFigure extends Figure {

    /* constants used to indicated the type of figure */
    public static final boolean FIGURE = true;
    public static final boolean FEEDBACK = false;

    /* Initial number of rows and columns */
    private int rows = 1;
    private int cols = 1;

    private int BORDER_LINE_WIDTH = MapMode.DPtoLP(7);
    private int LINE_WIDTH = MapMode.DPtoLP(1);
    private int BOTTOM_PAGE_MARGIN = MapMode.DPtoLP(60);

    // List that contains the bounds of the filled 
    // rectangles which comprise the figure.    
    private List recList;

    private boolean figureType = FIGURE;

    /**
     * Constructor for PageBreaksFigure.
     */
    public PageBreaksFigure(boolean type) {
        super();
        figureType = type;
        if (type) {
            recList = new ArrayList(5);
	    }
    }

    public void setPageCount(int rows, int cols) {
        this.rows = Math.max(1, rows);
        this.cols = Math.max(1, cols);
    }

    public void setRows(int rows) {
        this.rows = Math.max(1, rows);
    }

    public void setCols(int cols) {
        this.cols = Math.max(1, cols);
    }

    public Point getPageCount() {
        return new Point(rows, cols);
    }

    /**
     * @see Figure#paintBorder(Graphics)
     */
    protected void paintBorder(Graphics g) {
        super.paintBorder(g);
    }

    /**
     * @see Figure#paintFigure(Graphics)
     */
    protected void paintFigure(Graphics g) {
        if (figureType) {
            paintPageFigure(g);
        } else {
            paintFeedbackFigure(g);
        }
    }

    /**
     * The regular version of paint(Graphics g)
     */
    private void paintPageFigure(Graphics g) {
        super.paintFigure(g);
        recList.clear();
        Rectangle r = getBounds();

        g.setForegroundColor(ColorConstants.blue);
        g.setBackgroundColor(ColorConstants.blue);

        // Draw the border
        Rectangle top = new Rectangle(r.x, r.y, r.width, BORDER_LINE_WIDTH);
        recList.add(top);

        Rectangle right =
            new Rectangle(
                r.x + r.width - BORDER_LINE_WIDTH,
                r.y,
                BORDER_LINE_WIDTH + 1,
                r.height);
        recList.add(right);

        Rectangle left = new Rectangle(r.x, r.y, BORDER_LINE_WIDTH, r.height);
        recList.add(left);

        Rectangle bottom =
            new Rectangle(
                r.x,
                r.y + r.height - BORDER_LINE_WIDTH,
                r.width,
                BORDER_LINE_WIDTH + 1 );
        recList.add(bottom);

        for (int i = 0; i < recList.size(); i++) {
            g.fillRectangle((Rectangle) recList.get(i));
        }

        // Draw the internal page division lines
        g.setLineWidth(LINE_WIDTH);
        Point location = getLocation();

        int colSize = (int) Math.floor(r.width / cols);
        for (int i = 1; i < cols; i++) {
            int colsOffset = colSize * i;
            Rectangle rec =
                new Rectangle(
                    location.x + colsOffset,
                    location.y,
                    LINE_WIDTH,
                    r.height);
            recList.add(rec);
            g.fillRectangle(rec);
        }

        int rowSize = (int) Math.floor(r.height / rows);
        for (int i = 1; i < rows; i++) {
            int rowOffset = rowSize * i;
            Rectangle rec =
                new Rectangle(
                    location.x,
                    location.y + rowOffset,
                    r.width,
                    LINE_WIDTH);
            recList.add(rec);
            g.fillRectangle(rec);
        }

        // Add page numers centered at the bottom of each page
        int halfPageSize = (int) Math.floor(colSize / 2);
        for (int col = 1; col < cols + 1; col++) {
            for (int row = 1; row < rows + 1; row++) {
                int colsOffset = location.x + (colSize * col);
                int rowOffset = location.y + (rowSize * row);
                int topCenter = colsOffset - halfPageSize;
                String pageNumber = row + " - " + col; //$NON-NLS-1$
                Dimension size =
                    FigureUtilities.getTextExtents(pageNumber, getFont());
                g.drawString(
                    pageNumber,
                    topCenter - size.width,
                    rowOffset - (BOTTOM_PAGE_MARGIN + size.height));
            }
        }
    }

    /**
     * The feedback version of paint(Graphics g)
     */
    private void paintFeedbackFigure(Graphics g) {
        super.paintFigure(g);
        Rectangle r = getBounds();

        g.setForegroundColor(ColorConstants.blue);
        g.setBackgroundColor(ColorConstants.blue);
        g.setLineStyle(Graphics.LINE_DASHDOT);

        g.drawRectangle(r.x, r.y, r.width - 1, r.height - 1);


        int colSize = (int) Math.floor(r.width / cols);
        for (int i = 1; i < cols; i++) {
            int colsOffset = colSize * i;
            g.drawLine(r.x + colsOffset, r.y, r.x + colsOffset, r.y + r.height);
        }

        int rowSize = (int) Math.floor(r.height / rows);
        for (int i = 1; i < rows; i++) {
            int rowOffset = rowSize * i;
            g.drawLine(r.x, r.y + rowOffset, r.x + r.width, r.y + rowOffset);
        }
    }

    public boolean containsPoint(int x, int y) {
    	if (figureType == FEEDBACK)
    		return false;
    		
        if (isOpaque())
            return super.containsPoint(x, y);

        for (int i = 0; i < recList.size(); i++) {
            Rectangle r = (Rectangle) recList.get(i);
            if (r.contains(x, y)) {
                return true;
            }
        }
        return false;
    }
}
