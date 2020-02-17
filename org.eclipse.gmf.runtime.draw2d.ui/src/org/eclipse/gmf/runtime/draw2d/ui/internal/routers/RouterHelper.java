/******************************************************************************
 * Copyright (c) 2006, 2008 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.draw2d.ui.internal.routers;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import org.eclipse.draw2d.Bendpoint;
import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ShortestPathConnectionRouter;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.PrecisionPoint;
import org.eclipse.draw2d.geometry.Ray;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gmf.runtime.draw2d.ui.figures.PolylineConnectionEx;
import org.eclipse.gmf.runtime.draw2d.ui.geometry.PointListUtilities;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.MapModeUtil;

/**
 * Manager class which determines which shortest path connection router to use
 * for a given <code>Connection</code>.
 * 
 * @author sshaw
 * 
 */

class RouterHelper {	
	 
    static private RouterHelper sprm = new RouterHelper(true);

    /**
     * @return the <code>RouterHelper</code> singleton instance
     */
    static public RouterHelper getInstance() {
        return sprm;
    }
    
   private boolean useGEFRouter = true;
    
    private RouterHelper(boolean useGEFRouter) {
        super();
        this.useGEFRouter = useGEFRouter;
    }
    
    protected boolean getUseGEFRouter() {
    	return useGEFRouter;
    }
    /***************************************************************************
     * Following section is supporting useGEFRouter = true option
     ***************************************************************************/
    
    // if useGEFRouter = true, holds one router for each container that has at least
    // one connection with avoid obstacles on
    private Map<IFigure, ShortestPathConnectionRouter> routers = new WeakHashMap<IFigure, ShortestPathConnectionRouter>();

    // if useGEFRouter = true, keeps track of the last GEF router used
    // for routing each connection with avoid obstacles on
    private Map<Connection, ShortestPathConnectionRouter> lastUsedRouter = new WeakHashMap<Connection, ShortestPathConnectionRouter>();

    /**
     * Added to support GEF's router.
     */
    public void setConstraint(Connection conn, Object constraint) {
        if (useGEFRouter && isAvoidingObstructions(conn)) {
            ShortestPathConnectionRouter spcr = getConnRouter(conn, false);
            if (spcr != null) {
                setConstraint(spcr, conn, constraint);
            }
        }
    }
    
	/**
	 * Sets constraint for GEF router.
	 * 
	 * @param spcr
	 *            GEF router
	 * @param conn
	 *            <code>Connection</code> whose constraint is being set
	 * @param constraint
	 *            New constraint
	 */
	public void setConstraint(ShortestPathConnectionRouter spcr,
			Connection conn, Object constraint) {
		spcr.setConstraint(conn, null);
	}

	/**
	 * Added to support GEF's router.
	 * 
	 * @param conn
	 *            the <code>Connection</code> to be removed from GEF's router,
	 *            if applicable.
	 */
	public void remove(Connection conn) {
		if (useGEFRouter && isAvoidingObstructions(conn)) {
			ShortestPathConnectionRouter spcr = getConnRouter(conn, false);
			if (spcr != null) {
				cleanUpAvoidObstaclesRouter(spcr, conn);
			}
		}
	}
	
    /**
     * Removes conn from router and cleans up routers and lastUSedRouter appropriately
     */
    private void cleanUpAvoidObstaclesRouter(ShortestPathConnectionRouter router, Connection conn) {    	 
		if (router != null) {
			router.remove(conn);
			if (!router.hasMoreConnections()) {
				routers.remove(router.getContainer());
			}
			lastUsedRouter.remove(conn);
		}
    } 	

    /**
	 * Added to support GEF's router.
	 * 
	 * @param conn
	 *            the <code>Connection</code> to be potentially invalidated. 
	 */
    public void invalidate(Connection conn) {
        if (useGEFRouter && isAvoidingObstructions(conn)) {
            ShortestPathConnectionRouter spcr = getConnRouter(conn, false);
            if (spcr != null) {
                spcr.invalidate(conn);
            }
        }
    }
    
