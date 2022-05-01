/******************************************************************************
 * Copyright (c) 2005, 2009 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/
package org.eclipse.gmf.runtime.diagram.ui.internal.figures;

import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gmf.runtime.draw2d.ui.geometry.LineSeg;
import org.eclipse.gmf.runtime.draw2d.ui.geometry.PointListUtilities;

/**
 * Helper class to convert the label coordinates from an offset value
 * from a keypoint to real draw2d coordinate
 * 
 * @author sshaw
 *
 */
public class LabelHelper {

	/**
	 * Calculates the label offset from the reference point given the label bounds.  
	 * 
	 * @param label the <code>IFigure</code> to calculate the offset for
	 * @param bounds the <code>Rectangle</code> that is the bounding box of the label.
	 * @param ref the <code>Point</code> that is the reference point that the offset
	 * is based on.
	 * @return a <code>Point</code> which represents a value offset from the <code>ref</code>
	 * point oriented based on the nearest line segment.
	 */
	static public Point offsetFromRelativeCoordinate(IFigure label, Rectangle bounds, Point ref) {
		return offsetFromRelativeCoordinate(label, bounds, getParentPointList(label), ref);
	}
	
	/**
	 * Calculates the label offset from the reference point given the label bounds and a points list.  
	 * 
	 * @param label the <code>IFigure</code> to calculate the offset for
	 * @param bounds the <code>Rectangle</code> that is the bounding box of the label.
	 * @param points the <code>PointList</code> that contains that the label offset is relative to.
	 * @param ref the <code>Point</code> that is the reference point that the offset
	 * is based on.
	 * @return a <code>Point</code> which represents a value offset from the <code>ref</code>
	 * point oriented based on the nearest line segment.
	 */
	static private Point offsetFromRelativeCoordinate(IFigure label, Rectangle bounds, PointList points, Point ref) {
		Rectangle rect = new Rectangle(bounds);
		
		//Componsate for the fact that we are using the 
		// figure center
		rect.translate(rect.width /2, rect.height /2);
		
		Point normalPoint = normalizeRelativePointToPointOnLine(points, ref,
				new Point(rect.x - ref.x, rect.y - ref.y));
		
		return normalPoint;
	}
	
	/**
	 * Calculates the relative coordinate that is equivalent to the offset from the reference 
	 * point, that can be used to set the label location.
	 * 
	 * @param label the <code>IFigure</code> to calculate the relative coordinate for 
	 * @param ref a <code>Point</code> located on the parent which the offset value
	 * is relative to.
	 * @param offset a <code>Point</code> which represents a value offset from the <code>ref</code>
	 * point oriented based on the nearest line segment.
	 * @return a <code>Point</code> that is the relative coordinate of the label that can be
	 * used to set it's location.
	 */
	static public Point relativeCoordinateFromOffset(IFigure label, Point ref, Point offset) {
		return relativeCoordinateFromOffset(label, getParentPointList(label), ref, offset);
	}
	
	/**
	 * Calculates the relative coordinate that is equivalent to the offset from the reference 
	 * point, that can be used to set the label location.
	 * 
	 * @param label the <code>IFigure</code> to calculate the relative coordinate for 
	 * @param points the <code>PointList</code> that contains that the label offset is relative to.
	 * @param ref a <code>Point</code> located on the parent which the offset value
	 * is relative to.
	 * @param offset a <code>Point</code> which represents a value offset from the <code>ref</code>
	 * point oriented based on the nearest line segment.
	 * @return a <code>Point</code> that is the relative coordinate of the label that can be
	 * used to set it's location.
	 */
	static private Point relativeCoordinateFromOffset(IFigure label, PointList points, Point ref, Point offset) {
		Point location = calculatePointRelativeToPointOnLine(points, ref, offset);
		location.translate(-1 * label.getBounds().width /2, -1 * label.getBounds().height /2);
		return location;
	}
	
	/**
	 * gets the point list using the passed figure to get the parent
	 * 
	 * @param label the <code>IFigure</code> to use to retrieve the parent points
	 * @return List of points
	 */
	static private PointList getParentPointList(IFigure label) {
		IFigure parent = label.getParent();
		if (parent instanceof Connection) {
			return ((Connection) parent).getPoints();
		} else {
			PointList ptList = new PointList();
			ptList.addPoint(parent.getBounds().getLocation());
			return ptList;
		}			
	}
	
