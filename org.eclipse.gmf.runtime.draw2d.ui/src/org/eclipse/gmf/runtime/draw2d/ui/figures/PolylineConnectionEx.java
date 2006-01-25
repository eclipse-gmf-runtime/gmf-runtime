/******************************************************************************
 * Copyright (c) 2002, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.draw2d.ui.figures;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.eclipse.draw2d.ArrowLocator;
import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.ConnectionLocator;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.RotatableDecoration;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gmf.runtime.draw2d.ui.geometry.LineSeg;
import org.eclipse.gmf.runtime.draw2d.ui.geometry.PointListUtilities;
import org.eclipse.gmf.runtime.draw2d.ui.internal.figures.ConnectionLayerEx;
import org.eclipse.gmf.runtime.draw2d.ui.internal.figures.DelegatingLayout;
import org.eclipse.gmf.runtime.draw2d.ui.internal.figures.PolylineAnchor;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.MapModeUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;


/**
 * This is an extension of the <code>PolylineConnection</code> class to support avoid obstructions, smoothness
 * and jump-links behavior.
 * 
 * @author sshaw
 * @canBeSeenBy %partners
 */
public class PolylineConnectionEx extends PolylineConnection implements IPolygonAnchorableFigure {
	
	private RotatableDecoration startDecoration, endDecoration;

    private static Rectangle LINEBOUNDS = Rectangle.SINGLETON;
    private static int TOLERANCE = 3;
    
    /**
     * No smoothness
     */
    public static final int SMOOTH_NONE = 0x0000;
    
    /**
     * Some smoothness
     */
    public static final int SMOOTH_LESS = 0x0010;
    
    /**
     * Normal smoothness
     */
    public static final int SMOOTH_NORMAL = 0x0020;
    
    /**
     * More exagerated smoothness
     */
    public static final int SMOOTH_MORE = 0x0040;

    /**
     * Flag indicates whether the connection will attempt to "jump"
     * other connection below it in the z-order.
     */
    public static final int JUMPLINK_FLAG_BELOW = 0x4000; // jump links below
    
    /**
     * Flag indicates whether the connection will attempt to "jump"
     * other connection above it in the z-order.
     */
    public static final int JUMPLINK_FLAG_ABOVE = 0x8000; // jump links above
    
    /**
     * Flag indicates whether the connection will attempt to "jump"
     * all other connections regardless of z-order.
     */
    public static final int JUMPLINK_FLAG_ALL =
        JUMPLINK_FLAG_BELOW | JUMPLINK_FLAG_ABOVE;

    private static final int SMOOTH_FACTOR_LESS = 15;
    private static final int SMOOTH_FACTOR_NORMAL = 30;
    private static final int SMOOTH_FACTOR_MORE = 50;

    // extra routing styles
    private static final int ROUTE_AVOID_OBSTACLE = 0x0100;
    // avoid intersection with other nodes
    private static final int ROUTE_CLOSEST_ROUTE = 0x0200;
    // always use closest route to the destination
    private static final int ROUTE_JUMP_LINKS = 0x0400; // jump other links

    // jump link flags
    private static final int JUMPLINK_FLAG_SMOOTH = 0x0800;
    // indicates whether the jump links are smooth or not
    private static final int JUMPLINK_FLAG_ANGLEIN = 0x1000;
    // indicates whether the link is angled inwards
    private static final int JUMPLINK_FLAG_ONBOTTOM = 0x2000;

    private static final int JUMPLINK_DEFAULT_SMOOTHNESS = 30;
    
    private long styleBits;
    private JumpLinkSet jumpLinkSet;
    private Map connectionAnchors;
    static private final String szAnchor = ""; //$NON-NLS-1$
    
    /**
     * A dimension used by the isFeedbackLayer() method to check if we are
     * on a feedback layer.
     */
    private static final Dimension dimCheck = new Dimension(100, 100);
    
    /**
     * This method checks if we are on a feedback layer by comparing
     * the value of a Dimension with the value after translating it
     * into relative coordinates.
     * 
     * @return true if we are on a feedback layer, which means the
     * results after translating were the same as not translating, or false
     * if we are not on a feedback layer.
     */
    private boolean isFeedbackLayer() {
    	Dimension copied = dimCheck.getCopy();
    	translateToRelative(copied);
    	return dimCheck.equals(copied);
    }
    
    /**
     * 
     */
    public PolylineConnectionEx() {
        styleBits =
                JUMPLINK_FLAG_BELOW
                | JUMPLINK_FLAG_SMOOTH
                | JUMPLINK_FLAG_ANGLEIN;            
        setLayoutManager(new DelegatingLayout());
    }
    
	/**
	 * Returns the connectionAnchors.
	 * @return Hashtable
	 */
	protected Map getConnectionAnchors() {
		if (connectionAnchors == null)
			connectionAnchors = new HashMap(1);
		return connectionAnchors;
	}
    
    /**
     * Provides a utility function for dirtying the jump links and repainting the line.
     */
    public void refreshLine() {
        dirtyJumpLinks();
        repaint();
    }

    /**
     * Add a point to the polyline connection.
     */
    public void addPoint(Point pt) {
        super.addPoint(pt);
        refreshLine();
    }

