/******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.draw2d.ui.internal.routers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.AbsoluteBendpoint;
import org.eclipse.draw2d.AnchorListener;
import org.eclipse.draw2d.Bendpoint;
import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LayoutManager;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.graph.Path;
import org.eclipse.draw2d.graph.ShortestPathRouter;
import org.eclipse.gmf.runtime.draw2d.ui.figures.IBorderItemLocator;
import org.eclipse.gmf.runtime.draw2d.ui.figures.PolylineConnectionEx;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.IMapMode;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.MapModeUtil;

/**
 * @author mmostafa
 *
 * RectilinearRouter which is aware of border items
 * This router, will make sure that it connects to teh correct side of teh border item
 * Also, it willmake sure that it never overlape the border item container
 * 
 */

public class BorderItemRectilinearRouter
    extends RectilinearRouter {

    private static int OFFSET = 15;

    /* (non-Javadoc)
     * @see org.eclipse.gmf.runtime.draw2d.ui.internal.routers.ObliqueRouter#calculateBendPoints(org.eclipse.draw2d.Connection)
     */
    protected PointList calculateBendPoints(Connection conn) {
        IFigure source = conn.getSourceAnchor().getOwner();
        IFigure target = conn.getTargetAnchor().getOwner();
        if (source == null || target == null || isAvoidingObstructions(conn) || isClosestDistance(conn)) {
            // reorient
            return super.calculateBendPoints(conn);
        }

        int sourcePosition = getBorderFigurePosition(source);
        int targetPosition = getBorderFigurePosition(target);
        PolylineConnectionEx fakeConnection = new PolylineConnectionEx() {

            public void validate() {
                // no need to validate
            }

            public void paintFigure(Graphics graphics) {
                // nothing to paint
            }
        };
        Rectangle sourceParentRect = getObstacle(source, conn, (sourcePosition != PositionConstants.NONE));
        Rectangle targetParentRect = getObstacle(target, conn, (targetPosition != PositionConstants.NONE));
        if (sourceParentRect.contains(targetParentRect)) {
            sourcePosition = reversePosition(sourcePosition);
        } else if (targetParentRect.contains(sourceParentRect)) {
            targetPosition = reversePosition(targetPosition);
        }

        fakeConnection.setSourceAnchor(new BorderItemConnectionAnchor(conn
            .getSourceAnchor(), sourcePosition, OFFSET));
        fakeConnection.setTargetAnchor(new BorderItemConnectionAnchor(conn
            .getTargetAnchor(), targetPosition, OFFSET));

        fakeConnection.setConnectionRouter(conn.getConnectionRouter());
        List originalbendpoints = (ArrayList) conn.getConnectionRouter()
            .getConstraint(conn);
        // protection code to prevent NPE while creating the connection
        if (originalbendpoints == null) {
            // reorient
            return super.calculateBendPoints(conn);
        }

        fakeConnection.setParent(conn.getParent());
        if (conn instanceof PolylineConnectionEx) {
            PolylineConnectionEx connection = (PolylineConnectionEx) conn;
            fakeConnection.setRoutingStyles(connection
                .isClosestDistanceRouting(), connection
                .isAvoidObstacleRouting());
        }
        List constraint = new ArrayList();
        for (Iterator itr = originalbendpoints.iterator(); itr.hasNext();) {
            Bendpoint bp = (Bendpoint) itr.next();
            constraint.add(new AbsoluteBendpoint(bp.getLocation()));
        }
        AbsoluteBendpoint startPoint = (AbsoluteBendpoint) constraint.get(0);
        if (sourcePosition != PositionConstants.NONE) {
            BorderItemConnectionAnchor anchor = (BorderItemConnectionAnchor) fakeConnection
                .getSourceAnchor();
            Point startBendpoint = anchor.getReferencePoint();
            conn.translateToRelative(startBendpoint);
            constraint.remove(0);
            startPoint = new AbsoluteBendpoint(startBendpoint);
            constraint.add(0, startPoint);
        }
        AbsoluteBendpoint endPoint = (AbsoluteBendpoint) constraint
            .get(constraint.size() - 1);
        if (targetPosition != PositionConstants.NONE) {
            BorderItemConnectionAnchor anchor = (BorderItemConnectionAnchor) fakeConnection
                .getTargetAnchor();
            Point endBendpoint = anchor.getReferencePoint();
            conn.translateToRelative(endBendpoint);
            constraint.remove(constraint.size() - 1);
            endPoint = new AbsoluteBendpoint(endBendpoint);
            constraint.add(endPoint);
        }
        avoidOverlappingWithParent(startPoint, endPoint, sourceParentRect,
            targetParentRect, constraint, conn);
        fakeConnection.setRoutingConstraint(constraint);
        PointList points = super.calculateBendPoints(fakeConnection);
        if (sourcePosition != PositionConstants.NONE) {
            BorderItemConnectionAnchor anchor = (BorderItemConnectionAnchor) fakeConnection
                .getSourceAnchor();
            Point startPoint1 = anchor.getAnchorPoint();
            conn.translateToRelative(startPoint1);
            points.insertPoint(startPoint1, 0);
        }

        if (targetPosition != PositionConstants.NONE) {
            BorderItemConnectionAnchor anchor = (BorderItemConnectionAnchor) fakeConnection
                .getTargetAnchor();
            Point endPoint1 = anchor.getAnchorPoint();
            conn.translateToRelative(endPoint1);
            points.addPoint(endPoint1);
        }
        fakeConnection.setParent(null);
        return points;
    }

    /**
     * utility method to revers the position
     * @param position the position to reverse
     * @return  teh reversed position
     */
    private int reversePosition(int position) {
        int newPosition = position;
        if (position == PositionConstants.SOUTH)
            newPosition = PositionConstants.NORTH;
        else if (position == PositionConstants.NORTH)
            newPosition = PositionConstants.SOUTH;
        else if (position == PositionConstants.WEST)
            newPosition = PositionConstants.EAST;
        else if (position == PositionConstants.EAST)
            newPosition = PositionConstants.WEST;
        return newPosition;
    }

    /**
     * @author MMostafa
     *  Border Item aware Anchor, this anchor will make sure that the anchor point
     *  and the reference point are on the correct side
     */
    private class BorderItemConnectionAnchor
        implements ConnectionAnchor {

        private ConnectionAnchor anchor;

        private int position;

        private int offset;

        public BorderItemConnectionAnchor(ConnectionAnchor anchor,
                int position, int offset) {
            this.anchor = anchor;
            this.position = position;
            this.offset = offset;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        public Point getReferencePoint() {
            Point referencePoint = getAnchorPoint();
            if (position == PositionConstants.SOUTH)
                referencePoint.y += offset;
            else if (position == PositionConstants.NORTH)
                referencePoint.y -= offset;
            else if (position == PositionConstants.WEST)
                referencePoint.x -= offset;
            else if (position == PositionConstants.EAST)
                referencePoint.x += offset;
            return referencePoint;
        }

        public Point getAnchorPoint() {
            Rectangle ownerBounds = getOwner().getBounds().getCopy();
            getOwner().translateToAbsolute(ownerBounds);
            if (position == PositionConstants.SOUTH)
                return ownerBounds.getBottom();
            else if (position == PositionConstants.NORTH)
                return ownerBounds.getTop();
            else if (position == PositionConstants.WEST)
                return ownerBounds.getLeft();
            else if (position == PositionConstants.EAST)
                return ownerBounds.getRight();
            return ownerBounds.getCenter();
        }

        public void addAnchorListener(AnchorListener listener) {
            // nothing to do
        }

        public Point getLocation(Point reference) {
            return reference;
        }

        public IFigure getOwner() {
            return anchor.getOwner();
        }

        public void removeAnchorListener(AnchorListener listener) {
            // nothing to do
        }

        public ConnectionAnchor getAnchor() {
            return anchor;
        }

    }

    /**
     * Returns the position of the border item with respect to its parent
     * Clients can override this method to change the way postions is
     * calculated for border items
     * @param borderFigure  Figure to use to get the position
     * @return  the border item position, see <code>PositionConstants</code>
     */
    protected int getBorderFigurePosition(IFigure borderFigure) {
        IFigure child = borderFigure;
        IFigure parent = borderFigure.getParent();
        if (parent != null && parent.getLayoutManager() != null) {
            LayoutManager layoutManager = parent.getLayoutManager();
            Object layoutConstraint = layoutManager.getConstraint(child);
            if (layoutConstraint instanceof IBorderItemLocator) {
                return ((IBorderItemLocator) layoutConstraint)
                    .getCurrentSideOfParent();
            }
        }
        return PositionConstants.NONE;
    }
    
    

    /**
     * Utility method that adjust the constrinat to avoid overlapping with 
     * Source or target parents, this method should be called only in case
     * of routing connections connecting to border items 
     * @param startPoint        start point or the route
     * @param endPoint          end point of the route
     * @param sourceParentRect  the rectangle of the source element parent
     * @param targetParentRect  the rectangle of the target element parent
     * @param constraint        the constraint to modify
     * @param conn              the connection to route
     */
    private void avoidOverlappingWithParent(AbsoluteBendpoint startPoint,
            AbsoluteBendpoint endPoint, Rectangle sourceParentRect,
            Rectangle targetParentRect, List constraint, Connection conn) {
        if (sourceParentRect.intersects(targetParentRect)) {
            return;
        }
        IMapMode mapMode = MapModeUtil.getMapMode(conn);
        int logicalOffset = mapMode.DPtoLP(OFFSET);
        // use GEF's shortest path router to get reference bend points
        ShortestPathRouter router = new ShortestPathRouter();
        Path path = new Path(startPoint, endPoint);
        router.addPath(path);
        router.addObstacle(sourceParentRect);
        router.addObstacle(targetParentRect);
        router.setSpacing(logicalOffset);
        router.solve();
        PointList points = path.getPoints();
        // remove the start and end points
        points.removePoint(0);
        points.removePoint(points.size() - 1);
        // deal with the remaining points
        if (points.size() > 0) {
            Point refrencePoint = points.getFirstPoint();
            AbsoluteBendpoint startPointGuidePoint = new AbsoluteBendpoint(startPoint);
            adjustPointUsingReferencePointAndObstacle(startPointGuidePoint,refrencePoint,sourceParentRect,  logicalOffset);
            constraint.add(1, startPointGuidePoint);
            points.removePoint(0);
            // this means we had only one reference point, we will use this point
            // to adjust poth source and target
            if (points.size() == 0) {
                AbsoluteBendpoint endPointGuidePoint = new AbsoluteBendpoint(endPoint);
                adjustPointUsingReferencePointAndObstacle(endPointGuidePoint,refrencePoint,targetParentRect,  logicalOffset);
                if (endPointGuidePoint.y == endPoint.y)
                    endPointGuidePoint.x = startPointGuidePoint.x;
                else
                    endPointGuidePoint.y = startPointGuidePoint.y;
                constraint.add(2, endPointGuidePoint);
            }
        }
        if (points.size() > 0) {
            Point referencePoint = points.getLastPoint();
            AbsoluteBendpoint endPointGuidePoint = new AbsoluteBendpoint(endPoint);
            adjustPointUsingReferencePointAndObstacle(endPointGuidePoint,referencePoint,targetParentRect , logicalOffset);
            constraint.add(constraint.size() - 1, endPointGuidePoint);
        }
    }

    /**
     * Modify a guide point based on a reference point to avoid collision with the
     * passed obstacle, the offset had to be in logical coordinates
     * @param guidePoint, the point to adjust
     * @param referencePoint, reference point to use during the adjust process
     * @param obstacle, obstable to consider
     * @param offSet, the offset had to be in logical coordinate
     */
    private void adjustPointUsingReferencePointAndObstacle(AbsoluteBendpoint guidePoint, Point referencePoint,
            Rectangle obstacle, int offSet ) {
        // check if the y of the starting point is in the rectangle range (point
        // will be west or east or the rectangle)
        boolean changeY = (guidePoint.y >= obstacle.y && guidePoint.y <= (obstacle.y + obstacle.height));

        if (changeY) {
            if (referencePoint.y < guidePoint.y)
                guidePoint.y = obstacle.y - offSet;
            else
                guidePoint.y = obstacle.y + obstacle.height
                    + offSet;
        } else {
            if (referencePoint.x < guidePoint.x)
                guidePoint.x = obstacle.x - offSet;
            else
                guidePoint.x = obstacle.x + obstacle.width
                    + offSet;
        }
    }

    /**
     * Returns the obstacle that the route should try to avoid, for example if the isBordereItem 
     * flag is ON, it will return the rectangle of the border item parent.
     * This method can be overriden by clients to provide a client specific way to find the obstacle
     * @param figure, figure to get the obstacle for
     * @param conn, the connection the router is routing
     * @param isBorderItem, indicates if the passed figure is a border item figrue or not
     * @return obstacle
     */
    protected Rectangle getObstacle(IFigure figure, Connection conn,
            boolean isBorderItem) {
        IFigure parent = null;
        if (isBorderItem)
            parent = getBorderItemParent(figure);
        else
            parent = figure;
        Rectangle rect = parent.getBounds().getCopy();
        parent.translateToAbsolute(rect);
        conn.translateToRelative(rect);
        return rect;
    }

    /**
     * return the parent of a border item figure
     * clients can override this method to find the 
     * @param figure, the border item figure
     * @return teh parent of the border item
     */
    protected IFigure getBorderItemParent(IFigure figure) {
        return figure.getParent().getParent();
    }
}
