/******************************************************************************
 * Copyright (c) 2002, 2003, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.gef.ui.figures;

import java.util.Hashtable;
import java.util.Iterator;

import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.PrecisionPoint;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.handles.HandleBounds;
import org.eclipse.gmf.runtime.draw2d.ui.figures.BaseSlidableAnchor;
import org.eclipse.gmf.runtime.draw2d.ui.figures.IAnchorableFigure;
import org.eclipse.gmf.runtime.draw2d.ui.figures.IPolygonAnchorableFigure;
import org.eclipse.gmf.runtime.draw2d.ui.internal.figures.TransparentBorder;

/**
 * Base class that most shape figures should extend from to gain default connection anchor behavior.
 * 
 * @author melaasar
 */
public class NodeFigure 
	extends Figure
		implements HandleBounds, IAnchorableFigure, IPolygonAnchorableFigure {

	private Hashtable connectionAnchors;
	
	/**
	 * <code>String</code> that is the identifier for the default anchor
	 */
	static public final String szAnchor = ""; //$NON-NLS-1$

	/** 
	 * Constructor - sets the default colors for all node figures.
	 */
	public NodeFigure() {
		// empty constructor
	}

	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.gef.handles.HandleBounds#getHandleBounds()
	 */
	public Rectangle getHandleBounds() {
		Insets insets = new Insets(0, 0, 0, 0);
		if (getBorder() instanceof TransparentBorder) {
			insets =
				((TransparentBorder) getBorder()).getTransparentInsets(this);
		}
        
		// Ignore the insets when placing the handles
		return new Rectangle(
				getBounds().x + insets.left,
				getBounds().y + insets.top,
				getBounds().width - (insets.right + insets.left),
				getBounds().height - (insets.bottom + insets.top));
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.draw2d.ui.figures.IAnchorableFigure#getConnectionAnchor(java.lang.String)
	 */
	public ConnectionAnchor getConnectionAnchor(String terminal) {

		ConnectionAnchor connectAnchor =
			(ConnectionAnchor) getConnectionAnchors().get(terminal);
		if (connectAnchor == null) {
			if (terminal.equals(szAnchor)) {
				// get a new one - this figure doesn't support static anchors
				connectAnchor = createDefaultAnchor();
				getConnectionAnchors().put(terminal,connectAnchor);
			}
			else {
				connectAnchor = createAnchor(SlidableAnchor.parseTerminalString(terminal));
			}
		}

		return connectAnchor;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.draw2d.ui.figures.IAnchorableFigure#getConnectionAnchorTerminal(org.eclipse.draw2d.ConnectionAnchor)
	 */
	public String getConnectionAnchorTerminal(ConnectionAnchor c) {
		if (c instanceof SlidableAnchor) {
			return ((SlidableAnchor) c).getTerminal();
		}
		if (getConnectionAnchors().containsValue(c)) {
			Iterator iter = getConnectionAnchors().keySet().iterator();
			String key;
			while (iter.hasNext()) {
				key = (String) iter.next();
				if (getConnectionAnchors().get(key).equals(c))
					return key;
			}
		}
		getConnectionAnchor(szAnchor);
		return szAnchor;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.draw2d.ui.figures.IAnchorableFigure#getSourceConnectionAnchorAt(org.eclipse.draw2d.geometry.Point)
	 */
	public ConnectionAnchor getSourceConnectionAnchorAt(Point p) {
		return createConnectionAnchor(p);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.draw2d.ui.figures.IAnchorableFigure#getTargetConnectionAnchorAt(org.eclipse.draw2d.geometry.Point)
	 */
	public ConnectionAnchor getTargetConnectionAnchorAt(Point p) {
		return createConnectionAnchor(p);
	}

	/**
	 * Returns a new anchor for this node figure.
	 * 
	 * @param p <code>Point</code> on the figure that gives a hint which anchor to return.
	 * @return <code>ConnectionAnchor</code> reference to an anchor associated with the 
	 * given point on the figure.
	 */
	protected ConnectionAnchor createConnectionAnchor(Point p) {
		if (p == null) {
			return getConnectionAnchor(szAnchor);
		}
		else {
			Point temp = p.getCopy();
			translateToRelative(temp);
			PrecisionPoint pt = BaseSlidableAnchor.getAnchorRelativeLocation(temp, getBounds());
			if (isDefaultAnchorArea(pt))
				return getConnectionAnchor(szAnchor);
			return createAnchor(pt);
		}
	} 
	
	/**
	 * Checks whether the <PrecisionPoint> p which is a candidate for a relative reference
	 * for the <Code>SlidableAnchor</Code> belongs to the area where the default anchor
	 * must be created
	 * 
	 * @param p
	 * @return <code>boolean</code> <code>true</code> if <PrecisionPoint> belongs to the area where the default anchor must be 
	 * created, <code>false</code> otherwise
	 */
	protected boolean isDefaultAnchorArea(PrecisionPoint p) {
		return p.preciseX >= getSlidableAnchorArea()/2 && p.preciseX <= 1 - getSlidableAnchorArea()/2 &&
			p.preciseY >= getSlidableAnchorArea()/2 && p.preciseY <= 1 - getSlidableAnchorArea()/2;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.draw2d.Figure#paintFigure(org.eclipse.draw2d.Graphics)
	 */
	protected void paintFigure(Graphics graphics) {
		if (isOpaque() && getBorder() != null) {
			Rectangle tempRect = new Rectangle(getBounds());
			tempRect.crop(getBorder().getInsets(this));
			graphics.fillRectangle(tempRect);
			return;
		}

		super.paintFigure(graphics);
	}
	

	/**
	 * Returns the connectionAnchors.
	 * @return Hashtable
	 */
	protected Hashtable getConnectionAnchors() {
		if (connectionAnchors == null)
			connectionAnchors = new Hashtable(1);
		return connectionAnchors;
	}
		

	/**
	 * Specifies how large the area of the figure's bounds where <Code>SlidableAnchor</Code>
	 * will be created. The result number: 0<=result<=1
	 * 
	 * @return  the size of the area of the figure's bounds
	 */
	protected double getSlidableAnchorArea() {
		return 0.5;
	}
	
	/**
	 * Creates the default Slidable anchor with a reference point at the center
	 * of the figure's bounds
	 * 
	 * @return - default SlidableAnchor, relative reference the center of the figure
	 */
	protected ConnectionAnchor createDefaultAnchor() {
		return new SlidableAnchor(this);
	}
	
	/**
	 * Creates a slidable anchor at the specified point (from the ratio of the
	 * reference's coordinates and bounds of the figure
	 * 
	 * @param p - relative reference for the <Code>SlidableAnchor</Code>
	 * @return a <code>SlidableAnchor</code> for this figure with relative reference at p
	 */
	protected ConnectionAnchor createAnchor(PrecisionPoint p) {
		if (p==null)
			// If the old terminal for the connection anchor cannot be resolved (by SlidableAnchor) a null
			// PrecisionPoint will passed in - this is handled here
			return createDefaultAnchor();
		return new SlidableAnchor(this, p);
	}

	/**
	 * @return <code>String</code> that is the identifier for the default anchor
	 */
	public static String getDefaultAnchorID() {
		return szAnchor;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.draw2d.ui.figures.IPolygonAnchorableFigure#getPolygonPoints()
	 */
	public PointList getPolygonPoints() {
		PointList points = new PointList(5);
		Rectangle anchorableRectangle = getHandleBounds();
		points.addPoint(anchorableRectangle.x, anchorableRectangle.y);
		points.addPoint(anchorableRectangle.x + anchorableRectangle.width,
				anchorableRectangle.y);
		points.addPoint(anchorableRectangle.x + anchorableRectangle.width,
				anchorableRectangle.y + anchorableRectangle.height);
		points.addPoint(anchorableRectangle.x, anchorableRectangle.y
				+ anchorableRectangle.height);
		points.addPoint(anchorableRectangle.x, anchorableRectangle.y);
		return points;
	}	
	
}