    /**
     * Calculate and store the tolerance value for determining whether the line contains a point or not.
     * 
     * @param isFeedbackLayer  see the isFeedbackLayer() method
     */
    private int calculateTolerance(boolean isFeedbackLayer) {
		Dimension absTol = new Dimension(TOLERANCE + lineWidth / 2, 0);
		
    	if (!isFeedbackLayer) {
    		MapModeUtil.getMapMode(this).DPtoLP(absTol);
    	}

    	return absTol.width;
    }
    
    /**
     * Returns the bounds which holds all the points in this
     * polyline connection. Returns any previously existing
     * bounds, else calculates by unioning all the children's
     * dimensions.
     *
     * @return  Bounds to hold all the points.
     */
    public Rectangle getBounds(){
        if (bounds == null) {
            if (getSmoothFactor() != 0) {
                bounds = getSmoothPoints().getBounds();
                bounds.expand(lineWidth/2, lineWidth/2);
                
                for(int i=0; i<getChildren().size(); i++) {
                    IFigure child = (IFigure)getChildren().get(i);
                    bounds.union(child.getBounds());
                }
            }
            else
                super.getBounds();
            
            boolean isFeedbackLayer = isFeedbackLayer();
            int calculatedTolerance = calculateTolerance(isFeedbackLayer);
            Dimension jumpLinkSize = calculateJumpLinkSize(isFeedbackLayer);
            
            // extend the boundary slightly by the jumplinks height value
            bounds.expand(jumpLinkSize.height + calculatedTolerance, jumpLinkSize.height + calculatedTolerance);
        }
        return bounds;
    }

    /**
     * Method getShallowBounds.
     * @return Rectangle
     */
    public Rectangle getSimpleBounds(){
        Point s = getStart();
        Point e = getEnd();
        Point start = new Point(Math.min(s.x, e.x), Math.min(s.y, e.y));
        Dimension d = new Dimension(Math.abs(s.x-e.x), Math.abs(s.y-e.y));
        return new Rectangle(start.x, start.y, d.width, d.height);
    }

    /**
     * Determine if the polyline connection contains a given point.
     * @param x int value of the point to check containment of
     * @param y int value of the point to check containment of.
     * @return boolean true indicating containment, false otherwise.
     */
    public boolean containsPoint(int x, int y) {
    
    	boolean isFeedbackLayer = isFeedbackLayer();
    	int calculatedTolerance = calculateTolerance(isFeedbackLayer);
    
        LINEBOUNDS.setBounds(getBounds());
        LINEBOUNDS.expand(calculatedTolerance,calculatedTolerance);
        if (!LINEBOUNDS.contains(x, y))
            return false;
    
    	int ints[] = getSmoothPoints().toIntArray();
    	for (int index = 0; index < ints.length - 3; index  += 2) {
    		if (lineContainsPoint(ints[index], ints[index + 1],
    			ints[index + 2], ints[index + 3], x, y,
				isFeedbackLayer))
    			return true;
    	}
    	List children = getChildren();
    	for (int i = 0; i < children.size(); i++) {
    		if (((IFigure)children.get(i)).containsPoint(x, y))
    			return true;
    	}
    	
        return false;
    }
    
    private boolean lineContainsPoint(
        int x1, int y1,
        int x2, int y2,
        int px, int py,
		boolean isFeedbackLayer) {
        LINEBOUNDS.setSize(0,0);
        LINEBOUNDS.setLocation(x1,y1);
        LINEBOUNDS.union(x2,y2);
        int calculatedTolerance = calculateTolerance(isFeedbackLayer);
        LINEBOUNDS.expand(calculatedTolerance,calculatedTolerance);
        if (!LINEBOUNDS.contains(px,py))
            return false;
    
        double v1x, v1y, v2x, v2y;
        double numerator, denominator;
        double result = 0;
    
        if( x1 != x2 && y1 != y2 ) {
            v1x = (double)x2 - x1;
            v1y = (double)y2 - y1;
            v2x = (double)px - x1;
            v2y = (double)py - y1;
            
            numerator = v2x * v1y - v1x * v2y;
            
            denominator = v1x * v1x + v1y * v1y;
    
            result = numerator * numerator / denominator;
        }
        
        // if it is the same point, and it passes the bounding box test,
        // the result is always true.
        return result <= calculatedTolerance * calculatedTolerance;
                             
    }
    
    /**
     * Calculate the line segment index for a given point.  This is important
     * for the drag tracker that add's bendpoints on a connection.
     * 
     * @param x the x value in relative coordinates
     * @param y the y value in relative coordinates
     * @return the index of the line segment that is nearest to the given point.
     */
    public int findLineSegIndexOfPoint(int x, int y) {
        calculateTolerance(isFeedbackLayer());

        return PointListUtilities.findNearestLineSegIndexOfPoint(getPoints(), new Point(x, y));
    }
    
    /**
     * If the smooth factor is turned on, then calculate the approximated smooth polyline for display
     * or other purposes.  It is important to note that the smoothed points are not persisted.
     * 
     * @return <code>PointList</code> that is a polyline approximation of a bezier curve calculated based 
     * on the smoothness factor.
     */
    public PointList getSmoothPoints() {
        if (getSmoothFactor() > 0) {
            return PointListUtilities.calcSmoothPolyline(getPoints(), getSmoothFactor(), PointListUtilities.DEFAULT_BEZIERLINES);
        } else {
            return PointListUtilities.copyPoints(getPoints());
        }
    }

    /**
     * Insert a point at the given index into the polyline connection.
     */
    public void insertPoint(Point pt, int index) {
        super.insertPoint(pt, index);
        refreshLine();
    }