    /**
     * Retrieves GEF's router for given figContainer
     * @param figContainer
     * @param createNew If true, new router will be created if one doesn't already exist
     * @return
     */
    private ShortestPathConnectionRouter getRouter(IFigure figContainer, boolean createNew) {
        ShortestPathConnectionRouter shortestPathRouter = routers.get(figContainer);
        if (shortestPathRouter == null && createNew) {
            shortestPathRouter = new ShortestPathConnectionRouter(figContainer);
            shortestPathRouter.setSpacing(MapModeUtil.getMapMode(figContainer)
                .DPtoLP(10));
            routers.put(figContainer, shortestPathRouter);
        }

        return shortestPathRouter;
    }    
    
    /**
     * Retrieves GEF's router for routing conn, if both source and target of the connection
     * belong to the same container.
     * @param conn the <code>Connection</code> whose router is to be retrieved.
     * @param forRouting Indicates if this call is made in order to route the connection.
     * is no router for conn. If yes, and there is no apporpriate router for this connection, then new 
     * router will be created. 
     * @return the router to use for routing conn
     */
    protected ShortestPathConnectionRouter getConnRouter(Connection conn, boolean forRouting) {
        ShortestPathConnectionRouter lur = lastUsedRouter.get(conn);    	
    	// First, get the container for this connection
    	// Rule: both source and target figures have to belong to the same container. 
    	IFigure container = null;
        IFigure sourcefigContainer = getSourceContainer(conn);
        IFigure targetfigContainer = getTargetContainer(conn);        
        if (sourcefigContainer == null || targetfigContainer == null ) {
        	// this may happen when source or target anchors are being moved outside of all figures,
        	// or if source or target of conn is another connection        	        	
        	if (lur != null) {
        		// Route using the last router, if any, otherwise don't route using GEF
        		return lur;
        	} else {
        		container = null;
        	}
        } else if (sourcefigContainer != targetfigContainer) {
        	container = null;
        } else {
        	container = sourcefigContainer;
        }
        
        if (container == null) {
        	// conn spans between two different containers in which case we don't support
        	// avoid obstacles. If there was a router for this connection (conn used to belong
        	// to one container), remove it from previous router.
        	if (lur != null) {
        		cleanUpAvoidObstaclesRouter(lur, conn);
        	}
            return null;
        }
        ShortestPathConnectionRouter spcr = getRouter(container, forRouting);
 
        if (spcr != null && forRouting) {
			if (lur != spcr) {
				if (lur != null) {
					// conn changed container. Remove it from previous router.
					cleanUpAvoidObstaclesRouter(lur, conn);
				}
				lastUsedRouter.put(conn, spcr);				
			}			
		}
        return spcr;
    }   
    
    
    /***************************************************************************
     * end of section supporting useGEFRouter = true option
     ***************************************************************************/
    
    
    
    /**
     * @param conn the <code>Connection</code> that is to be check if it is a feedback
     * connection or not.
     * @return <code>true</code> is it is a feedback connection, <code>false</code> otherwise.
     */
    public boolean isFeedback(Connection conn) {
        Dimension dim = new Dimension(100, 100);
        Dimension dimCheck = dim.getCopy();
        conn.translateToRelative(dimCheck);
        return dim.equals(dimCheck);
    }
    
