/******************************************************************************
 * Copyright (c) 2006, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 ****************************************************************************/

package org.eclipse.gmf.runtime.draw2d.ui.figures;

import org.eclipse.draw2d.AbstractConnectionAnchor;
import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.PrecisionPoint;
import org.eclipse.draw2d.geometry.PrecisionRectangle;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gmf.runtime.common.core.util.StringStatics;
import org.eclipse.gmf.runtime.draw2d.ui.geometry.LineSeg;
import org.eclipse.gmf.runtime.draw2d.ui.geometry.PointListUtilities;
import org.eclipse.gmf.runtime.draw2d.ui.geometry.PrecisionPointList;


/**
 * Provides the implementation of Slidable anchor
 * 
 * @author oboyko / sshaw
 *
 */
public class BaseSlidableAnchor
	extends AbstractConnectionAnchor implements OrthogonalConnectionAnchor {
	
	final private static char TERMINAL_START_CHAR = '(';
	final private static char TERMINAL_DELIMITER_CHAR = ',';
	final private static char TERMINAL_END_CHAR = ')'; 

	// The connection anchor reference point (sometimes the same as anchor location)
	private PrecisionPoint relativeReference;

	/**
	 * Empty constructor 
	 */
	public BaseSlidableAnchor() {
		// empty constructor
	}
	
	/**
	 * Default constructor. The anchor will have the center of the figure as the
	 * reference point 
	 * 
	 * @param f <code>IFigure</code> that this anchor is associated with.
	 */
	public BaseSlidableAnchor(IFigure f) {
		super(f);
	}

	/**
	 * Constructor. Takes point p to store the reference point 
	 * 
	 * @param f <code>IFigure</code> that this anchor is associated with.
	 * @param p the <code>PrecisionPoint</code> that the anchor will initially attach to.
	 */
	public BaseSlidableAnchor(IFigure f, PrecisionPoint p) {
		super(f);
		this.relativeReference = new PrecisionPoint(p.preciseX, p.preciseY);
	}
	
	/**
	 * Creates terminal string for slidable anchor
	 * 
	 * @return <code>String</code> terminal for slidable anchor
	 */
	public String getTerminal() {
		if (isDefaultAnchor())
			return StringStatics.BLANK;
		return composeTerminalString(relativeReference);
	}

	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.draw2d.ConnectionAnchor#getReferencePoint()
	 */
	public Point getReferencePoint() {
		return getAnchorPosition();
	}
	
	/**
	 * Creates a terminal string for any reference point passed in the format understandable by
	 * slidable anchors
	 * 
	 * @param p - a <Code>PrecisionPoint</Code> that must be represented as a unique
	 * <Code>String</Code>, namely as "(preciseX,preciseY)"
	 * @return <code>String</code> terminal composed from specified <code>PrecisionPoint</code>  
	 */
	private String composeTerminalString(PrecisionPoint p) {
		StringBuffer s = new StringBuffer(24);
		s.append(TERMINAL_START_CHAR); 		// 1 char
		s.append(p.preciseX);		// 10 chars
		s.append(TERMINAL_DELIMITER_CHAR);	// 1 char
		s.append(p.preciseY);		// 10 chars
		s.append(TERMINAL_END_CHAR);		// 1 char
		return s.toString();				// 24 chars max (+1 for safety, i.e. for string termination)
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (obj instanceof BaseSlidableAnchor) {
			BaseSlidableAnchor anchor = (BaseSlidableAnchor) obj;
			/*
			 * Owning figures must be identical to satisfy equality of anchors
			 */
			if (getOwner() == anchor.getOwner()) {
				if (isDefaultAnchor()) {
					return anchor.isDefaultAnchor();
				}
				return relativeReference.equals(anchor.relativeReference);
			}
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		int figureHashCode = getOwner() != null ? getOwner().hashCode() : 0; 
		if (relativeReference == null) {
			return figureHashCode;
		}
		return new Double(relativeReference.preciseX()).hashCode()
				^ new Double(relativeReference.preciseY()).hashCode()
				^ figureHashCode;
	}

	/**
	 * From relative reference returns the relative coordinates of the anchor
	 * Method's visibility can be changed as needed
	 */
	private Point getAnchorPosition() {
		PrecisionRectangle rBox = new PrecisionRectangle(getBox());
		if (isDefaultAnchor())
			return rBox.getCenter();
		return new PrecisionPoint(relativeReference.preciseX * rBox.preciseWidth
				+ rBox.preciseX, relativeReference.preciseY * rBox.preciseHeight
				+ rBox.preciseY);
	}
	
	/**
	 * Calculates the location of the anchor depending on the anchors own reference
	 * and foreign reference points
	 * 
	 * @param ownReference - the own reference of the anchor
	 * @param foreignReference - foreign reference that comes in 
	 * @return the location of the anchor depending on the anchors own reference
	 * and foreign reference points
	 */
	protected Point getLocation(Point ownReference, Point foreignReference) {
		PointList intersections = getIntersectionPoints(ownReference, foreignReference);
		if (intersections!=null && intersections.size()!=0) {
			Point location = PointListUtilities.pickClosestPoint(intersections,
					((getBox().contains(foreignReference)) ? ownReference
							: foreignReference));
			return location;
		}
		return null;
	}

	static private int STRAIGHT_LINE_TOLERANCE = 10;

	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.draw2d.ConnectionAnchor#getLocation(org.eclipse.draw2d.geometry.Point)
	 */
	public Point getLocation(Point reference) {
		Point ownReference = normalizeToStraightlineTolerance(reference, getReferencePoint(), STRAIGHT_LINE_TOLERANCE);
		
		Point location = getLocation(ownReference, reference);
		if (location == null) {
			location = getLocation(new PrecisionPoint(getBox().getCenter()), reference);
			if (location == null) {
				location = getBox().getCenter();
			}
		}
		
		return location;
	}

	
	/**
	 * Returns a new owned reference point that is normalized to be with-in a straight-line 
	 * tolerance value.
	 * 
	 * @param foreignReference <code>Point</code> that is the foreign reference point used to calculate
	 * the interfection anchor point on the shape in absolute coordinates.
	 * @param ownReference <code>Point</code> that is the reference point with-in the shape in 
	 * absolute coordinates
	 * @param tolerance <code>int</code> value that is the difference in absolute coordinates where the
	 * two points would be considered straight and then adjusted.
	 * @return <code>Point</code> that is the normalized owned reference to be with-in a given
	 * straight-line tolerance value of the foreign reference point.
	 */
	protected Point normalizeToStraightlineTolerance(Point foreignReference, Point ownReference, int tolerance) {
		PrecisionPoint preciseOwnReference = new PrecisionPoint(ownReference);
		PrecisionPoint normalizedReference = (PrecisionPoint)preciseOwnReference.getCopy();
		PrecisionPoint preciseForeignReference = new PrecisionPoint(foreignReference);
		if (Math.abs(preciseForeignReference.preciseX - preciseOwnReference.preciseX) < tolerance) {
			normalizedReference.preciseX = preciseForeignReference.preciseX;
			normalizedReference.updateInts();
			return normalizedReference;
		}
		if (Math.abs(preciseForeignReference.preciseY - preciseOwnReference.preciseY) < tolerance) {
			normalizedReference.preciseY = preciseForeignReference.preciseY;
			normalizedReference.updateInts();
		}
		return normalizedReference;
	}

	/**
	 * Calculates intersection points of the figure and the line that passes through 
	 * ownReference and foreignReference points
	 * 
	 * @param ownReference the reference <code>Point</code> on or inside the shape that is being
	 * anchored to.
	 * @param foreignReference the outside reference <code>Point</code> point that is the terminal
	 * end of the line formed by the two parameters.
	 * @return intersection points of the figure and the line that passes through 
	 * ownReference and foreignReference points
	 */
	protected PointList getIntersectionPoints(Point ownReference, Point foreignReference) {
		final PointList polygon = getPolygonPoints();
		return (new LineSeg(ownReference, foreignReference)).getLineIntersectionsWithLineSegs(polygon); 
	}

	
	/**
	 * Returns the list of all the vertices of the figure.
	 * The created list must form a polygon, i.e. closed polyline, for figures
	 * hence the starting and ending points must be the same
	 * 
	 * @return the <code>PointList</code> list of all the vertices of the figure.
	 */
	protected PointList getPolygonPoints() {
		if (getOwner() instanceof IPolygonAnchorableFigure) {
			PrecisionPointList polyList = new PrecisionPointList(((IPolygonAnchorableFigure) getOwner()).getPolygonPoints());
			getOwner().translateToAbsolute(polyList);
			return polyList;
		}
		PrecisionRectangle r = new PrecisionRectangle(getBox());
		PrecisionPointList ptList = new PrecisionPointList(5);
		ptList.addPoint(new PrecisionPoint(r.preciseX, r.preciseY));
		ptList.addPoint(new PrecisionPoint(r.preciseX + r.preciseWidth, r.preciseY));
		ptList.addPoint(new PrecisionPoint(r.preciseX + r.preciseWidth, r.preciseY + r.preciseHeight));
		ptList.addPoint(new PrecisionPoint(r.preciseX, r.preciseY + r.preciseHeight));
		ptList.addPoint(new PrecisionPoint(r.preciseX, r.preciseY));
		return ptList;
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
	static public PrecisionPoint getAnchorRelativeLocation(Point p, Rectangle bounds) {
		if (bounds.width == 0 || bounds.height == 0) {
			/*
			 * If figure hasn't been laid out yet, we don't want to fail the slidable anchor creation.
			 * Hence, we'll just return the (0.5, 0.5) meaning that the anchor reference point is the center of the figure.
			 */
			return new PrecisionPoint(0.5, 0.5);
		}
		PrecisionPoint relLocation;
		PrecisionPoint temp = new PrecisionPoint(p);
		if (p.x < bounds.x || p.x > bounds.x + bounds.width
			|| p.y < bounds.y || p.y > bounds.y + bounds.height) {
			if (p.x < bounds.x || p.x > bounds.x + bounds.width) {
				temp.preciseX = p.x < bounds.x ? bounds.x
					: bounds.x + bounds.width;
			}
			if (p.y < bounds.y || p.y > bounds.y + bounds.height) {
				temp.preciseY = p.y < bounds.y ? bounds.y
					: bounds.y + bounds.height;
			}
			relLocation = new PrecisionPoint((temp.preciseX - bounds.x)
				/ bounds.width, (temp.preciseY - bounds.y)
				/ bounds.height);
		} else {

		relLocation = new PrecisionPoint((temp.preciseX - bounds.x)
				/ bounds.width, (temp.preciseY - bounds.y)
				/ bounds.height);
		}
		return relLocation;
	}
	
	/**
	 * Gets the anchors associated figure's bounding box in absolute coordinates.
	 * 
	 * @return a <code>Rectangle</code> that is the bounding box of the owner figure 
	 * in absolute coordinates
	 */
	protected Rectangle getBox() {
        Rectangle rBox = (getOwner() instanceof Connection) ? ((Connection) getOwner())
            .getPoints().getBounds()
            : getOwner().getBounds();
        PrecisionRectangle box = new PrecisionRectangle(rBox);
        getOwner().translateToAbsolute(box);
        return box;
	}
	
	/**
	 * Returns true if the <Code>SlidableAnchor</Code> is default one with a reference at the center
	 * 
	 * @return <code>boolean</code> <code>true</code> is the <code>SlidableAnchor</code> is default one, <code>false</code> otherwise
	 */
	public boolean isDefaultAnchor() {
		return relativeReference == null;
	}
	
	/**
	 * Parses anchors terminal string and returns the relative reference icorporated
	 * in the terminal string
	 * 
	 * @param terminal - the terminal string containing relative reference
	 * @return returns the relative reference incorporated in the terminal string
	 */
	public static PrecisionPoint parseTerminalString(String terminal) {
		try {
			return new PrecisionPoint(Double.parseDouble(terminal.substring(
				terminal.indexOf(BaseSlidableAnchor.TERMINAL_START_CHAR) + 1,
				terminal.indexOf(BaseSlidableAnchor.TERMINAL_DELIMITER_CHAR))),
				Double.parseDouble(terminal.substring(terminal
					.indexOf(BaseSlidableAnchor.TERMINAL_DELIMITER_CHAR) + 1,
					terminal.indexOf(BaseSlidableAnchor.TERMINAL_END_CHAR))));
		} catch (Exception e) {
			return null;
		}
	}
	
	public Point getOrthogonalLocation(Point orthoReference) {
		PrecisionPoint ownReference = new PrecisionPoint(getReferencePoint());
//		PrecisionRectangle bounds = new PrecisionRectangle(getBox());
		PrecisionRectangle bounds = new PrecisionRectangle(FigureUtilities.getAnchorableFigureBounds(getOwner()));
		getOwner().translateToAbsolute(bounds);
		bounds.expand(0.000001, 0.000001);
		PrecisionPoint preciseOrthoReference = new PrecisionPoint(orthoReference);
		int orientation = PositionConstants.NONE;
		if (preciseOrthoReference.preciseX >= bounds.preciseX && preciseOrthoReference.preciseX <= bounds.preciseX + bounds.preciseWidth) {
			ownReference.preciseX = preciseOrthoReference.preciseX;
			orientation = PositionConstants.VERTICAL;
		} else if (preciseOrthoReference.preciseY >= bounds.preciseY && preciseOrthoReference.preciseY <= bounds.preciseY + bounds.preciseHeight) {
			ownReference.preciseY = preciseOrthoReference.preciseY;
			orientation = PositionConstants.HORIZONTAL;
		}
		ownReference.updateInts();
		
		Point location = getLocation(ownReference, preciseOrthoReference);
		if (location == null) {
			location = getLocation(orthoReference);
			orientation = PositionConstants.NONE;
		}
		
		if (orientation != PositionConstants.NONE) {
			PrecisionPoint loc = new PrecisionPoint(location);
			if (orientation == PositionConstants.VERTICAL) {
				loc.preciseX = preciseOrthoReference.preciseX;
			} else {
				loc.preciseY = preciseOrthoReference.preciseY;
			}
			loc.updateInts();
			location = loc;
		}
		
		return location;
	}

}