    /**
     * Override the figure method "outlineShape" to draw the actual polyline connection shape.
     * Special code to regenerate the jumplinks and to draw the polyline smooth is also done
     * during this method call.
     */
    protected void outlineShape(Graphics g) {

        PointList displayPoints = getSmoothPoints();
        int incline = calculateJumpLinkIncline(isFeedbackLayer());
        
        if (shouldJumpLinks()) {
            regenerateJumpLinks();

            JumpLinkSet pJumpLinkSet = getJumpLinkSet();
            if (pJumpLinkSet != null && pJumpLinkSet.m_pJumpLinks != null) {
                int nSmoothNess = 0;
                if (isJumpLinksSmooth())
                    nSmoothNess = JUMPLINK_DEFAULT_SMOOTHNESS;

                boolean bOnBottom = isJumpLinksOnBottom();

                ListIterator linkIter = pJumpLinkSet.m_pJumpLinks.listIterator();
                while (linkIter.hasNext()) {
                    JumpLink pJumpLink = (JumpLink) linkIter.next();

                    PointList jumpLinkPoints = PointListUtilities.routeAroundPoint(
                        displayPoints,
                        pJumpLink.m_ptIntersect,
                        pJumpLink.m_nHeight,
                        pJumpLink.m_nWidth,
                        nSmoothNess,
                        incline,
                        !bOnBottom);
                    if (jumpLinkPoints != null)
                    	displayPoints = jumpLinkPoints;
                }
            }
        }

        g.drawPolyline(displayPoints);
    }
    
    /**
     * Set the line width of the polyline connection.
     */
    public void setLineWidth(int w) {
        bounds = null;
        super.setLineWidth(w);
    }

    /**
     * @deprecated Use setPoints(PointList)
     */
    public void setPoint(Point pt, int index) {
        super.setPoint(pt, index);
        refreshLine();
    }

    /**
     * Sets the list of points to be used by this polyline connection.
     * Removes any previously existing points. 
     *
     * @param points  New set of points.
     */
    public void setPoints(PointList points) {
        super.setPoints(points);

        dirtyAllJumpLinks();
        refreshLine();
    }
    
    /**
     * Calculate the jumplink dimensions.
     */
    private static final int JUMPLINK_DEFAULT_WIDTH = 25;
    private static final int JUMPLINK_DEFAULT_HEIGHT = 10;
   
    /**
     * Calculate the size of the jump link.
     * 
     * @param isFeedbackLayer the <code>boolean</code> that determines if mapping of the coordinates will occur.  
     * This is necessary since the feedback layer doesn't not go through the zooming or mapmode scaling.
     * 
     * @return <code>Dimension</code> that is the jump link size
     */
    private Dimension calculateJumpLinkSize(boolean isFeedbackLayer) {
    	Dimension jumpDim = new Dimension(JUMPLINK_DEFAULT_WIDTH, JUMPLINK_DEFAULT_HEIGHT);
    	
    	if (!isFeedbackLayer) {
    		MapModeUtil.getMapMode(this).DPtoLP(jumpDim);
    	}
    	
        return jumpDim;
    }
    
    /**
     * Calculate the jumplink incline
     * 
     * @param isFeedbackLayer the <code>boolean</code> that determines if mapping of the coordinates will occur.  
     * This is necessary since the feedback layer doesn't not go through the zooming or mapmode scaling.
     */
    private int calculateJumpLinkIncline(boolean isFeedbackLayer) {
    	if (isJumpLinksAngledIn())
        	return calculateJumpLinkSize(isFeedbackLayer).width / 5;
    	
    	return 0;
    }
    
    /**
     * Dirty all connection jump links in the diagram
     */
    public void dirtyAllJumpLinks()
    {
        IFigure pParent = getParent();
        if (pParent instanceof ConnectionLayerEx)
            ((ConnectionLayerEx)pParent).dirtyJumpLinks(getBounds());
    }
     
    /**
     * Inner class for storing the specific JumpLink information.
     */
    protected class JumpLink {
        
        /**
         * intersection point value
         */
        public Point m_ptIntersect;
        
        /**
         * width of the jump link
         */
        public int m_nWidth;
        
        /**
         * height of the jump link
         */
        public int m_nHeight;
  
        /**
         * distance along the polyline
         */
        public int m_nDistance;
    }

    /**
     * Inner class for storing the set of JumpLink's associated with the
     * polyline connection.
     */
    protected class JumpLinkSet {

        /**
         * Default constructor
         */
        public JumpLinkSet() {
            m_bDirty = true;
            m_pJumpLinks = null;
        }
        
        /**
         * Determines if the jump links need to be regenerated.
         * 
         * @return <code>boolean</code> <code>true</code> if jump links need to be regenerated, <code>false</code> otherwise.
         */
        public boolean isDirty() {
            return m_bDirty;
        }
        
        /**
         * Sets the dirty flag back to false and notifies the connection layer
         * that it has been cleaned.
         * 
         * @param connect <code>Connection</code> whose jump links need to be regenerated.
         */
        protected void cleanJumpLinks(Connection connect) {
            m_bDirty = false;
            
            IFigure pParent = connect.getParent();
            if (pParent instanceof ConnectionLayerEx)
                ((ConnectionLayerEx)pParent).cleanJumpLinks();
        }
        
        /** 
         * Sets the jump links set as being dirty so that they will be regenerated
         * again at display time.
         */
        public void dirtyJumpLinks() {
            m_bDirty = true;
        }
        