	/**
	 * Returns a point located relative to the line by the given offset.
	 * 
	 * @param ptLst the point
	 * @param ptOnLine
	 * @param offset
	 * @return the relative point given the line angle
	 */
	protected static Point calculatePointRelativeToPointOnLine(PointList ptLst, Point ptOnLine, Point offset) {
		// Calculate slope of line
		if (ptLst.size() == 1) {
			// This is a node...
			return ptLst.getFirstPoint().getTranslated(offset);
		} else if (ptLst.size() >= 2){
			// This is a edge...
			int index = PointListUtilities.findNearestLineSegIndexOfPoint(ptLst, ptOnLine);	
			if (index < 1) {
				return ptLst.getFirstPoint().getTranslated(offset);
			}
			LineSeg segment = (LineSeg) PointListUtilities.getLineSegments(ptLst).get(index - 1);
			Point relativeOffset = null;
			if (segment != null) {
				if (segment.isHorizontal()) {
					if (segment.getOrigin().x > segment.getTerminus().x) {
						relativeOffset = ptOnLine.getTranslated(offset.getNegated());
						//System.out.println("1. Relative offset: " + relativeOffset);//$NON-NLS-1$
						return relativeOffset;
					} else {
						relativeOffset = ptOnLine.getTranslated(offset);	
						//System.out.println("2. Relative offset: " + relativeOffset);//$NON-NLS-1$
						return relativeOffset;
					}
				} else if (segment.isVertical()) {
					if (segment.getOrigin().y > segment.getTerminus().y) {
						relativeOffset = ptOnLine.getTranslated(offset.getCopy().scale(-1, 1).transpose());
						//System.out.println("3. Relative offset: " + relativeOffset);//$NON-NLS-1$
						return relativeOffset;						
					} else {
						relativeOffset = ptOnLine.getTranslated(offset.getCopy().scale(1, -1).transpose());
						//System.out.println("4. Relative offset: " + relativeOffset);//$NON-NLS-1$
						return relativeOffset;
					}
				} else {				
					double slope = segment.slope();
					double theta = Math.atan(slope);
					Point normalizedOffset = new Point(offset);
					Point calculatedOffset = new Point();
					if (segment.getOrigin().x > segment.getTerminus().x) {
						normalizedOffset = offset.getCopy().scale(-1, -1);
					}

					calculatedOffset = new Point(normalizedOffset.x
						* Math.cos(theta) - normalizedOffset.y
						* Math.sin(theta), normalizedOffset.x * Math.sin(theta)
						+ normalizedOffset.y * Math.cos(theta));
					relativeOffset = ptOnLine.getTranslated(calculatedOffset);
					//System.out.println("5. Relative offset: " + relativeOffset);//$NON-NLS-1$
					return relativeOffset;									
				}
			}
		}					
		return null;
	}
	
	/**
	 * Calculates the normalized offset from a point on a <code>Connection</code>'s point list to an point.
	 * 
	 * @param ptLst
	 * @param ptOnLine
	 * @param offset
	 * @return the normalized offset
	 */
	private static Point normalizeRelativePointToPointOnLine(PointList ptLst, Point ptOnLine, Point offset) {
		// Calculate slope of line
		if (ptLst.size() == 1) {
			// This is a node...
			return offset;
		} else if (ptLst.size() >= 2){
			// This is a edge...
			int index = PointListUtilities.findNearestLineSegIndexOfPoint(ptLst, ptOnLine);		
			LineSeg segment = (LineSeg) PointListUtilities.getLineSegments(ptLst).get(index - 1);
			Point normalOffset = null;
			if (segment != null) {
				if (segment.isHorizontal()) {
					if (segment.getOrigin().x > segment.getTerminus().x) {
						normalOffset = offset.getNegated();
						//System.out.println("1. Normal offset: " + normalOffset);//$NON-NLS-1$
						return normalOffset;
					} else {
						normalOffset = offset;
						//System.out.println("2. Normal offset: " + normalOffset);//$NON-NLS-1$
						return normalOffset;						
					}					
				} else if (segment.isVertical()) {
					if (segment.getOrigin().y < segment.getTerminus().y) {
						normalOffset = offset.scale(-1, 1).transpose();
						//System.out.println("3. Normal offset: " + normalOffset);//$NON-NLS-1$
						return normalOffset;
					} else {
						normalOffset = offset.scale(1, -1).transpose();
						//System.out.println("4. Normal offset: " + normalOffset);//$NON-NLS-1$						
						return normalOffset;
					}
				} else {				
					Point p = ptOnLine.getTranslated(offset);
					normalOffset = getOrthogonalDistances(segment, ptOnLine, p);	
					//System.out.println("5. Normal offset: " + normalOffset);//$NON-NLS-1$
					return normalOffset;										
				}
			}
		}					
		return null;

	}
	
	/**
	 * Calculates distances from a <code>Point</code> on a <code>LineSeg</code> to 
	 * another <code>Point</code>.  The sign of the distances indicate direction.
	 * 
	 * @param lineSeg
	 * @param ptOnLine
	 * @param refPoint
	 * @return the distance from <code>Point</code> on a <code>LineSeg</code> to another <code>Point</code>
	 */
	private static Point getOrthogonalDistances(LineSeg lineSeg, Point ptOnLine, Point refPoint) {
		LineSeg parallelSeg = lineSeg.getParallelLineSegThroughPoint(refPoint);
		Point p1 = parallelSeg.perpIntersect(ptOnLine.x, ptOnLine.y);
		double dx = p1.getDistance(refPoint) * ((p1.x > refPoint.x) ? -1 : 1);
		double dy = p1.getDistance(ptOnLine) * ((p1.y < ptOnLine.y) ? -1 : 1);
		Point orth = new Point(dx, dy);	
		// Reflection in the y axis		
		if (lineSeg.getOrigin().x > lineSeg.getTerminus().x)
			orth = orth.scale(-1, -1);
		return orth;
	}
}
