/******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.internal.figures;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LayoutAnimator;
import org.eclipse.draw2d.TreeSearch;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gmf.runtime.gef.ui.figures.NodeFigure;

/**
 * This is the figure used for a group. The figure itself has nothing to paint
 * except for its children.
 * 
 * In order to support grouping of shapes with border items, the group figure is
 * an <code>IExpandableFigure</code> which has an extended bounds that
 * contains all the childrens border items. Otherwise, the group's bounds would
 * have had to include all the border items of its children. The group figure
 * contains a <code>BorderItemContainerFigure</code> similar to the
 * BorderedNodeFigure in order to accomplish this. To add shapes to this group,
 * use the {@link #getContainerFigure()} method.
 * 
 * @author crevells
 * 
 */
public class GroupFigure
    extends NodeFigure
    implements IExpandableFigure {

    /**
     * The <code>BorderItemContainerFigure</code> needs slight customization
     * to be used for groups.
     */
    private class BorderItemContainerFigureEx
        extends BorderItemContainerFigure {

        /**
         * The main figure is just the <code>GroupFigure</code>. There is no
         * other figure that needs to be painted.
         */
        public IFigure getMainFigure() {
            return getParent();
        }

        protected boolean useLocalCoordinates() {
            // the group children locations are relative to the group's location
            return true;
        }

    }

    /**
     * The figure that will hold all the children so that the children's border
     * items can be painted outside the bounds of the <code>GroupFigure</code>.
     */
    private BorderItemContainerFigure borderItemContainer;

    /**
     * Creates a new <code>GroupFigure</code>.
     */
    public GroupFigure() {
        super();
        setOpaque(false); // set transparent by default
        setLayoutManager(new XYLayout());
        add(getContainerFigure());
    }

    /**
     * Gets the container figure to which the shapes belonging to the group
     * should be added.
     * 
     * @return the container figure for the children
     */
    public IFigure getContainerFigure() {
        if (borderItemContainer == null) {
            borderItemContainer = createBorderItemContainerFigure();
        }
        return borderItemContainer;
    }

    private BorderItemContainerFigure createBorderItemContainerFigure() {
        BorderItemContainerFigure figure = new BorderItemContainerFigureEx();
        figure.setLayoutManager(new XYLayout());
        figure.addLayoutListener(LayoutAnimator.getDefault());
        figure.setVisible(true);
        return figure;
    }

    public Rectangle getClientArea(Rectangle rect) {
        return getContainerFigure().getClientArea(rect);
    }

    public boolean containsPoint(int x, int y) {
        return getContainerFigure().containsPoint(x, y);
    }

    public Rectangle getHandleBounds() {
        return getContainerFigure().getBounds();
    }

    protected void layout() {
        if (!this.getBounds().equals(getContainerFigure().getBounds())) {
            getContainerFigure().setBounds(this.getBounds().getCopy());
        }
    }

    public void erase() {
        super.erase();
        getContainerFigure().erase();
    }

    public void repaint() {
        super.repaint();
        getContainerFigure().repaint();
    }

    public IFigure findFigureAt(int x, int y, TreeSearch search) {
        return getContainerFigure().findFigureAt(x, y, search);
    }

    public IFigure findMouseEventTargetAt(int x, int y) {
        return getContainerFigure().findMouseEventTargetAt(x, y);
    }

    public boolean intersects(Rectangle rect) {
        return getExtendedBounds().intersects(rect);
    }

    public Dimension getMinimumSize(int wHint, int hHint) {
        return getContainerFigure().getMinimumSize(wHint, hHint);
    }

    public Dimension getPreferredSize(int wHint, int hHint) {
        return getContainerFigure().getPreferredSize(wHint, hHint);
    }

    public Rectangle getExtendedBounds() {
        return ((BorderItemContainerFigure) getContainerFigure())
                    .getExtendedBounds();
    }

}
