/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.gef.ui.figures;

import java.util.Hashtable;
import java.util.Iterator;

import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PrecisionPoint;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.handles.HandleBounds;

import org.eclipse.gmf.runtime.draw2d.ui.internal.figures.TransparentBorder;

/**
 * Base class that most shape figures should extend from to gain default connection anchor behavior.
 * 
 * @author melaasar
 */
public class NodeFigure 
	extends Figure
		implements HandleBounds {

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

	/**
	 * Given a string identifier, return the associated anchor for that identifier
	 * 
	 * @param terminal <code>String</code> identifier associated with the anchor
	 * @return <code>ConnectionAnchor</code> that is associated with the given string.
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

	/**
	 * Dynamically allocates a new anchor if needed.  Otherwise, recycles old anchors
	 * no longer in use.
	 * 
	 * @param c the <code>ConnectionAnchor</code> reference to an anchor associated with the 
	 * given point on the figure
	 * @return a <code>String</code> that represents the anchor identifier.
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

	/** 
	 * Gets the source connection anchor at a given point on the figure.
	 * 
	 * @param p <code>Point</code> on the figure that gives a hint which anchor to return.
	 * @return a <code>ConnectionAnchor</code> reference to an anchor associated with the given 
	 * point on the figure.
	 */
	public ConnectionAnchor getSourceConnectionAnchorAt(Point p) {
		return createConnectionAnchor(p);
	}

	/** 
	 * Gets the target connection anchor at a given point on the figure.
	 * 
	 * @param p <code>Point</code> on the figure that gives a hint which anchor to return.
	 * @return <code>ConnectionAnchor</code> reference to an anchor associated with the 
	 * given point on the figure.
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
			PrecisionPoint pt = getAnchorRelativeLocation(temp);
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
	 * Calculates the relative location of the reference point with respect to the bounds
	 * of the figure. If point p is not inside of the figure's bounds then the point
	 * is mapped on the bounds and the point relative location is calculated 
	 * 
	 * @param p the <code>Point</code> that is relative coordinates of the point
	 * @return <Code>PrecisionPoint</Code>, i.e. the relative reference for
	 * <Code>SlidableAnchor</Code>
	 */
	protected PrecisionPoint getAnchorRelativeLocation(Point p) {
		PrecisionPoint relLocation;
		Point temp = new Point(p);
		if (p.x < getBounds().x || p.x > getBounds().x + getBounds().width
			|| p.y < getBounds().y || p.y > getBounds().y + getBounds().height) {
			if (p.x < getBounds().x || p.x > getBounds().x + getBounds().width) {
				temp.x = p.x < getBounds().x ? getBounds().x
					: getBounds().x + getBounds().width;
			}
			if (p.y < getBounds().y || p.y > getBounds().y + getBounds().height) {
				temp.y = p.y < getBounds().y ? getBounds().y
					: getBounds().y + getBounds().height;
			}
			relLocation = new PrecisionPoint((double) (temp.x - getBounds().x)
				/ getBounds().width, (double) (temp.y - getBounds().y)
				/ getBounds().height);
		} else {

		relLocation = new PrecisionPoint((double) (temp.x - getBounds().x)
				/ getBounds().width, (double) (temp.y - getBounds().y)
				/ getBounds().height);
		}
		return relLocation;
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
}
