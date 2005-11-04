/******************************************************************************
 * Copyright (c) 2002, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.draw2d.ui.internal.routers;

import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Ray;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gmf.runtime.draw2d.ui.geometry.LineSeg;
import org.eclipse.gmf.runtime.draw2d.ui.geometry.PointListUtilities;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.MapModeUtil;


/**
 * @author sshaw
 * @canBeSeenBy org.eclipse.gmf.runtime.draw2d.ui.*
 *
 * RectilinearRouter which routes the Connector so that the lines
 * are always vertical or horizontal.
 */
public class RectilinearRouter extends ObliqueRouter implements OrthogonalRouter {

	/**
     * removeSegmentsInViews
     * This method will parse through all the line segments in the given 
     * polyline and remove any of the segments that intersect with the 
     * start and end figures.
     * 
	 * @param conn Connection figure that is used to access anchors
	 * @param newLine PointList that will be modified by the routine.
	 * @return boolean true if method change newLine PointList, false otherwise
	 */
	protected boolean removeSegmentsInViews(Connection conn, PointList newLine) {
		// Ignore the first and last points
		PointList newPoints = new PointList(newLine.size());
		Point ptStart = new Point(newLine.getFirstPoint());
		Point ptEnd = new Point(newLine.getLastPoint());
		for (int i = 0; i < newLine.size(); i++) {
			if (i != 0 && i != newLine.size() - 1)
				newPoints.addPoint(new Point(newLine.getPoint(i)));
		}

		if (newPoints.size() < 3)
			return false;

		int lastIntersect = 0;
		int count = 0;
		boolean found = false;
        boolean bChanged = false;

		if (conn.getSourceAnchor().getOwner() == null)
			return false;

		Rectangle startRect =
			new Rectangle(conn.getSourceAnchor().getOwner().getBounds());
		conn.getSourceAnchor().getOwner().translateToAbsolute(startRect);
		conn.translateToRelative(startRect);

		for (int i = 0; i < newPoints.size() - 1; i++) {
			boolean in1 = startRect.contains(newPoints.getPoint(i));
			boolean in2 = startRect.contains(newPoints.getPoint(i + 1));
			if (in1 != in2) {
				lastIntersect = count;
				found = true;
			} else if (!(in1 || in2)) // Neither intersect, so skip out
				{
				break;
			}
			++count;
		}

		// remove segments before the one that finally
		// intersects:
		if (found) {
			for (int i = 0; i <= lastIntersect; ++i) {
				newPoints.removePoint(0);
                bChanged = true;
            }
		}

		lastIntersect = count = newLine.size() - 1;
		found = false;

		if (conn.getTargetAnchor().getOwner() == null)
			return false;

		Rectangle endRect =
			new Rectangle(conn.getTargetAnchor().getOwner().getBounds());
		conn.getTargetAnchor().getOwner().translateToAbsolute(endRect);
		conn.translateToRelative(endRect);

		for (int i = newPoints.size() - 1; i > 0; i--) {
			boolean in1 = endRect.contains(newPoints.getPoint(i));
			boolean in2 = endRect.contains(newPoints.getPoint(i - 1));
			if (in1 != in2) {
				lastIntersect = count;
				found = true;
			} else if (!(in1 || in2)) // Neither intersect, so skip out
				{
				break;
			}
			--count;
		}

		// remove segments after the one that finally
		// intersects:
		if (found) {
			for (int i = newPoints.size() - 1; i >= lastIntersect; --i) {
				newPoints.removePoint(newPoints.size() - 1);
                bChanged = true;
            }
		}

		if (newPoints.size() != newLine.size()) {
			newLine.removeAllPoints();
			newLine.addPoint(ptStart);
			for (int i = 0; i < newPoints.size(); i++)
				newLine.addPoint(new Point(newPoints.getPoint(i)));
			newLine.addPoint(ptEnd);
		}
        
        return bChanged;
	}
    