        /**
         * Regenerates the jump links again according to the current arrangement of connections
         * on the diagram.
         * 
         * @param connect <code>Connection</code> whose jump links need to be regenerated.
         * @return <code>boolean</code> <code>true</code> if jump links were regenerated, <code>false</code> otherwise.
         */
        public boolean regenerateJumpLinks(Connection connect) {
            // check if we need to regenerate the jump link info
            if (isDirty()) {
                // regenerate the points where jump links will occur    
                calculateIntersections(connect);

                cleanJumpLinks(connect);
                
                return true;
            }

            return false;
        }

        /**
         * Inner class useed to compare two jump links to see which is further
         * along the polyline connection.
         */
        private class CompareDistance implements Comparator {
           
            public int compare(Object obj1, Object obj2) {
                JumpLink j1 = (JumpLink) obj1;
                JumpLink j2 = (JumpLink) obj2;

                if (j1.m_nDistance < j2.m_nDistance)
                    return -1;
                else
                    if (j1.m_nDistance > j2.m_nDistance)
                        return 1;

                return 0;
            }
        }

        /**
         * Sort the jump links according to their distance along the polyline
         * connection line.
         */
        private void sortByDistance() {

            Object[] jumpArray = m_pJumpLinks.toArray();
            Arrays.sort(jumpArray, new CompareDistance());

            for (int i = 0; i < m_pJumpLinks.size(); i++)
                m_pJumpLinks.set(i, jumpArray[i]);
        }
        
        /**
         * Calculate the intersections that occur between this connection and all the
         * other connections on the diagram.
         * 
         * @param connect <code>Connection</code> to calculate intersections with other connections in the layer.
         */
        private void calculateIntersections(Connection connect) {
            // regenerate the points where jump links will occur
            IFigure pParent = connect.getParent();

            if (m_pJumpLinks != null)
                m_pJumpLinks = null;

            PointList tmpLine = getSmoothPoints();

            long jumpType = (styleBits & JUMPLINK_FLAG_ALL);

            // only check intersections with connect views which are below this one.
            List children = pParent.getChildren();
            int nIndex = children.indexOf(connect);

            ListIterator childIter = children.listIterator();
            boolean bForwards = true;
            if (jumpType != JUMPLINK_FLAG_ALL)
            {
                childIter = children.listIterator(nIndex);
                if (jumpType == JUMPLINK_FLAG_BELOW)
                    bForwards = false;
            }
            
            boolean isFeedbackLayer = isFeedbackLayer();
            Dimension jumpLinkSize = calculateJumpLinkSize(isFeedbackLayer);
            
            while (bForwards ? childIter.hasNext() : childIter.hasPrevious()) {
                IFigure figure =
                    (IFigure) (bForwards ? childIter.next() : childIter.previous());
                PointList checkLine = null;

                if (figure != connect) {
                    if (figure instanceof PolylineConnectionEx)
                        checkLine = ((PolylineConnectionEx) figure).getSmoothPoints();
                    else
                        if (figure instanceof Connection)
                            checkLine = PointListUtilities.copyPoints(((Connection) figure).getPoints());

                    if (checkLine != null) {
                        PointList intersections = new PointList();
                        PointList distances = new PointList();
    
                        if (m_pJumpLinks == null)
                            m_pJumpLinks = new ArrayList(intersections.size());
    
                        if (PointListUtilities.findIntersections(tmpLine, checkLine, intersections, distances)) {
                            for (int i = 0; i < intersections.size(); i++) {
                                double dist1 = intersections.getPoint(i).getDistance(tmpLine.getFirstPoint());
                                double dist2 = intersections.getPoint(i).getDistance(tmpLine.getLastPoint());
                                double dist3 = intersections.getPoint(i).getDistance(checkLine.getFirstPoint());
                                double dist4 = intersections.getPoint(i).getDistance(checkLine.getLastPoint());
                                double minDist = Math.min(Math.min(dist1,dist2), Math.min(dist3,dist4));
                                if (minDist > jumpLinkSize.width/2){
                                    addJumpLink(intersections.getPoint(i), distances.getPoint(i).x, isFeedbackLayer);
                                }
                            }
                        }
                    }
                }
            }

            // check to see if we need to combine intersects that are close together
            combineCloseLinks(tmpLine);
        }

        /**
         * Add a new jump with the given intersection point and distance along the polyline
         * connection line.
         * @param ptIntersect
         * @param nDistance
         * @param isFeedbackLayer see the isFeedbackLayer() method
         */
        private void addJumpLink(Point ptIntersect, int nDistance, boolean isFeedbackLayer) {
            JumpLink pNewJumpLink = new JumpLink();
            pNewJumpLink.m_ptIntersect = new Point(ptIntersect);
            
            Dimension jumpLinkSize = calculateJumpLinkSize(isFeedbackLayer);
            
            pNewJumpLink.m_nWidth = jumpLinkSize.width;
            pNewJumpLink.m_nHeight = jumpLinkSize.height;
            pNewJumpLink.m_nDistance = nDistance;

            m_pJumpLinks.add(pNewJumpLink);
        }

