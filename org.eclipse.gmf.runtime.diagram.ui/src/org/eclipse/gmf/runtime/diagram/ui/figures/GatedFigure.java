/******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/


package org.eclipse.gmf.runtime.diagram.ui.figures;

import java.util.ListIterator;

import org.eclipse.draw2d.Border;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Locator;
import org.eclipse.draw2d.ScalableFreeformLayeredPane;
import org.eclipse.draw2d.TreeSearch;
import org.eclipse.draw2d.Viewport;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

import org.eclipse.gmf.runtime.gef.ui.figures.NodeFigure;

/**
 * GatedFigures contain the main pane on which gates are places.
 * Gates are children of GatedFigures and therefore move when the parent moves.
 * Gates within the GatedFigure may be painted outside the main figure and therefore
 * the GatedFigure controls clipping region.
 * The work of positioning gates is handled by the locator.
 * 
 * ( This implementation of gates was re-written from the original created by Tauseef ).
 * The orginal implementation kept gates in a separate list ( not as children ) and painted them after,
 * requiring explicit moving of children.
 *  
 * @author tisrar
 * @author jbruck
 */
public class GatedFigure extends NodeFigure {
	
	/**
	 * Constructor
	 */
	public GatedFigure() {
		super();
	}

	
	/**
	 * Simply paint the children.
	 */
	protected void paintFigure(Graphics graphics) {
		paintClientArea(graphics);
	}

	/**
	 * Paints this Figure's client area. The client area is typically defined as the anything
	 * inside the Figure's {@link Border} or {@link Insets}, and by default includes the
	 * children of this Figure. On return, this method must leave the given Graphics in its
	 * initial state.
	 * @param graphics The Graphics used to paint
	 * @since 2.0
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
	 * Adds a gate figure to this figure. It is different than adding gate as a
	 * "child" because gates are not affected by the layout manager of this
	 * figure. This method also keep account of the largest gate size added so
	 * that the client area and the border are shrinked accoringly.
	 * 
	 * @param figure  The GateFigure.
	 * @param constraint the locator to associate with this figure
	 */
	public void addGate(GateFigure figure, Object constraint) {
		add(figure, constraint);
		//TODO: remove this unecessary call.
		figure.setLocator((Locator)constraint);
		
	}
			
	
	/**
	 * gets the handle bounds of the main figure
	 * @return the handle bounds of the main figure
	 * @see org.eclipse.gef.handles.HandleBounds#getHandleBounds()
	 */
	public Rectangle getHandleBounds() {
		Rectangle handleRect = null;
		
		ListIterator listIterator = getChildren().listIterator();
		while (listIterator.hasNext()) {
			Object object = listIterator.next();
			if( object instanceof NodeFigure && ((NodeFigure)object).isVisible())	{
				if( handleRect == null ) {
					handleRect = ((NodeFigure) object).getHandleBounds().getCopy();
				} else {
					handleRect.union(((NodeFigure) object).getHandleBounds().getCopy());
				}
			}
		}
		return handleRect;
	}

	/**
	 * Removes a gate from this figure. This method also sets the new valid max
	 * padding from the sides if the existing are equal to this gates
	 * dimension.
	 * 
	 * @param figure
	 *            The GateFigure.
	 */
	public void removeGate(GateFigure figure) {
		remove(figure);
	}

	/**
	 * @see org.eclipse.draw2d.IFigure#containsPoint(int, int)
	 * We need to override this for smooth painting of gate items.
	 */
	public boolean containsPoint(int x, int y) {
		for (int i = getChildren().size(); i > 0;) {
			i--;
			IFigure fig = (IFigure)getChildren().get(i);
			if( fig.containsPoint(x,y) ) {
				return true;
			}
		}
		return false;
	}
	