	/**
     * updateToBiTerminal
     * Determines if the polyline has only two bendpoints (endpoints) and if so,
     * updates the connection to be consistent with the Rectilinear router - i.e.
     * vertical or horizontal alignment.
     * 
	 * @param conn Connection that is being routed.
	 * @param newLine PointList to be checked and modified if bi-terminal routing is
     * possible
	 * @return true if PointList is a candidate for bi-terminal routing, false otherwise
	 */
	protected boolean updateToBiTerminal(Connection conn, PointList newLine) {
		boolean retVal = false;

		if (newLine.size() == 2) {
			Point ptOrig = new Point(newLine.getPoint(0));
			Point ptTerm = new Point(newLine.getPoint(1));

			Dimension offsets = new Dimension(10, 10);
			conn.translateToRelative(offsets);
			
			Rectangle bBoxF, bBoxT;
			if (conn.getSourceAnchor().getOwner() != null) {
				bBoxF =
					new Rectangle(
						conn.getSourceAnchor().getOwner().getBounds());
				conn.getSourceAnchor().getOwner().translateToAbsolute(bBoxF);
				conn.translateToRelative(bBoxF);
			} else
				bBoxF = new Rectangle(ptOrig.x - offsets.width / 2, ptOrig.y - offsets.height / 2, 
											offsets.width, offsets.height);

			if (conn.getTargetAnchor().getOwner() != null) {
				bBoxT =
					new Rectangle(
						conn.getTargetAnchor().getOwner().getBounds());
				conn.getTargetAnchor().getOwner().translateToAbsolute(bBoxT);
				conn.translateToRelative(bBoxT);
			} else
				bBoxT = new Rectangle(ptTerm.x - offsets.width / 2, ptTerm.y - offsets.height / 2, 
											offsets.width, offsets.height);

			int ix1 = Math.max(bBoxF.getLeft().x, bBoxT.getLeft().x);
			int ix2 = Math.min(bBoxF.getRight().x, bBoxT.getRight().x);

			Point posF = bBoxF.getCenter();
			Point posT = bBoxT.getCenter();

			Ray origSeg = new Ray(ptOrig, ptTerm);
			boolean isOblique = (origSeg.y != 0 && origSeg.x != 0);

			if (ix1 <= ix2) {
				// The two boundboxes overlap each other so we can create a single
				// segment that goes between them, but only if we have a nonrectilinear line
				// or the existing segment is already is routed between the two icons
				if (isOblique || ptOrig.x < ix1 || ptOrig.x > ix2) {
					if (isOblique && ptOrig.x > ix1 && ptOrig.x < ix2) 
						posF.x = ptOrig.x;
					else
						posF.x = ix1 + (ix2 - ix1) / 2;
					
					posT.x = posF.x;

					newLine.removeAllPoints();
					newLine.addPoint(posF);
					newLine.addPoint(posT);
					retVal = true;
				}
			} else {
				int iy1 = Math.max(bBoxF.getTop().y, bBoxT.getTop().y);
				int iy2 = Math.min(bBoxF.getBottom().y, bBoxT.getBottom().y);
				if (iy1 <= iy2) {
					// The two boundboxes overlap each other so we can create a single
					// segment that goes between them, but only if we have a nonrectilinear line
					// or the existing segment is already is routed between the two icons
					if (isOblique || ptOrig.y < iy1 || ptOrig.y > iy2) {
						if (isOblique && ptOrig.y > iy1 && ptOrig.y < iy2) 
							posF.y = ptOrig.y;
						else
							posF.y = iy1 + (iy2 - iy1) / 2;
						posT.y = posF.y;

						newLine.removeAllPoints();
						newLine.addPoint(posF);
						newLine.addPoint(posT);
						retVal = true;
					}
				}
			}
		}

		return retVal;
	}