        /**
         * If there are two consecutive jump links that are close together with a certain
         * tolerance value, then combine them into one larger jump link.
         * 
         * @param tmpLine the <code>PointList</code> 
         */
        private void combineCloseLinks(PointList tmpLine) {

            if (m_pJumpLinks == null || m_pJumpLinks.size() < 2)
                return;

            Dimension jumpLinkSize = calculateJumpLinkSize(isFeedbackLayer());
            int nCurrentWidth = jumpLinkSize.width;
            ArrayList jumpLinks = new ArrayList(m_pJumpLinks.size());

            // sort the jump links by distance
            sortByDistance();

            jumpLinks.addAll(m_pJumpLinks);
            m_pJumpLinks.clear();

            ListIterator linkIter = jumpLinks.listIterator();

            // combine intersects that are close together and increase jump link width
            JumpLink pLastJumpLink = (JumpLink) linkIter.next();
            JumpLink pPrevJumpLink = null;

            final int nDeltaMin = jumpLinkSize.width * 4 / 3;

            while (pLastJumpLink != null) {
                JumpLink pJumpLink = null;
                int nDelta = 0;

                if (linkIter.hasNext()) {
                    pJumpLink = (JumpLink) linkIter.next();
                    nDelta = pJumpLink.m_nDistance - pLastJumpLink.m_nDistance;
                }

                if ((nDelta > nDeltaMin) || pJumpLink == null) {
                    JumpLink pNewJumpLink = new JumpLink();

                    pNewJumpLink.m_nHeight = jumpLinkSize.height;
                    pNewJumpLink.m_nWidth = nCurrentWidth;
                    pNewJumpLink.m_nDistance = 0;
                    pNewJumpLink.m_ptIntersect = new Point(pLastJumpLink.m_ptIntersect);

                    if (pPrevJumpLink != null) {
                        // need to recalc the intersection point
                        long nNewDistance =
                            pPrevJumpLink.m_nDistance
                                + ((pLastJumpLink.m_nDistance - pPrevJumpLink.m_nDistance) / 2);
                        pNewJumpLink.m_ptIntersect = new Point();
                        PointListUtilities.pointOn(tmpLine, nNewDistance, LineSeg.KeyPoint.ORIGIN, pNewJumpLink.m_ptIntersect);
                    }

                    m_pJumpLinks.add(pNewJumpLink);
                    nCurrentWidth = jumpLinkSize.width;
                    pPrevJumpLink = null;
                } else {
                    if (pPrevJumpLink == null)
                        pPrevJumpLink = pLastJumpLink;
                    nCurrentWidth += jumpLinkSize.width - (nDeltaMin - nDelta);
                }

                pLastJumpLink = pJumpLink;
            }
        }

        private boolean m_bDirty;
        
        private List m_pJumpLinks;
    }

    /**
     * Get the smoothness factor for the polyline connection.  A value of 0
     * indicates that there is no smoothness.
     * 
     * @return the value is one of 0 - no smoothing, SMOOTH_FACTOR_LESS - rounded edges, 
     * SMOOTH_FACTOR_NORMAL - more curved look, SMOOTH_FACTOR_MORE - exagerated curving
     */
    private final int getSmoothFactor() {
        int smoothStyle = getSmoothness();

        if (smoothStyle == SMOOTH_LESS)
            return SMOOTH_FACTOR_LESS;
        else
            if (smoothStyle == SMOOTH_NORMAL)
                return SMOOTH_FACTOR_NORMAL;
            else
                if (smoothStyle == SMOOTH_MORE)
                    return SMOOTH_FACTOR_MORE;

        return 0;
    }

    /**
     * Sets the smoothness factor of this Connection that is reflected when the polyline is rendered. 
     * 
     * @param smooth the value is one of SMOOTH_NONE - no smoothing, SMOOTH_LESS - rounded edges, 
     * SMOOTH_NORMAL - more curved look, SMOOTH_MORE - exagerated curving.
     */
    public final void setSmoothness(int smooth) {
        // always turn off all smoothing
        styleBits &= ~(SMOOTH_LESS | SMOOTH_NORMAL | SMOOTH_MORE);

        if (smooth == SMOOTH_LESS
            || smooth == SMOOTH_NORMAL
            || smooth == SMOOTH_MORE) {
            styleBits |= smooth;
        }
    }

    /**
     * Gets the smoothness factor.  
     * 
     * @return the value is one of SMOOTH_NONE - no smoothing, SMOOTH_LESS - rounded edges, 
     * SMOOTH_NORMAL - more curved look, SMOOTH_MORE - exagerated curving.
     */
    public final int getSmoothness() {
        if ((styleBits & SMOOTH_LESS) != 0)
            return SMOOTH_LESS;
        else
            if ((styleBits & SMOOTH_NORMAL) != 0)
                return SMOOTH_NORMAL;
            else
                if ((styleBits & SMOOTH_MORE) != 0)
                    return SMOOTH_MORE;

        return 0;
    }

    /**
     * Determines if this polyline connection is using closest distance routing or not.
     * 
     * @return <code>boolean</code> <code>true</code> if it should be using closest distance routing, 
     * <code>false</code otherwise.
     */
    public final boolean isClosestDistanceRouting() {
        return ((styleBits & ROUTE_CLOSEST_ROUTE) != 0);
    }

    /**
     * Determines if this polyline connection is using avoid obstruction routing or not.
     * 
     * @return <code>boolean</code> <code>true</code> if it should be using avoid obstruction routing, 
     * <code>false</code otherwise.
     */
    public final boolean isAvoidObstacleRouting() {
        return ((styleBits & ROUTE_AVOID_OBSTACLE) != 0);
    }

