/******************************************************************************
 * Copyright (c) 2003, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *    Mariot Chauvin <mariot.chauvin@obeo.fr> - bug 259507
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.figures;

import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.DelegatingLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LayoutAnimator;
import org.eclipse.draw2d.TreeSearch;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gmf.runtime.diagram.ui.internal.figures.BorderItemContainerFigure;
import org.eclipse.gmf.runtime.diagram.ui.figures.IExpandableFigure;
import org.eclipse.gmf.runtime.gef.ui.figures.NodeFigure;

/**
 * Wrapper figure that contains the main figure and the border item figures.
 * This is required to allow the main figure's layout manager to ignore the
 * border items. The border item container applies a delegating layout manager
 * to allow border item children to lay themselves out.
 * 
 * @author jbruck, cmahoney, mmostafa
 */
public class BorderedNodeFigure
	extends NodeFigure implements IExpandableFigure{

	private BorderItemContainerFigure borderItemContainer;

	private IFigure mainFigure;
	
	private Rectangle extendedBounds = new Rectangle();

	/**
	 * Creates a new BorderedNodeFigure figure.
	 * 
	 * @param mainFigure
	 *            the figure to use with this figure
	 */
	public BorderedNodeFigure(IFigure mainFigure) {
		super();
		setOpaque(false); // set transparent by default
		setBorder(null);
		setLayoutManager(null);
		this.mainFigure = mainFigure;

		add(getMainFigure());
		add(getBorderItemContainer());

		setBounds(getMainFigure().getBounds().getCopy());
		getBorderItemContainer().setBounds(new Rectangle(0, 0, 1, 1));
	}

	/**
	 * Gets the border item container figure to which border item figures can be
	 * added with a {@link IBorderItemLocator} as the constraint and then later
	 * removed.
	 * 
	 * @return The border item container figure
	 */
	public IFigure getBorderItemContainer() {
		if (borderItemContainer == null) {
			borderItemContainer = new BorderItemContainerFigure();
			borderItemContainer
				.setLayoutManager(new DelegatingLayout());
			borderItemContainer.addLayoutListener(LayoutAnimator.getDefault());
			borderItemContainer.setVisible(true);
		}
		return borderItemContainer;
	}

	/**
	 * Gets the main figure of this bordered figure.
	 * 
	 * @return The "main" figure
	 */
	public IFigure getMainFigure() {
		return mainFigure;
	}

	public Rectangle getClientArea(Rectangle rect) {
		if (getMainFigure() != null) {
			return getMainFigure().getClientArea(rect);
		}
		return super.getClientArea(rect);
	}

	/**
	 * gets the handle bounds of the main figure
	 * 
	 * @return the hnalde bounds
	 */
	public Rectangle getHandleBounds() {
		if (getMainFigure() instanceof NodeFigure) {
			return ((NodeFigure) getMainFigure()).getHandleBounds().getCopy();
		} else {
			return getMainFigure().getBounds().getCopy();
		}
	}
	
	/**
	 * Give the main figure the entire bounds of the wrapper.
	 */
	protected void layout() {
		if (!this.getBounds().equals(getMainFigure().getBounds())) {
			getMainFigure().setBounds(this.getBounds().getCopy());
			getBorderItemContainer().invalidateTree();
			erase();
		}
	}

	/**
	 * We need to override this for smooth painting of border items.
	 */
	public boolean containsPoint(int x, int y) {
		if (borderItemContainer.containsPoint(x, y)) {
			return true;
		}
		return super.containsPoint(x, y);
	}

	protected void primTranslate(int dx, int dy) {
		super.primTranslate(dx, dy);
		erase();
	}

	public void erase() {
		super.erase();
		if (borderItemContainer != null)
			borderItemContainer.erase();
	}

	/**
	 * Refresh adornments
	 */
	public void repaint() {
		super.repaint();
		if (borderItemContainer != null)
			borderItemContainer.repaint();
	}

	public IFigure findFigureAt(int x, int y, TreeSearch search) {
		if (search.prune(this))
			return null;
		IFigure result = borderItemContainer.findFigureAt(x, y, search);
		if (result != null) {
			return result;
		}
		return getMainFigure().findFigureAt(x, y, search);
	}

	public IFigure findMouseEventTargetAt(int x, int y) {
		IFigure borderItemFigure = borderItemContainer.findMouseEventTargetAt(
			x, y);
		if (borderItemFigure != null)
			return borderItemFigure;
		return super.findMouseEventTargetAt(x, y);
	}

    public boolean intersects(Rectangle rect) {
        if (getExtendedBounds().intersects(rect))
            return true;
        return super.intersects(rect);
    }

	public Dimension getMinimumSize(int wHint, int hHint) {
		return getMainFigure().getMinimumSize(wHint, hHint);
	}

	public Dimension getPreferredSize(int wHint, int hHint) {
		return getMainFigure().getPreferredSize(wHint, hHint);
	}

	public IFigure getToolTip() {
		return getMainFigure().getToolTip();
	}

	public void setToolTip(IFigure f) {
		getMainFigure().setToolTip(f);
	}

	public ConnectionAnchor getSourceConnectionAnchorAt(Point p) {
		if (getMainFigure() instanceof NodeFigure)
			return ((NodeFigure) getMainFigure())
				.getSourceConnectionAnchorAt(p);
		return super.getSourceConnectionAnchorAt(p);
	}

	public ConnectionAnchor getTargetConnectionAnchorAt(Point p) {
		if (getMainFigure() instanceof NodeFigure)
			return ((NodeFigure) getMainFigure())
				.getTargetConnectionAnchorAt(p);
		return super.getTargetConnectionAnchorAt(p);
	}

	public ConnectionAnchor getConnectionAnchor(String terminal) {
		if (getMainFigure() instanceof NodeFigure)
			return ((NodeFigure) getMainFigure()).getConnectionAnchor(terminal);
		return super.getConnectionAnchor(terminal);
	}

	public String getConnectionAnchorTerminal(ConnectionAnchor c) {
		if (getMainFigure() instanceof NodeFigure)
			return ((NodeFigure) getMainFigure())
				.getConnectionAnchorTerminal(c);
		return super.getConnectionAnchorTerminal(c);
	}

	/**
	 * This method provides a generic way to get a target figure's parent's main
	 * figure where if the target figure is a border item figure, then its
	 * parent is considered the figure on which it borders.It is not possible to
	 * get a target figure's parent using <code>IFigure#getParent()</code> if
	 * the target figure is a border item figure, since this would return the
	 * border item container figure.
	 * 
	 * @param target
	 *            the target figure, may or may not be acting as a border item
	 *            figure
	 * @return the parent figure or if the target figure was on a border item
	 *         container figure, then the main figure that it borders.
	 */
	public static IFigure getParentFigure(IFigure target) {
		IFigure parent = target.getParent();
		if (parent instanceof BorderItemContainerFigure) {
			return ((BorderedNodeFigure) parent.getParent()).getMainFigure();
		}
		return parent;
	}
    
    public Rectangle getExtendedBounds() {
        if (extendedBounds == null) {
	        Rectangle rect = getBounds().getCopy();
	         if (borderItemContainer!=null){
	            rect = rect.union(borderItemContainer.getExtendedBounds());
	        }
	         extendedBounds = rect;
        }
        return extendedBounds;
    }
    
    @Override
    public void invalidate() {
        extendedBounds = null;
    	super.invalidate();
    }
    
    @Override
    public void validate() {
        extendedBounds = null;
    	super.validate();
    }
    
    @Override
    protected void fireFigureMoved() {
    	super.fireFigureMoved();
        extendedBounds = null;
    }
}