	/* 
     * resetEndPointsToEdge
     * Resets both of the end points in the polyline to be anchored properly on the
     * edge of the start and end figures.
     * 
	 * @see com.ibm.xtools.draw2d.ObliqueRouter#resetEndPointsToEdge(org.eclipse.draw2d.Connection, org.eclipse.draw2d.geometry.PointList)
	 */
	protected void resetEndPointsToEdge(Connection conn, PointList newLine) {

		// if we are reorienting, then just default to the super class implementation and
		// don't try to do rectilinear routing.
		if (isReorienting(conn)) {
			super.resetEndPointsToEdge(conn, newLine);
			return;
		}
 
		LineSeg edgeLine1 =
			OrthogonalRouterUtilities.getOrthogonalLineSegToAnchorLoc(conn, conn.getSourceAnchor(), newLine.getPoint(1));
        
		LineSeg edgeLine2 =
			OrthogonalRouterUtilities.getOrthogonalLineSegToAnchorLoc(conn, conn.getTargetAnchor(), newLine.getPoint(newLine.size() - 2));

		if (edgeLine1 != null && edgeLine2 != null) {
			newLine.setPoint(edgeLine1.getOrigin(), 0);
			if (newLine.size() > 2) {
				for (int i=0; i<2; i++) {
		            Point ptCurrent = newLine.getPoint(i);
		            Point ptNext = newLine.getPoint(i+1);
		            makeOrthogonal(ptCurrent, ptNext);
		            
		            newLine.setPoint(ptNext, i+1);
		        }
			}
			
			newLine.setPoint(edgeLine2.getOrigin(), newLine.size() - 1);
			if (newLine.size() > 2) {
				for (int i=newLine.size() - 1; i>=newLine.size() - 2; i--) {
		            Point ptCurrent = newLine.getPoint(i);
		            Point ptNext = newLine.getPoint(i-1);
		            makeOrthogonal(ptCurrent, ptNext);
		            
		            newLine.setPoint(ptNext, i-1);
		        }
			}
		} else
			super.resetEndPointsToEdge(conn, newLine);
	}

	private void makeOrthogonal(Point ptCurrent, Point ptNext) {
		if (Math.abs(ptNext.x - ptCurrent.x) < Math.abs(ptNext.y - ptCurrent.y)) {
		    ptNext.x = ptCurrent.x;
		} else {
		    ptNext.y = ptCurrent.y;
		}
	}

	private static int CONNECTION_OFFSET = 26;

	/**
     * updateIfNotRectilinear
     * This is the core method that will calculate the rectilinear version of the
     * polyline points.
     * 
	 * @param conn Connection that is the owner of the PointList
	 * @param newLine PointList to be checked and modified
	 */
	protected void updateIfNotRectilinear(Connection conn, PointList newLine) {
		boolean isRectilinear = true;

		for (int i = 0; i < newLine.size() - 1; i++) {
			Ray segVector =
				new Ray(newLine.getPoint(i), newLine.getPoint(i + 1));

			if (segVector.x != 0 && segVector.y != 0) {
				isRectilinear = false;
				break;
			}
		}

		// If this thing isn't rectilinear, then first try to turn it into a
		// biterminal and if that doesn't work, then try to route it.
		if (!isRectilinear && !updateToBiTerminal(conn, newLine)) {
			// We've got a line that isn't rectilinear, so let's route
			// General rules based on number of segments starting with
			// if starting with two points (one segment) take shortest distance first.
			// if starting with three points (two segments) put longest segment
			//	as the middle segment
			OrthogonalRouterUtilities.resetEndPointsToCenter(conn, newLine);

			PointList oldPoints = PointListUtilities.copyPoints(newLine);

			PointList newPoints = new PointList();
			newPoints.addPoint(oldPoints.removePoint(0));
			while (oldPoints.size() > 0) {
				if (oldPoints.size() >= 2) {
					// This starts at point where last left off,
					// or the starting point if first time through.
					Point p0 = newPoints.getLastPoint();
					Point p1 = oldPoints.removePoint(0);
					Point p2 = oldPoints.removePoint(0);

					// make the shortest segment first.
					if (Math.abs(p2.y - p0.y) > Math.abs(p2.x - p0.x)) {
						// x has shortest segment
						newPoints.addPoint(new Point(p1.x, p0.y));
						newPoints.addPoint(new Point(p1.x, p2.y));
					} else // y has shortest segment first. 
						{
						newPoints.addPoint(new Point(p0.x, p1.y));
						newPoints.addPoint(new Point(p2.x, p1.y));
					}
					newPoints.addPoint(p2);
				} else if (oldPoints.size() == 1) {
					Point p0 = newPoints.getLastPoint();
					Point p1 = oldPoints.removePoint(0);
					if (Math.abs(p1.y - p0.y) > Math.abs(p1.x - p0.x)) {
						newPoints.addPoint(new Point(p1.x, p0.y));
					} else {
						newPoints.addPoint(new Point(p0.x, p1.y));
					}
					newPoints.addPoint(p1);
				}

			}
			oldPoints.removeAllPoints();
			// Now make a pass through to collapse any redundent segments.
			oldPoints.addPoint(newPoints.removePoint(0));
			while (newPoints.size() >= 2) {
				Point p0 = oldPoints.getLastPoint();
				Point p1 = newPoints.getPoint(0);
				Point p2 = newPoints.getPoint(1);
				if (p0.x == p1.x && p0.x == p2.x) {
					// Have two vertical segments in a row
					// get rid of the point between
					newPoints.removePoint(0);
				} else if (p0.y == p1.y && p0.y == p2.y) {
					// Have two horizontal segments in a row
					// get rid of the point between
					newPoints.removePoint(0);
				} else {
					oldPoints.addPoint(newPoints.removePoint(0));
				}
			}
			while (newPoints.size() > 0) {
				oldPoints.addPoint(newPoints.removePoint(0));
			}

			// set the newly routed line back into newLine
			newLine.removeAllPoints();
			for (int i = 0; i < oldPoints.size(); i++)
				newLine.addPoint(oldPoints.getPoint(i));
		}
	}