    /**
     * Set the overall routing styles for this polyline connection.
     *
     * @param closestDistance <code>boolean</code> <code>true</code> if it should be using closest distance routing, 
     * <code>false</code otherwise
     * @param avoidObstacles <code>boolean</code> <code>true</code> if it should be using avoid obstruction routing, 
     * <code>false</code otherwise
     */
    public void setRoutingStyles(
        final boolean closestDistance,
        final boolean avoidObstacles) {
        
        if (closestDistance)
            styleBits |= ROUTE_CLOSEST_ROUTE;
        else {
            styleBits &= ~ROUTE_CLOSEST_ROUTE;
        }

        if (avoidObstacles) {
            if (!closestDistance)
                styleBits |= ROUTE_CLOSEST_ROUTE;

            styleBits |= ROUTE_AVOID_OBSTACLE;
        } else
            styleBits &= ~ROUTE_AVOID_OBSTACLE;
    }

    /**
     * Determines if this polyline connection should use the jump links methodology
     * or not.
     * 
     * @return <code>boolean</code> <code>true</code> if this connection should support jump links, 
     * <code>false</code> otherwise.
     */
    public final boolean shouldJumpLinks() {
        if ((styleBits & ROUTE_JUMP_LINKS) != 0) {
            IFigure pParent = getParent();
            if (pParent instanceof ConnectionLayerEx)
                return ConnectionLayerEx.shouldJumpLinks();
            
            return true;
        }

        return false;
    }

    /** 
     * Turns on or off the jump links functionality.
     * 
     * @param on the <code>boolean</code> <code>true</code> if this connection should support jump links, 
     * <code>false</code> otherwise.
     */
    public void setJumpLinks(boolean on) {
        if (on)
            styleBits |= ROUTE_JUMP_LINKS;
        else
            styleBits &= ~ROUTE_JUMP_LINKS;
    }

    /**
     * Set the jump links styles associated with the jump links functionality.
     *
     * @param jumpType value can be one of <code>PolylineConnectionEx.JUMPLINK_FLAG_BELOW</code>, 
     * <code>PolylineConnectionEx.JUMPLINK_FLAG_ABOVE</code> or <code>PolylineConnectionEx.JUMPLINK_FLAG_ALL</code>
     * @param curved the <code>boolean</code> indicating if <code>true</code> the jump link should be curved (semi-circle) 
     * or if <code>false</code> it should be straight (rectangular).
     * @param angleIn the <code>boolean</code> if <code>true</code> indicating the sides of the jump link are angled or 
     * if <code>false</code> then the sides of the jump link are straight.
     * @param onBottom the <code>boolean</code> <code>true</code> it will be oriented on the bottom of the connection,
     * <code>false</code> it will oriented on top.
     */
    public void setJumpLinksStyles(
        int jumpType,
        boolean curved,
        boolean angleIn,
        boolean onBottom) {

        styleBits &= ~JUMPLINK_FLAG_ALL;
        styleBits |= jumpType;

        if (curved)
            styleBits |= JUMPLINK_FLAG_SMOOTH;
        else
            styleBits &= ~JUMPLINK_FLAG_SMOOTH;

        if (angleIn)
            styleBits |= JUMPLINK_FLAG_ANGLEIN;
        else
            styleBits &= ~JUMPLINK_FLAG_ANGLEIN;

        if (onBottom)
            styleBits |= JUMPLINK_FLAG_ONBOTTOM;
        else
            styleBits &= ~JUMPLINK_FLAG_ONBOTTOM;
            
        dirtyJumpLinks();
    }

    /**
     * Determines if the jump links are smooth or not.
     * 
     * @return <code>boolean</code> indicating if <code>true</code> the jump link should be curved (semi-circle) or 
     * if <code>false</code> it should be straight (rectangular).
     */
    public final boolean isJumpLinksSmooth() {
        return ((styleBits & JUMPLINK_FLAG_SMOOTH) != 0);
    }

    /**
     * Determines if the jump links are angled in or not.
     * 
     * @return <code>boolean</code> if <code>true</code> indicating the sides of the jump link are angled or 
     * if <code>false</code> then the sides of the jump link are straight.
     */
    public final boolean isJumpLinksAngledIn() {
        return ((styleBits & JUMPLINK_FLAG_ANGLEIN) != 0);
    }

    /**
     * Determines if the jump links are on the bottom of the polyline connection or not.
     * 
     * @return <code>boolean</code> <code>true</code> it will be oriented on the bottom of the connection,
     * <code>false</code> it will oriented on top.
     */
    public final boolean isJumpLinksOnBottom() {
        return ((styleBits & JUMPLINK_FLAG_ONBOTTOM) != 0);
    }

    /**
     * Dirty the jump links in this polyline connection.
     */
    void dirtyJumpLinks() {
        JumpLinkSet pJumpLinkSet = getJumpLinkSet();
        if (pJumpLinkSet != null) {
            pJumpLinkSet.dirtyJumpLinks();
        }
    }

    /**
     * Regenerate all the jump links in this polyline connection.
     */
    private boolean regenerateJumpLinks() {
        JumpLinkSet pJumpLinkSet = getJumpLinkSet();
        if (pJumpLinkSet != null) {
            return pJumpLinkSet.regenerateJumpLinks(this);
        }

        return false;
    }