    /**
     * @param conn the <code>Connection</code> that is to be routed.
     * @return the <code>PointList</code> that is the list of points that are
     * a direct mapping of the constraint points.
     */
    public PointList routeFromConstraint(Connection conn) {
    	if (useGEFRouter && lastUsedRouter.get(conn) != null) {
    		// User just unselected Avoid obstacles options for this connection, clean up.
    		cleanUpAvoidObstaclesRouter(lastUsedRouter.get(conn), conn);
    	}
        List bendpoints = (List)conn.getConnectionRouter().getConstraint(conn);
        if (bendpoints == null)
            bendpoints = Collections.EMPTY_LIST;
        
        PointList points = new PointList(bendpoints.size());

        for (int i = 0; i < bendpoints.size(); i++) {
            Bendpoint bp = (Bendpoint) bendpoints.get(i);
            points.addPoint(bp.getLocation());
        }

        if (bendpoints.size() == 0) {
            Point r1 = conn.getSourceAnchor().getReferencePoint().getCopy();
            conn.translateToRelative(r1);
            points.addPoint(r1);
            
            Point r2 = conn.getTargetAnchor().getReferencePoint().getCopy();
            conn.translateToRelative(r2);
            points.addPoint(r2);
        }
        
        return points;
    }

    /**
     * @param conn the <code>Connection</code> that is to be routed.
     * @return the <code>PointList</code> that is the list of points that represent
     * the closest distance possible to route the line.
     */
    public PointList routeClosestDistance(Connection conn) {
    	if (useGEFRouter && lastUsedRouter.get(conn) != null) {
    		// User just unselected Avoid obstacles options for this connection, clean up.
    		cleanUpAvoidObstaclesRouter(lastUsedRouter.get(conn), conn);
    	}
        PointList newLine = routeFromConstraint(conn);
        
        Point ptOrig = new Point(newLine.getFirstPoint());
        Point ptTerm = new Point(newLine.getLastPoint());

        newLine.removeAllPoints();
        newLine.addPoint(ptOrig);
        newLine.addPoint(ptTerm);
        
        return newLine;
    }       
    
    /**
	 * @param conn
	 *            the <code>Connection</code> that is to be routed.
	 * @return the <code>PointList</code> that is the list of points that are
	 *         avoiding all the possible obstructions in the container for the
	 *         connection.
	 */
	public PointList routeAroundObstructions(Connection conn) {
		PointList newLine = null;
		newLine = routeClosestDistance(conn);

		Point infimumPoint = PointListUtilities.getPointsInfimum(newLine);
		Point supremumPoint = PointListUtilities.getPointsSupremum(newLine);

		Ray diameter = new Ray(infimumPoint, supremumPoint);
		Rectangle rPoly = new Rectangle(infimumPoint.x, infimumPoint.y,
				diameter.x, diameter.y);

		List collectObstructs = new LinkedList();

		IFigure parent = getRouterContainerFigure(conn);

		// don't bother routing if there is no attachments
		if (parent == null)
			return routeFromConstraint(conn);

		// set the end points back to the reference points - this will avoid
		// errors, where
		// an edge point is erroneously aligned with a specific edge, even
		// though the avoid
		// obstructions would suggest attachment to another edge is more
		// appropriate
		Point ptRef = conn.getSourceAnchor().getReferencePoint();
		conn.translateToRelative(ptRef);
		newLine.setPoint(ptRef, 0);
		ptRef = conn.getTargetAnchor().getReferencePoint();
		conn.translateToRelative(ptRef);
		newLine.setPoint(ptRef, newLine.size() - 1);

		// TBD - optimize this
		// increase connect view rect by width or height of diagram
		// to maximize views included in the obstruction calculation
		// without including all views in the diagram
		Rectangle rBoundingRect = new Rectangle(parent.getBounds());
		parent.translateToAbsolute(rBoundingRect);
		conn.translateToRelative(rBoundingRect);

		if (rPoly.width > rPoly.height) {
			rPoly.y = rBoundingRect.y;
			rPoly.setSize(rPoly.width, rBoundingRect.height);
		} else {
			rPoly.x = rBoundingRect.x;
			rPoly.setSize(rBoundingRect.width, rPoly.height);
		}

		collectObstructions(conn, rPoly, collectObstructs);

		// parse through obstruction collect and combine rectangle that
		// intersect with each other
		if (collectObstructs.size() > 0) {
			Dimension buffer = new Dimension(ROUTER_OBSTRUCTION_BUFFER + 1, 0);
			if (!isFeedback(conn))
				buffer = (Dimension) MapModeUtil.getMapMode(conn)
						.DPtoLP(buffer);
			final int inflate = buffer.width;

			List collapsedRects = collapseRects(collectObstructs, inflate);
			collectObstructs.clear();

			// Loop through the collapsedRects list until there are no more
			// intersections
			boolean bRouted = true;
			while (bRouted && !collapsedRects.isEmpty()) {
				ListIterator listIter = collapsedRects.listIterator();
				bRouted = false;

				while (listIter.hasNext()) {
					Rectangle rObstruct = (Rectangle) listIter.next();
					PointList routedPoly = PointListUtilities.routeAroundRect(
							newLine, rObstruct, 0, false, inflate);

					if (routedPoly != null) {
						bRouted = true;
						newLine.removeAllPoints();
						newLine.addAll(routedPoly);
					} else
						collectObstructs.add(rObstruct);
				}

				List tempList = collapsedRects;
				collapsedRects = collectObstructs;
				tempList.clear();
				collectObstructs = tempList;

				if (bRouted && !collapsedRects.isEmpty())
					resetEndPointsToEdge(conn, newLine);
			}
		}

		return newLine;
    }
    