	/**
     * checkEndSegments
     * This method is useful to ensure that the arrow heads and / or tail adornments
     * are always visible irrespective of any routing that occurs.  This is accomplished
     * by assert a minimum length of the line segments that are at the beginning and end
     * of the PointList. 
     * 
	 * @param conn Connection to check the end segments of
	 * @param newLine PointList to modify
     * @return boolean true if end segments are ok, false otherwise.
	 */ 
	protected boolean checkEndSegments(Connection conn, PointList newLine) {
		boolean bOk = true;
        
		Dimension connection_offset = new Dimension(CONNECTION_OFFSET, 0);
		conn.translateToRelative(connection_offset);
		
        // now check for end segments length and fix up after.
		if (newLine.size() > 2) {
			Point ptFix = new Point(newLine.getPoint(1));
			if (!checkEndSegment(conn, conn.getSourceAnchor(), ptFix, connection_offset.width / 2)) {
				newLine.setPoint(ptFix, 1);
				// check next point to ensure rectilinear
				Point ptNext = newLine.getPoint(2);
				makeOrthogonal(ptFix, ptNext);
                
                newLine.setPoint(ptNext, 2);
                bOk = false;
			}

			ptFix = new Point(newLine.getPoint(newLine.size() - 2));
			if (!checkEndSegment(conn, conn.getTargetAnchor(), ptFix, connection_offset.width / 2)) {
				newLine.setPoint(ptFix, newLine.size() - 2);
				// check next point to ensure rectilinear
				Point ptNext = newLine.getPoint(newLine.size() - 3);
				makeOrthogonal(ptFix, ptNext);
                
                newLine.setPoint(ptNext, newLine.size() - 3);
                bOk = false;
			}
		}
        
        return bOk;
	}
    
    /**
     * straightenPoints
     * This is a simpler version of the @see updateIfNotRectilinear that simply ensures
     * that the lines are horizontal or vertical without any intelligence in terms of 
     * shortest distance around a rectangle.
     * 
	 * @param newLine PointList to check for rectilinear qualities and change if necessary.
	 */
	protected void straightenPoints(PointList newLine) {
        for (int i=0; i<newLine.size()-1; i++) {
            Point ptCurrent = newLine.getPoint(i);
            Point ptNext = newLine.getPoint(i+1);
            makeOrthogonal(ptCurrent, ptNext);
            
            newLine.setPoint(ptNext, i+1);
        }
    }

	/**
	 * checkEndSegment
	 * This method is useful to ensure that the arrow heads and / or tail adornments
	 * are always visible irrespective of any routing that occurs.  This is accomplished
	 * by assert a minimum length of the line segments that are at the beginning and end
	 * of the PointList. 
	 * 
	 * @param conn Connection that is used to reference the source / target anchors
	 * @param anchor ConnectionAnchor used to calculate the edge point
	 * @param ptNext Point that is checked against the edge to see if it's in violation.  It will
	 * be modified to a correct value if the method returns false.
     * @param offset int value representing the offset allowed from the shape edge.
	 * @return boolean true if end segment is ok, false otherwise.
	 */
	protected boolean checkEndSegment(
		Connection conn,
		ConnectionAnchor anchor,
		Point ptNext,
        int offset) {
		LineSeg seg = OrthogonalRouterUtilities.getOrthogonalLineSegToAnchorLoc(conn, anchor, ptNext);
		if (seg != null) {
			// ensure target line segments is bigger then a tolerance level (average arrow size)
			if (seg.length() < offset) {
				seg.pointOn(offset, LineSeg.KeyPoint.ORIGIN, ptNext);
				return false;
			}
		}

		return true;
	}

