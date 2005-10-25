/******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.gef.ui.figures;

import org.eclipse.draw2d.AbstractConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.PrecisionPoint;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.handles.HandleBounds;

import org.eclipse.gmf.runtime.common.core.util.StringStatics;
import org.eclipse.gmf.runtime.draw2d.ui.figures.IPolygonAnchorableFigure;
import org.eclipse.gmf.runtime.draw2d.ui.geometry.LineSeg;
import org.eclipse.gmf.runtime.draw2d.ui.geometry.PointListUtilities;


/**
 * Provides the implementation of Slidable anchor
 * 
 * @author oboyko
 *
 */
public class SlidableAnchor
	extends AbstractConnectionAnchor {
	
	final private static char TERMINAL_START_CHAR = '(';
	final private static char TERMINAL_DELIMITER_CHAR = ',';
	final private static char TERMINAL_END_CHAR = ')'; 

	// The connection anchor refrence point (sometimes the same as anchor location)
	private PrecisionPoint relativeReference;

	/**
	 * Empty constructor 
	 */
	public SlidableAnchor() {
		// empty constructor
	}
	
	/**
	 * Default constructor. The anchor will have the center of the figure as the
	 * reference point 
	 * 
	 * @param f <code>IFigure</code> that this anchor is associated with.
	 */
	public SlidableAnchor(IFigure f) {
		super(f);
	}

	/**
	 * Constructor. Takes point p to store the reference point 
	 * 
	 * @param f <code>IFigure</code> that this anchor is associated with.
	 * @param p the <code>PrecisionPoint</code> that the anchor will initially attach to.
	 */
	public SlidableAnchor(IFigure f, PrecisionPoint p) {
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
		Point p = new Point();
		if (getOwner() instanceof NodeFigure) {
			p = getAnchorPosition();
		}
		return p;
	}
	
	/**
	 * Creates a terminal string for any reference point passed in the format understandable by
	 * slidable anchors
	 * 
	 * @param p - a <Code>PrecisionPoint</Code> that must be represented as a unique
	 * <Code>String</Code>, namely as "(preciseX,preciseY)"
	 * @return <code>String</code> terminal composed from specified <code>PrecisionPoint</code>  
	 */
	public static String composeTerminalString(PrecisionPoint p) {
		StringBuffer s = new StringBuffer(24);
		s.append(TERMINAL_START_CHAR); 		// 1 char
		s.append((float)p.preciseX);		// 10 chars
		s.append(TERMINAL_DELIMITER_CHAR);	// 1 char
		s.append((float)p.preciseY);		// 10 chars
		s.append(TERMINAL_END_CHAR);		// 1 char
		return s.toString();				// 24 chars max (+1 for safety, i.e. for string termination)
	}

	/**
	 * From relative reference returns the relative coordinates of the anchor
	 * Method's visibility can be changed as needed
	 */
	private Point getAnchorPosition() {
		Rectangle rBox = getBox();
		if (isDefaultAnchor())
			return rBox.getCenter();
		return new Point(Math.round(relativeReference.preciseX * rBox.width
			+ rBox.x), Math.round(relativeReference.preciseY
			* rBox.height + rBox.y));
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
			Point location = PointListUtilities.pickClosestPoint(intersections, foreignReference);
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
		Point foreignReference = reference.getCopy();
		Point ownReference = getReferencePoint().getCopy();
		
		ownReference = normalizeToStraightlineTolerance(foreignReference, ownReference, STRAIGHT_LINE_TOLERANCE);
		
		Point location = getLocation(ownReference, foreignReference);
		if (location == null || 
			getBox().expand(1, 1).contains(foreignReference) &&
			!getBox().shrink(1, 1).contains(foreignReference))
			location = getLocation(getBox().getCenter(), foreignReference);
		
		if (location==null) {
			location = getBox().getCenter();
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
		Point normalizedReference = ownReference.getCopy();
		if (Math.abs(foreignReference.x - ownReference.x) < tolerance || 
			Math.abs(foreignReference.y - ownReference.y) < tolerance) {
			LineSeg lineSeg = new LineSeg(ownReference, foreignReference);
				
			normalizedReference = lineSeg.perpIntersect(ownReference.x, ownReference.y);
			
			// account for possible rounding errors and ensure the
			// resulting line is straight
			if (Math.abs(normalizedReference.x - foreignReference.x) < Math.abs(normalizedReference.y - foreignReference.y))
				normalizedReference.x = foreignReference.x;
			else
				normalizedReference.y = foreignReference.y;
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
		return (new LineSeg(ownReference,foreignReference)).getLineIntersectionsWithLineSegs(polygon);
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
			PointList polyList = ((IPolygonAnchorableFigure) getOwner()).getPolygonPoints();
			getOwner().translateToAbsolute(polyList);
			return polyList;
		}
		Rectangle rBox = getBox();
		PointList ptList = new PointList();
		ptList.addPoint(rBox.getTopLeft());
		ptList.addPoint(rBox.getTopRight());
		ptList.addPoint(rBox.getBottomRight());
		ptList.addPoint(rBox.getBottomLeft());
		ptList.addPoint(rBox.getTopLeft());
		return ptList;
	}

	/**
	 * Gets the anchors associated figure's bounding box in absolute coordinates.
	 * 
	 * @return a <code>Rectangle</code> that is the bounding box of the owner figure 
	 * in absolute coordinates
	 */
	protected Rectangle getBox() {
		Rectangle rBox = getOwner().getBounds().getCopy();
		if (getOwner() instanceof HandleBounds)
			rBox = ((HandleBounds)getOwner()).getHandleBounds().getCopy();
		
		getOwner().translateToAbsolute(rBox);
		return rBox;
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
			return new PrecisionPoint(Float.parseFloat(terminal.substring(
				terminal.indexOf(SlidableAnchor.TERMINAL_START_CHAR) + 1,
				terminal.indexOf(SlidableAnchor.TERMINAL_DELIMITER_CHAR))),
				Float.parseFloat(terminal.substring(terminal
					.indexOf(SlidableAnchor.TERMINAL_DELIMITER_CHAR) + 1,
					terminal.indexOf(SlidableAnchor.TERMINAL_END_CHAR))));
		} catch (Exception e) {
			return null;
		}
	}
	
}