    /**
     * Finds all the children shapes of the parent figure passed in that are in
     * the way of the connection. This method will dig into children of
     * container shapes if one of the connection ends is also in that container.
     * 
     * @param connection
     *            the connection being routed
     * @param connectionRect
     *            the rectangle representing the connection bounds that is used
     *            to determine if a shape intersects with the connection
     * @param obstructionsToReturn
     *            the list of figures that the connection should be routed
     *            around
     */
    protected void collectObstructions(Connection connection,
            Rectangle connectionRect, List obstructionsToReturn) {

        Set containerFiguresToSearch = new HashSet();
        Set figuresToExclude = new HashSet();

        IFigure figure = connection.getSourceAnchor().getOwner();
        figuresToExclude.add(figure);
        figure = figure.getParent();
        while (figure != null) {
            if (figure.getLayoutManager() instanceof XYLayout) {
                containerFiguresToSearch.add(figure);
            }
            figuresToExclude.add(figure);
            figure = figure.getParent();
        }

        figure = connection.getTargetAnchor().getOwner();
        figuresToExclude.add(figure);
        figure = figure.getParent();
        while (figure != null) {
            if (figure.getLayoutManager() instanceof XYLayout) {
                containerFiguresToSearch.add(figure);
            }
            figuresToExclude.add(figure);
            figure = figure.getParent();
        }

        for (Iterator iter = containerFiguresToSearch.iterator(); iter
            .hasNext();) {
            IFigure containerFigure = (IFigure) iter.next();

            for (Iterator iterator = containerFigure.getChildren().iterator(); iterator
                .hasNext();) {
                IFigure childFigure = (IFigure) iterator.next();

                if (!figuresToExclude.contains(childFigure)) {

                    Rectangle rObstruct = new Rectangle(childFigure.getBounds());
                    childFigure.translateToAbsolute(rObstruct);
                    connection.translateToRelative(rObstruct);

                    // inflate slightly
                    rObstruct.expand(1, 1);

                    if (connectionRect.intersects(rObstruct)) {
                        obstructionsToReturn.add(rObstruct);
                    }
                }
            }
        }
    }
    