	private static final int maxRoutingDepth = 10;

	/* 
     * Overridden method from ObliqueRouter that will perform the conversion of the
     * polyline to a rectilinear version.
     * 
	 * @see com.ibm.xtools.draw2d.ObliqueRouter#routeLine(org.eclipse.draw2d.Connection, int, org.eclipse.draw2d.geometry.PointList)
	 */
	public void routeLine(
		Connection conn,
		int nestedRoutingDepth,
		PointList newLine) {
		boolean skipNormalization =
			(routerFlags & ROUTER_FLAG_SKIPNORMALIZATION) != 0;

        int nStartSize = newLine.size();
        
		// if we are reorienting, then just default to the super class implementation and
		// don't try to do rectilinear routing.
		if (isReorienting(conn)) {
			super.routeLine(conn, nestedRoutingDepth, newLine);
			resetEndPointsToEdge(conn, newLine);
			return;
		}

		// get the original line
		if (checkSelfRelConnection(conn, newLine)) {
			 checkEndSegments(conn, newLine);
			 resetEndPointsToEdge(conn, newLine);
			return;
		}

		// We've eliminated any unnecessary segments,
		// Now let's make sure everything is rectilinear
		updateIfNotRectilinear(conn, newLine);

		// Because we have created a polyline, it may have multiple
        // points of intersection with the originating and
        // terminating views.  We need to find the last intersection
        // point.
        boolean normalizationChangedLine = false;
		if (!skipNormalization) {
			normalizationChangedLine = removeSegmentsInViews(conn, newLine);
	    	normalizationChangedLine |= removePointsInViews(conn, newLine); 
            
            // Normalize the polyline to remove unwanted segments
			Dimension tolerance = new Dimension(3, 0);
			if (!isFeedback(conn))
				tolerance = (Dimension)MapModeUtil.getMapMode(conn).DPtoLP(tolerance);
			
            normalizationChangedLine |= PointListUtilities.normalizeSegments(newLine, tolerance.width);
		}
		
        // check the end segments to ensure they conform to a minimum distance.
        checkEndSegments(conn, newLine);
                 
		resetEndPointsToEdge(conn, newLine); 
        
        // final fix-up to ensure straight lines  
        straightenPoints(newLine);
         
		if (normalizationChangedLine) {
			// May need to reposition endpoints again, so recurse. It must be
			// the case that normalization reduces the complexity of the line,
			// so that the recursion terminates.
			if (nestedRoutingDepth < maxRoutingDepth) {
				nestedRoutingDepth++; 
				routeLine(conn, nestedRoutingDepth, newLine);
				// If unwinding from setting to 0, then don't decrement.
				if (nestedRoutingDepth != 0)
					nestedRoutingDepth--;
			}
		}
        else {
            Rectangle startRect = new Rectangle(conn.getSourceAnchor().getOwner().getBounds());
            conn.getSourceAnchor().getOwner().translateToAbsolute(startRect);
            conn.translateToRelative(startRect);
            
            Dimension buffer = new Dimension(2, 2);
    		conn.translateToRelative(buffer);
    		
            startRect.expand(buffer.width, buffer.height);
            
            Rectangle endRect = new Rectangle(conn.getTargetAnchor().getOwner().getBounds());
            conn.getTargetAnchor().getOwner().translateToAbsolute(endRect);
            conn.translateToRelative(endRect);
            endRect.expand(buffer.width, buffer.height);
            
            if (!startRect.contains(newLine.getPoint(0)) ||
                !endRect.contains(newLine.getPoint(newLine.size() - 1)) ||
                newLine.size() - nStartSize >= 2) {
             
                newLine.removeAllPoints();
                Point r1 = conn.getSourceAnchor().getReferencePoint();
                conn.translateToRelative(r1);
                newLine.addPoint(r1);

                Point r2 = conn.getTargetAnchor().getReferencePoint();
                conn.translateToRelative(r2);
                newLine.addPoint(r2);
                
                updateIfNotRectilinear(conn, newLine);
                resetEndPointsToEdge(conn, newLine); 
            }
        }
		//## end RectRouter::routeLine%803842153.body
	}
}
