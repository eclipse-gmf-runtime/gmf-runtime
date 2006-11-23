/******************************************************************************
 * Copyright (c) 2003, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.internal.figures;

import java.util.Iterator;
import java.util.ListIterator;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ScalableFreeformLayeredPane;
import org.eclipse.draw2d.TreeSearch;
import org.eclipse.draw2d.Viewport;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gmf.runtime.diagram.ui.figures.BorderItemsAwareFreeFormLayer;
import org.eclipse.gmf.runtime.diagram.ui.figures.BorderItemsUtil;
import org.eclipse.gmf.runtime.diagram.ui.figures.BorderedNodeFigure;
import org.eclipse.gmf.runtime.gef.ui.figures.NodeFigure;

/**
 * BorderItemContainerFigures contain the main pane on which border items are
 * places. BorderItems are children of BorderItemContainerFigures and therefore
 * move when the parent moves. BorderItems within the BorderItemContainerFigure
 * may be painted outside the main figure and therefore the
 * BorderItemContainerFigure controls clipping region. The work of positioning
 * border items is handled by the locator. ( This implementation of border items
 * was re-written from the original created by Tauseef ). The orginal
 * implementation kept border items in a separate list ( not as children ) and
 * painted them after, requiring explicit moving of children.
 * 
 * @author tisrar, jbruck, cmahoney, mmostafa
 */