    /**
     * @param conn
     *            the <code>Connection</code> that is to have used to
     *            determine the end points for reseting the <code>newLine</code>
     *            parameter.
     * @param newLine
     *            the <code>PointList</code> to reset the end points of to be
     *            on the edge of the connection source and target nodes.
     */
    public void resetEndPointsToEdge(Connection conn, PointList newLine) {
    	if (newLine.size() < 2) {
    		/*
			 * Connection must have at least 2 points in the list: the source
			 * and target anchor points. Otherwise it's invalid connection.
			 * Invalid connection case: add a dumb point at the start of the
			 * list and at the end of the list. The first and the last point in
			 * the list are replaced by the new source and target anchor points
			 * in this method
			 */
    		newLine.addPoint(0, 0);
    		newLine.insertPoint(new Point(), 0);
    	}
    	
        PrecisionPoint sourceAnchorPoint, targetAnchorPoint;
        if (newLine.size() > 2) {
        	/*
        	 * First bend point is the outside reference point for the source anchor.
        	 * Last bend point is the outside reference point for the target anchor.
        	 */
	        PrecisionPoint sourceReference = new PrecisionPoint(newLine.getPoint(1));
	        PrecisionPoint targetReference = new PrecisionPoint(newLine.getPoint(newLine.size() - 2));
	        conn.translateToAbsolute(sourceReference);
	        conn.translateToAbsolute(targetReference);
	        sourceAnchorPoint = getAnchorLocation(conn.getSourceAnchor(), sourceReference);
	        targetAnchorPoint = getAnchorLocation(conn.getTargetAnchor(), targetReference);
        } else {
        	/*
        	 * We need to take target anchor reference point as an outside reference point
        	 * for the source anchor location. The outside reference point for the target
        	 * anchor would the source anchor location. 
        	 */
        	PrecisionPoint sourceReference = getAnchorReference(conn.getTargetAnchor());
	        sourceAnchorPoint = getAnchorLocation(conn.getSourceAnchor(), sourceReference);
	        targetAnchorPoint = getAnchorLocation(conn.getTargetAnchor(), sourceAnchorPoint);
        }
        
        
        conn.translateToRelative(sourceAnchorPoint);
        conn.translateToRelative(targetAnchorPoint);
        
        newLine.setPoint(sourceAnchorPoint,0);
        newLine.setPoint(targetAnchorPoint,newLine.size() - 1);
    }

    private final static int ROUTER_OBSTRUCTION_BUFFER = 12;

    /**
     * This method will collapse all the rectangles together that intersect in
     * the given List. It utilizes a recursive implementation.
     */
    private List collapseRects(List collectRect, int inflate) {
        if (collectRect.size() == 0)
            return new LinkedList();

        Rectangle rCompare = new Rectangle((Rectangle) collectRect.remove(0));
        List collapsedRects = collapseRects(rCompare, collectRect, inflate);
        collapsedRects.add(rCompare);

        return collapsedRects;
    }

    /**
     * Recursively called method called by collapseRects(List collectRect).
     */
    private List collapseRects(Rectangle rCompare, List collectRect, int inflate) {
        List newCollect = new LinkedList();
        Rectangle rCompare1 = new Rectangle(rCompare);

        // compare rectangle with each rectangle in the rest of the list
        boolean intersectionOccurred = false;
        ListIterator listIter = collectRect.listIterator();
        while (listIter.hasNext()) {
            Rectangle rCompare2 = new Rectangle((Rectangle) listIter.next());

            Rectangle rExpandRect1 = new Rectangle(rCompare1);
            Rectangle rExpandRect2 = new Rectangle(rCompare2);

            // inflate the rect by the obstruction buffer for the intersection
            // calculation so that we won't try to route through a space smaller
            // then necessary
            rExpandRect1.expand(inflate, inflate);
            rExpandRect2.expand(inflate, inflate);

            if (rExpandRect1.intersects(rExpandRect2)) {
                rCompare1.union(rCompare2);
                intersectionOccurred = true;
            } else {
                newCollect.add(rCompare2);
            }
        }

        rCompare.setBounds(rCompare1);

        if (newCollect.size() > 0) {
            if (intersectionOccurred) {
                return collapseRects(rCompare, newCollect, inflate);
            } else {
                Rectangle rFirst = new Rectangle((Rectangle) newCollect
                    .remove(0));
                List finalCollapse = collapseRects(rFirst, newCollect, inflate);
                finalCollapse.add(rFirst);

                return finalCollapse;
            }
        } else {
            return newCollect;
        }
    }     
    

