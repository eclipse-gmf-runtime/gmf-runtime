/******************************************************************************
 * Copyright (c) 2003, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.internal.figures;

import java.util.List;
import java.util.ListIterator;

import org.eclipse.draw2d.FigureListener;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.GraphicsSource;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ScalableFreeformLayeredPane;
import org.eclipse.draw2d.TreeSearch;
import org.eclipse.draw2d.UpdateManager;
import org.eclipse.draw2d.Viewport;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gmf.runtime.diagram.ui.figures.BorderItemsAwareFreeFormLayer;
import org.eclipse.gmf.runtime.diagram.ui.figures.BorderItemsUtil;
import org.eclipse.gmf.runtime.diagram.ui.figures.BorderedNodeFigure;
import org.eclipse.gmf.runtime.diagram.ui.figures.IExpandableFigure;
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
    
    private BorderItemContainerHelper helper = new BorderItemContainerHelper();
    
	private BorderedItemContainerUpdateManagerWrapper updateManagerWrapper = new BorderedItemContainerUpdateManagerWrapper();
    
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
		Rectangle clip = getVisibleExtendedBounds();
		graphics.setClip(clip);

        if (useLocalCoordinates()) {
            graphics.translate(getBounds().x + getInsets().left, getBounds().y
                + getInsets().top);
		}
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

	protected void primTranslate(int dx, int dy) {
		/*
		 * If the border item container is being moved, the children must move by the same amount too
		 * (as opposed to free form figure).
		 * We must ignore children moves in this case in the helper, since border item container shouldn't be invalidated.
		 */
		helper.ignoreChildFigureMovesHandling = true;
		super.primTranslate(dx, dy);
		helper.ignoreChildFigureMovesHandling = false;
	}

	/**
	 * We need to override this for smooth painting of border item items.
	 */
	public boolean containsPoint(int x, int y) {
		if (useLocalCoordinates()) {
            x = x - getBounds().x - getInsets().left;
            y = y - getBounds().y - getInsets().top;
        }

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
		Rectangle rectangle = getExtendedBounds();
		return rectangle.intersects(rect);
	}
	
    /**
     * Gets the area of the extended bounds which is visible, that is, that is
     * contained within the viewport bounds. This is needed so that when this
     * shape is within a scrollable compartment, the border items are not
     * visible when they are outside the visible area.
     * 
     * @return the area of the extended bounds which is visible
     */
    private Rectangle getVisibleExtendedBounds() {
        Rectangle extendedRect = getExtendedBounds().getCopy();
        translateToAbsolute(extendedRect);
        Rectangle parentRect = getViewportBounds().getIntersection(
            extendedRect);
        translateToRelative(parentRect);
        return parentRect;
    }
    
    /**
     * Return the area of the parent viewport if there is one, otherwise, return
     * the client area of the parent. The viewport area is required so that
     * border items outside the viewport are not shown (i.e. when they are
     * scrolled out of view).
     * 
     * @return returns the viewport client area in absolute coordinates
     */
    private Rectangle getViewportBounds() {
        Rectangle rect = getParent().getParent().getClientArea().getCopy();
        getParent().getParent().translateToParent(rect);
        getParent().getParent().translateToAbsolute(rect);

        IFigure port = getViewport();
        if (port != null) {
            Rectangle portRect = port.getClientArea().getCopy();
            port.translateToParent(portRect);
            port.translateToAbsolute(portRect);

            if (portRect.height != 0 && portRect.width != 0) {
                rect = portRect;
            }
        }
        return rect;
    }
    
	public IFigure getMainFigure() {
        return ((BorderedNodeFigure) getParent()).getMainFigure();
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
		IFigure fig = getMainFigure();
		while (fig != null) {
			if (fig instanceof Viewport
	                || fig instanceof ScalableFreeformLayeredPane)
				return fig;
			fig = fig.getParent();
			if (fig instanceof BorderItemContainerFigure) {
				fig = ((BorderItemContainerFigure) fig).getMainFigure();
			}
		}
		return null;
	}

	public void erase() {
		if (getParent() != null && isVisible()) {
			repaint();
		}
	}

	/**
	 * Refresh adornments
	 */
	public void repaint() {
		repaint(getExtendedBounds());
	}
    
    public void invalidate() {
        extendedBounds = null;
        super.invalidate();
        updateLayerExtents();
    }
    
    public void validate() {
        extendedBounds = null;
        super.validate();
    }
    
	@Override
	public UpdateManager getUpdateManager() {
		if (getParent() != null) {
			return updateManagerWrapper;
		}
		return super.getUpdateManager();
	}

    /**
     * Gets the extended bounds of the figure which includes the bounds of all
     * the border item figures.
     * 
     * @return the extended bounds
     */
    public Rectangle getExtendedBounds() {
        if (extendedBounds == null) {
            extendedBounds = getParent().getBounds().getCopy();
            for (IFigure childFigure : (List<IFigure>) getChildren()) {
                Rectangle childBounds = (childFigure instanceof IExpandableFigure) ? ((IExpandableFigure) childFigure)
                    .getExtendedBounds()
                    : childFigure.getBounds(); 
                if (useLocalCoordinates()) {
                    childBounds = childBounds.getCopy();
                    childBounds.translate(getLocation());
                }
                extendedBounds.union(childBounds);
            }
        }
        return extendedBounds;
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
    
	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.Figure#add(org.eclipse.draw2d.IFigure, java.lang.Object, int)
	 */
	public void add(IFigure figure, Object constraint, int index) {
		super.add(figure, constraint, index);
		helper.hookChild(figure);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.Figure#remove(org.eclipse.draw2d.IFigure)
	 */
	public void remove(IFigure figure) {
		helper.unhookChild(figure);
		super.remove(figure);
	}
    
    private class BorderItemContainerHelper {
    	
    	boolean ignoreChildFigureMovesHandling;
    	
    	class ChildTracker implements FigureListener {
    		public void figureMoved(IFigure source) {
    			if (!ignoreChildFigureMovesHandling) {
    				revalidate();
    			}
    		}
    	}
    	
    	private FigureListener figureListener = new ChildTracker();
    	
    	public void hookChild(IFigure child) {
    	    revalidate();
    		child.addFigureListener(figureListener);
    	}
    	
    	public void unhookChild(IFigure child) {
    	    revalidate();
    		child.removeFigureListener(figureListener);
    	}
    	
    }
    
    private class BorderedItemContainerUpdateManagerWrapper extends UpdateManager {
    	
    	public BorderedItemContainerUpdateManagerWrapper() {
    	}

		@Override
		public void setGraphicsSource(GraphicsSource gs) {
			BorderItemContainerFigure.this.getParent().getUpdateManager().setGraphicsSource(gs);
		}

		@Override
		public synchronized void addDirtyRegion(IFigure figure, int x, int y,
				int w, int h) {
			Rectangle r = new Rectangle(x, y, w, h);
			IFigure borderedNodeParent = BorderItemContainerFigure.this.getParent().getParent();
			IFigure walker = figure;
			do {
				walker = walker.getParent();
				walker.translateToParent(r);
			} while (walker != borderedNodeParent && walker.getParent() != null);
			walker.getUpdateManager().addDirtyRegion(walker, r.x, r.y, r.width, r.height);
		}

		@Override
		public synchronized void addInvalidFigure(IFigure f) {
			BorderItemContainerFigure.this.getParent().getUpdateManager().addInvalidFigure(f);
		}

		@Override
		public synchronized void performUpdate() {
			BorderItemContainerFigure.this.getParent().getUpdateManager().performUpdate();
		}

		@Override
		public synchronized void performUpdate(Rectangle exposed) {
			BorderItemContainerFigure.this.getParent().getUpdateManager().performUpdate(exposed);
		}

		@Override
		public void performValidation() {
			BorderItemContainerFigure.this.getParent().getUpdateManager().performValidation();
		}

		@Override
		public synchronized void runWithUpdate(Runnable runnable) {
			BorderItemContainerFigure.this.getParent().getUpdateManager().runWithUpdate(runnable);
		}

		@Override
		public void setRoot(IFigure figure) {
			BorderItemContainerFigure.this.getParent().getUpdateManager().setRoot(figure);
		}

    }

}