	/**
	 * Returns a descendant of this Figure such that the Figure returned contains the point
	 * (x, y), and is accepted by the given TreeSearch. Returns <code>null</code> if none 
	 * found.
	 * @param x The X coordinate
	 * @param y The Y coordinate
	 * @param search the TreeSearch
	 * @return The descendant Figure at (x,y)
	 */
	protected IFigure findDescendantAtExcluding(int x, int y, TreeSearch search) {
		Point pt = new Point(x, y);
		this.translateFromParent(pt);
				
		IFigure fig;
		for (int i = getChildren().size(); i > 0;) {
			i--;
			fig = (IFigure)getChildren().get(i);
			if (fig.isVisible()) {
				fig = fig.findFigureAt(pt.x, pt.y, search);
				if (fig != null)
					return fig;
			}
		}
		//No descendants were found
		return null;
	}
	
	/**
	 * @see org.eclipse.draw2d.IFigure#findFigureAt(int, int, org.eclipse.draw2d.TreeSearch)
	 */
	public IFigure findFigureAt(int x, int y, TreeSearch search) {
		if (search.prune(this))
			return null;
		IFigure child = findDescendantAtExcluding(x, y, search);
		if (child != null)
			return child;
		return null;
	}
	
		
	
	/**
	 * Returns the deepest descendant for which {@link #isMouseEventTarget()} returns
	 * <code>true</code> or <code>null</code> if none found. The Parameters <i>x</i> and
	 * <i>y</i> are absolute locations. Any Graphics transformations applied by this Figure to
	 * its children during {@link #paintChildren(Graphics)} (thus causing the children to
	 * appear transformed to the user) should be applied inversely to the points <i>x</i> and
	 * <i>y</i> when called on the children.
	 * 
	 * @param x The X coordinate
	 * @param y The Y coordinate
	 * @return The deepest descendant for which isMouseEventTarget() returns true
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
	 * {@link #isMouseEventTarget()} returns <code>true</code> and returns that descendant or
	 * <code>null</code> if none found.
	 * @see #findMouseEventTargetAt(int, int)
	 * @param x The X coordinate
	 * @param y The Y coordiante
	 * @return The deepest descendant for which isMouseEventTarget() returns true
	 */
	protected IFigure findMouseEventTargetInDescendantsAt(int x, int y) {
		Point pt = new Point(x, y);
		this.translateFromParent(pt);
		
		IFigure fig;
		for (int i = getChildren().size(); i > 0;) {
			i--;
			fig = (IFigure)getChildren().get(i);
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

	
	/**
	 * Return the area of the parent viewport if there is one, otherwise, 
	 * return the client area of the parent.
	 */
	private Rectangle getParentRectangle()	{
		Rectangle rect = getParent().getParent().getClientArea().getCopy();
		
		IFigure port = getViewport();
		if( port != null )	{
			Rectangle portRect = port.getClientArea().getCopy();
			if( portRect.height != 0 && portRect.width != 0) {
				rect = portRect;
			}
		}
		return rect;
	}
	
	private IFigure getMainFigure( GatedFigure gf)	{
		GatedPaneFigure gpf = (GatedPaneFigure)gf.getParent();
		return gpf.getElementPane();
	}
	
	
	/**
	 * Helper to retrieve the viewport that this item sits on.
	 * @return the view port
	 */
	 private IFigure getViewport() {
	 	//
	 	// Start searching for viewport to clip on from the main figure.
	 	//
	 	IFigure fig = getMainFigure(this); 
		while (fig != null) {
			if (fig instanceof Viewport || fig instanceof ScalableFreeformLayeredPane )
				return  fig;
			fig = fig.getParent();
			if( fig instanceof GatedFigure ) {
				fig = getMainFigure((GatedFigure)fig);
			}
		}
		return null;
	}
	
	
	/**
	 * @see org.eclipse.draw2d.IFigure#erase()
	 */
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
		if(getChildren().isEmpty()) {	
			super.repaint();
		}
		else {	
			if (getParent() == null || !isVisible())
				return;
			Rectangle rectBounds = getParentRectangle();
			getParent().getParent().repaint(rectBounds);
			if ( getViewport() != null ) {
				getViewport().repaint(rectBounds);
			}
		}
	}
	
}