    /**
     * @param conn
     * @return
     */
    private IFigure getRouterContainerFigure(Connection conn) {
        IFigure sourcefigContainer = getSourceContainer(conn);
        IFigure targetfigContainer = getTargetContainer(conn);
        IFigure commonFig = FigureUtilities.findCommonAncestor(
            sourcefigContainer, targetfigContainer);

        IFigure routerContainer = null;

        if (sourcefigContainer == null || targetfigContainer == null)
            return null;
        
        if (sourcefigContainer == targetfigContainer) {
            routerContainer = sourcefigContainer;
        } else if (commonFig != sourcefigContainer
            && commonFig != targetfigContainer) {
            routerContainer = commonFig;
        } else {
            // find the end that isn't the common ancestor and use it's bounds
            // to find
            // the optimal end for the avoid obstructions algorithm
            IFigure checkFig = sourcefigContainer;
            if (commonFig == sourcefigContainer)
                checkFig = targetfigContainer;

            // decide which end of the connection exists more in it's container
            // relative
            // to the other end, and use that container to determine which
            // router to
            // return.
            Rectangle checkRect = checkFig.getBounds().getCopy();
            checkFig.translateToAbsolute(checkRect);
            conn.translateToRelative(checkRect);
            int sourceDistance = findDistanceToEndRect(conn.getPoints(),
                checkRect);
            int targetDistance = (int) PointListUtilities.getPointsLength(conn
                .getPoints())
                - sourceDistance;

            if (sourceDistance > targetDistance)
                routerContainer = sourcefigContainer;
            else
                routerContainer = targetfigContainer;
        }

        return routerContainer;
    }


    protected IFigure getSourceContainer(Connection conn) {
        if (conn.getSourceAnchor() != null)
            return findContainerFigure(conn.getSourceAnchor().getOwner());
        
        return null;
    }

    protected IFigure getTargetContainer(Connection conn) {
        if (conn.getTargetAnchor() != null)
            return findContainerFigure(conn.getTargetAnchor().getOwner());
        
        return null;
    }

    /**
     * findContainerFigure Recursive method to find the figure that owns the
     * children the connection is connecting to.
     * 
     * @param fig
     *            IFigure to find the shape container figure parent of.
     * @return Container figure
     */
    private IFigure findContainerFigure(IFigure fig) {
        if (fig == null)
            return null;

        if (fig.getLayoutManager() instanceof XYLayout)
            return fig;

        return findContainerFigure(fig.getParent());
    }

    private int findDistanceToEndRect(PointList points, Rectangle endRect) {
        PointList intersections = new PointList();
        PointList distances = new PointList();
        boolean foundSourceDistance = PointListUtilities.findIntersections(
            points, PointListUtilities.createPointsFromRect(endRect),
            intersections, distances);

        int sourceDistance = foundSourceDistance ? distances.getFirstPoint().x
            : 0;
        return sourceDistance;
}
   
    
    /**
     * Returns anchor location as <code>PrecisionPoint</code>
     * 
     * @param anchor connection anchor object
     * @param reference outside reference point
     * @return <code>PrecisionPoint</code> for anchor location
     */
    private PrecisionPoint getAnchorLocation(ConnectionAnchor anchor, Point reference) {
    	return new PrecisionPoint(anchor.getLocation(reference));
    }
       
    /**
     * Returns anchor reference point as <code>PrecisionPoint</code>
     * 
     * @param anchor connection anchor object
     * @return <code>PrecisionPoint</code> for anchor reference
     */
    private PrecisionPoint getAnchorReference(ConnectionAnchor anchor) {
   		return new PrecisionPoint(anchor.getReferencePoint());
    }
    
	/**
	 * Determines whether the router is going to avoid obstructions during the
	 * routing algorithm.
	 */
	public boolean isAvoidingObstructions(Connection conn) {
		if (conn instanceof PolylineConnectionEx) {
			return ((PolylineConnectionEx) conn).isAvoidObstacleRouting();
		}

		return false;
	}

	
}