    /**
     * Gets the set of all the jump links in this polyline connection.
     */
    private JumpLinkSet getJumpLinkSet() {
        if (shouldJumpLinks()) {
            if (jumpLinkSet == null) {
                jumpLinkSet = new JumpLinkSet();
            }
        } else {
            jumpLinkSet = null;
        }

        return jumpLinkSet;
    }
    
    /**
     * Contains location data about a point in relation
     * with the polyline.
     * See getPointInfo for more information.
     */
    static public class PointInfo {
        /**
         * orthogonal distance from line
         */
        public int      fromLine;
        
        /**
         * distance from the end of the line
         */
        public int      fromEnd;
        
        /**
         * are the values stored as a percentage value instead of relative coordinates.
         */
        public boolean  isPercentage = true;
        
        /**
         * Defines a sign which encodes the positive or negative position of the point
         * as defined in {@link LineSeg#positionRelativeTo}
         */
        public double   proj = 0.0;
    }


    private static boolean projIn( double proj ) {
        return proj > 0 && proj < 1;
    }
    
    /**
     * returns a measure of how much proj is away from 0
     * when negative of from 1 when positive
     * returns 0 when proj is between 0 and 1
     * intended to be called when proj is not between 0 and 1
     */
    private static double projFactor(double proj) {
        if (proj < 0 )
            return -proj;
        else if (proj > 1.0)
            return proj - 1.0;
        else
            return 0;
    }
    
    private boolean atLeastOneProjectionCovers(Point p) {
        boolean oneProjIn = false;
        
        List segments = PointListUtilities.getLineSegments(getPoints());
        
        ListIterator segIter = segments.listIterator();
        while (segIter.hasNext() && !oneProjIn) {
            LineSeg segment = (LineSeg) segIter.next();
            double proj = segment.projection( p.x, p.y );
            if (projIn(proj)) {
                oneProjIn = true;
            }
        }
        
        return oneProjIn;
    }

    /**
     * Returns a structure PointInfo that contains the following information:
     * 1) perpendicular distance from the point p to the nearest segment 
     * 2) percentage distance from the projection of point p to the nearest segment
     * to the beginning of the polyline
     * NOTE:
     * 1) has a sign which encodes the positive or negative position of the point
     * as defined in LineSeg.positionRelativeTo()
     */
    private PointInfo getPointInfo( Point p ) {
        
        PointInfo pointInfo = new PointInfo();
        pointInfo.fromLine = Integer.MAX_VALUE;
        pointInfo.fromEnd  = 0;
        pointInfo.proj = Double.NEGATIVE_INFINITY;// so that initially projFactor is maximal
        
        List segments = PointListUtilities.getLineSegments(getPoints());
        
        int accumulatedLength = 0;
        
        boolean isCovered = atLeastOneProjectionCovers(p);
        
        ListIterator segIter = segments.listIterator();
        while (segIter.hasNext()) {
            LineSeg segment = (LineSeg) segIter.next();
            double proj = segment.projection( p.x, p.y );
            Point pt = segment.perpIntersect( p.x, p.y );
            int perpDist = (int)Math.round(p.getDistance(pt));
            if (isCovered) {
                // when covered we pick the smallest perpendicular distance
                if (perpDist < Math.abs(pointInfo.fromLine)) {
                    if (proj > 0 && proj < 1 ) {
                        pointInfo.fromLine = (segment.positionRelativeTo(p) == LineSeg.Sign.POSITIVE ? perpDist : -perpDist);
                        Point origin = segment.getOrigin();             
                        int inSegmentDistance = (int)Math.round(origin.getDistance(pt));
                        double fractionDistance = ((double)(accumulatedLength + inSegmentDistance))/((double)PointListUtilities.getPointsLength(getPoints()));
                        pointInfo.fromEnd = (int)Math.round(100*fractionDistance);
                    }
                }
            }
            else {
                // when not covered we pick the smallest projFactor
                if (projFactor(proj) < projFactor(pointInfo.proj)) {
                    pointInfo.fromLine = (segment.positionRelativeTo(p) == LineSeg.Sign.POSITIVE ? perpDist : -perpDist);
                    Point origin = segment.getOrigin();             
                    // we enforce the point to stay on a perpendicular position with regard to the segment
                    int inSegmentDistance = proj>0 ? 
                        Math.min((int)Math.round(origin.getDistance(pt)), (int)segment.length()) 
                        : 0;
                        
                    double fractionDistance = ((double)(accumulatedLength + inSegmentDistance))/((double)PointListUtilities.getPointsLength(getPoints()));
                    pointInfo.fromEnd = (int)Math.round(100*fractionDistance);
                    if (pointInfo.fromEnd > 0 && pointInfo.fromEnd < 100)
                        pointInfo.fromEnd += ((inSegmentDistance == (int)segment.length()) ? -1 : 1);// to avoid corners
                    pointInfo.proj = proj;
                }
            }
            accumulatedLength += segment.length();
        }
            
        // sanity check
        if (pointInfo.fromLine == Integer.MAX_VALUE)
            pointInfo.fromLine = 0;// we should never get here 
                
        return pointInfo;
    }
    
    /* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.draw2d.ui.figures.IPolygonAnchorableFigure#getPolygonPoints()
	 */
	public PointList getPolygonPoints() {
		// TODO Auto-generated method stub
		return getSmoothPoints();
	}