public class BorderItemContainerFigure
	extends NodeFigure {
    
    // rectangle indicating the extended bounds of the figure
    // extended bounds include the border items in the calculations 
    private Rectangle extendedBounds = new Rectangle();

	/**
	 * Constructor
	 */
	public BorderItemContainerFigure() {
		super();
	}

	/**
	 * Paints this Figure's client area. The client area is typically defined as
	 * the anything inside the Figure's {@link Border} or {@link Insets}, and
	 * by default includes the children of this Figure. On return, this method
	 * must leave the given Graphics in its initial state.
	 * 
	 * @param graphics
	 *            The Graphics used to paint
	 * 
	 */
	protected void paintClientArea(Graphics graphics) {
		if (getChildren().isEmpty() || !isVisible())
			return;
		Rectangle clip = getParentRectangle();
		graphics.setClip(clip);
		graphics.pushState();
		paintChildren(graphics);
		graphics.popState();
		graphics.restoreState();
	}

	/**
	 * gets the handle bounds of the main figure
	 * 
	 * @return the handle bounds of the main figure
	 * @see org.eclipse.gef.handles.HandleBounds#getHandleBounds()
	 */
	public Rectangle getHandleBounds() {
		Rectangle handleRect = null;

		ListIterator listIterator = getChildren().listIterator();
		while (listIterator.hasNext()) {
			Object object = listIterator.next();
			if (object instanceof NodeFigure
				&& ((NodeFigure) object).isVisible()) {
				if (handleRect == null) {
					handleRect = ((NodeFigure) object).getHandleBounds()
						.getCopy();
				} else {
					handleRect.union(((NodeFigure) object).getHandleBounds()
						.getCopy());
				}
			}
		}
		return handleRect;
	}

	/**
	 * We need to override this for smooth painting of border item items.
	 */
	public boolean containsPoint(int x, int y) {
		for (int i = getChildren().size(); i > 0;) {
			i--;
			IFigure fig = (IFigure) getChildren().get(i);
			if (fig.containsPoint(x, y)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns a descendant of this Figure such that the Figure returned
	 * contains the point (x, y), and is accepted by the given TreeSearch.
	 * Returns <code>null</code> if none found.
	 * 
	 * @param x
	 *            The X coordinate
	 * @param y
	 *            The Y coordinate
	 * @param search
	 *            the TreeSearch
	 * @return The descendant Figure at (x,y)
	 */
	protected IFigure findDescendantAtExcluding(int x, int y, TreeSearch search) {
		Point pt = new Point(x, y);
		this.translateFromParent(pt);

		IFigure fig;
		for (int i = getChildren().size(); i > 0;) {
			i--;
			fig = (IFigure) getChildren().get(i);
			if (fig.isVisible()) {
				fig = fig.findFigureAt(pt.x, pt.y, search);
				if (fig != null)
					return fig;
			}
		}
		// No descendants were found
		return null;
	}

	public IFigure findFigureAt(int x, int y, TreeSearch search) {
		if (search.prune(this))
			return null;
		IFigure child = findDescendantAtExcluding(x, y, search);
		if (child != null)
			return child;
		return null;
	}

	/**
	 * Returns the deepest descendant for which {@link #isMouseEventTarget()}
	 * returns <code>true</code> or <code>null</code> if none found. The
	 * Parameters <i>x</i> and <i>y</i> are absolute locations. Any Graphics
	 * transformations applied by this Figure to its children during
	 * {@link #paintChildren(Graphics)} (thus causing the children to appear
	 * transformed to the user) should be applied inversely to the points <i>x</i>
	 * and <i>y</i> when called on the children.
	 * 
	 * @param x
	 *            The X coordinate
	 * @param y
	 *            The Y coordinate
	 * @return The deepest descendant for which isMouseEventTarget() returns
	 *         true
	 */
	public IFigure findMouseEventTargetAt(int x, int y) {
		IFigure f = findMouseEventTargetInDescendantsAt(x, y);
		if (f != null)
			return f;
		if (isMouseEventTarget())
			return this;
		return null;
	}

	/**
	 * Searches this Figure's children for the deepest descendant for which
	 * {@link #isMouseEventTarget()} returns <code>true</code> and returns
	 * that descendant or <code>null</code> if none found.
	 * 
	 * @see #findMouseEventTargetAt(int, int)
	 * @param x
	 *            The X coordinate
	 * @param y
	 *            The Y coordiante
	 * @return The deepest descendant for which isMouseEventTarget() returns
	 *         true
	 */
	protected IFigure findMouseEventTargetInDescendantsAt(int x, int y) {
		Point pt = new Point(x, y);
		this.translateFromParent(pt);

		IFigure fig;
		for (int i = getChildren().size(); i > 0;) {
			i--;
			fig = (IFigure) getChildren().get(i);
			if (fig.isVisible() && fig.isEnabled()) {
				if (fig.containsPoint(pt.x, pt.y)) {
					fig = fig.findMouseEventTargetAt(pt.x, pt.y);
					return fig;
				}
			}
		}
		return null;
	}

	/**
	 * @see org.eclipse.draw2d.IFigure#intersects(Rectangle)
	 */
	public boolean intersects(Rectangle rect) {		
		Rectangle rectangle = getParentRectangle();
		return rectangle.intersects(rect);
	}
    
    private Rectangle getParentRectangle() {
        return _getParentRectangle();
    }
    
    /**
     * Return the area of the parent viewport if there is one, otherwise, return
     * the client area of the parent.
     */
    private Rectangle _getParentRectangle() {
        Rectangle rect = getParent().getParent().getClientArea().getCopy();

        IFigure port = getViewport();
        if (port != null) {
            Rectangle portRect = port.getClientArea().getCopy();
            if (portRect.height != 0 && portRect.width != 0) {
                rect = portRect;
            }
        }
        return rect;
    }

	private IFigure getMainFigure(BorderItemContainerFigure gf) {
		BorderedNodeFigure gpf = (BorderedNodeFigure) gf.getParent();
		return gpf.getMainFigure();
	}

	/**
	 * Helper to retrieve the viewport that this item sits on.
	 * 
	 * @return the view port
	 */
	private IFigure getViewport() {
		//
		// Start searching for viewport to clip on from the main figure.
		//
		IFigure fig = getMainFigure(this);
		while (fig != null) {
			if (fig instanceof Viewport
				|| fig instanceof ScalableFreeformLayeredPane)
				return fig;
			fig = fig.getParent();
			if (fig instanceof BorderItemContainerFigure) {
				fig = getMainFigure((BorderItemContainerFigure) fig);
			}
		}
		return null;
	}

	public void erase() {
		if (getChildren().isEmpty()) {
			super.erase();
		} else {
			if (getParent() == null || !isVisible())
				return;
			repaint();
		}
	}

	/**
	 * Refresh adornments
	 */
	public void repaint() {
		if (getChildren().isEmpty()) {
			super.repaint();
		} else {
			if (getParent() == null || !isVisible())
				return;
			Rectangle rectBounds = getParentRectangle();
			getParent().getParent().repaint(rectBounds);
			if (getViewport() != null) {
				getViewport().repaint(rectBounds);
			}
		}
	}
    
    public void invalidate() {
        extendedBounds = null;
        super.invalidate();
        updateLayerExtents();
    }
    
    

    public Rectangle getExtendedBounds(){
        if (extendedBounds == null)
            extendedBounds = getExtendedBounds(getParent()).getCopy();
        return extendedBounds;
    }
    
       
    private Rectangle getExtendedBounds(IFigure figure) {
        if (figure == null)
            return getBounds().getCopy();
        else {
            Rectangle _bounds = figure.getBounds().getCopy();
            if (figure instanceof BorderedNodeFigure){
                BorderedNodeFigure borderedFigure = (BorderedNodeFigure)figure;
                BorderItemContainerFigure borderedItemContainer = (BorderItemContainerFigure)borderedFigure.getBorderItemContainer();
                if (borderedItemContainer!=null){
                    Iterator iterator = borderedItemContainer.getChildren().iterator();
                    while (iterator.hasNext()) {
                        Figure element = (Figure) iterator.next();
                        if (element instanceof BorderedNodeFigure){
                            BorderedNodeFigure childbFigure = (BorderedNodeFigure)element;
                            BorderItemContainerFigure childBorderedItemContainer = (BorderItemContainerFigure)childbFigure.getBorderItemContainer();
                            if (childBorderedItemContainer!=null)
                                _bounds.union(childBorderedItemContainer.getExtendedBounds());
                            else
                                _bounds.union(childbFigure.getBounds());
                        }
                        else
                            _bounds.union(element.getBounds());
                     }
                }
            }
            return _bounds;
        }
    }
    
    protected void fireFigureMoved() {
        super.fireFigureMoved();
        extendedBounds = null;
        updateLayerExtents();
    }

    private void updateLayerExtents() {
        BorderItemsAwareFreeFormLayer layer = BorderItemsUtil.getBorderItemLayer(this);
        if (layer!=null){
            layer.borderFigureMoved();
        }
    }
}