	/* 
     * (non-Javadoc)
     * @see org.eclipse.gmf.runtime.draw2d.ui.figures.IAnchorableFigure#getConnectionAnchor(java.lang.String)
     */
    public ConnectionAnchor getConnectionAnchor(String terminal) {
        ConnectionAnchor connectionAnchor = (ConnectionAnchor)getConnectionAnchors().get(terminal);
        
        if (connectionAnchor == null)
            connectionAnchor = new PolylineAnchor(this, terminal);

        return connectionAnchor;
    }

    /* 
     * (non-Javadoc)
     * @see org.eclipse.gmf.runtime.draw2d.ui.figures.IAnchorableFigure#getConnectionAnchorTerminal(org.eclipse.draw2d.ConnectionAnchor)
     */
    public String getConnectionAnchorTerminal(ConnectionAnchor c) {
        if (getConnectionAnchors().containsValue(c)) {
            Iterator iter = getConnectionAnchors().keySet().iterator();
            String key;
            while (iter.hasNext()) {
                key = (String) iter.next();
                if (getConnectionAnchors().get(key).equals(c))
                    return key;
            }
        }
        return null;
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
     * @param p Point on the figure that gives a hint which anchor to return.
     * @return ConnectionAnchor reference to an anchor associated with the given point on the figure.
     */
    protected ConnectionAnchor createConnectionAnchor(Point p) {

        ConnectionAnchor connectionAnchor = null;
        int fromEnd = 0;
        
        if (p.x < 0) {  // if negative - return an anchor at the center
            connectionAnchor = new PolylineAnchor(this, fromEnd = 50);
        }
        else {
            PointInfo pInfo = getPointInfo(p);
            connectionAnchor = new PolylineAnchor(this, fromEnd = pInfo.fromEnd);
        }
        
        if (connectionAnchor != null) {
            String szKey = szAnchor + fromEnd;
            getConnectionAnchors().put(szKey, connectionAnchor);
        }
        
        return connectionAnchor;
    }

    /* 
     * (non-Javadoc)
     * @see org.eclipse.draw2d.IFigure#setForegroundColor(org.eclipse.swt.graphics.Color)
     */
    public void setForegroundColor(Color fg) {
        super.setForegroundColor(fg);
        if (getTargetDecoration() != null){
            getTargetDecoration().setForegroundColor(fg);
        }
        if (getSourceDecoration() != null){
            getSourceDecoration().setForegroundColor(fg);
        }
    }
    
    
    /**
     * Sets the decoration to be used at the start of the {@link Connection}.
     * 
     * @param dec the new source decoration
     * @param locator the <code>ConnectionLocator</code> that allows placement of the source
     * <code>RotableDecoration</code>.
     */
    public void setSourceDecoration(RotatableDecoration dec, ConnectionLocator locator) {
    	if (getSourceDecoration() != null)
    		remove(getSourceDecoration());
    	startDecoration = dec;
    	if (dec != null) {
    		add(dec, locator);
    	}
    }
    
    /**
     * Sets the decoration to be used at the end of the {@link Connection}.
     * 
     * @param dec the new target decoration
     * @param locator the <code>ConnectionLocator</code> that allows placement of the target
     * <code>RotableDecoration</code>.
     */
    public void setTargetDecoration(RotatableDecoration dec, ConnectionLocator locator) {
    	if (getTargetDecoration() != null)
    		remove(getTargetDecoration());
    	endDecoration = dec;
    	if (dec != null) {
    		add(dec, locator);
    	} 
    		
    } 
    
    /**
     * @return the target decoration - possibly null
     */
    protected RotatableDecoration getTargetDecoration() {
    	return endDecoration;
    }    
    
    /**
     * @return the source decoration - possibly null
     */
    protected RotatableDecoration getSourceDecoration() {
    	return startDecoration;
    }
    
    
	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.PolylineConnection#setTargetDecoration(org.eclipse.draw2d.RotatableDecoration)
	 */
	public void setTargetDecoration(RotatableDecoration dec) {
		if (getTargetDecoration() != null)
    		remove(getTargetDecoration());
    	endDecoration = dec;
    	if (dec != null) {
    		add(dec, new ArrowLocator(this, ConnectionLocator.TARGET));
    	} 
	}
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.PolylineConnection#setSourceDecoration(org.eclipse.draw2d.RotatableDecoration)
	 */
	public void setSourceDecoration(RotatableDecoration dec) {
		if (getSourceDecoration() != null)
    		remove(getSourceDecoration());
    	startDecoration = dec;
    	if (dec != null) {
    		add(dec, new ArrowLocator(this, ConnectionLocator.SOURCE));
    	}
	}
    
	private int[] dashes = null;
	
	/**
	 * Workaround for bugzilla 105358
	 * @param dashes <code>int[]</code> array of dash lengths
	 * @see Graphics#setLineDash(int[])
	 */
	public void setLineDash(int[] dashes) {
		if (dashes != null) {
			this.dashes = new int[dashes.length];
			for (int i = 0; i < dashes.length; i++) {
				int dash = dashes[i];
				if (dash <= 0)
					SWT.error(SWT.ERROR_INVALID_ARGUMENT);
				this.dashes[i] = dash;
			}
		}
		else {
			this.dashes = null;
		}
	}

	/** 
	 * Overriden to workaround bugzilla 105358
	 */
	public void paintFigure(Graphics graphics) {
		graphics.pushState();
		if (dashes != null && getLineStyle() == SWT.LINE_CUSTOM)
			graphics.setLineDash(dashes);
		super.paintFigure(graphics);
		graphics.popState();
	}
}

