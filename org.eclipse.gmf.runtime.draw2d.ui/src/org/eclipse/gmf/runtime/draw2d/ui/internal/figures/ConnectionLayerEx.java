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

package org.eclipse.gmf.runtime.draw2d.ui.internal.figures;

import java.util.List;
import java.util.ListIterator;

import org.eclipse.draw2d.AutomaticRouter;
import org.eclipse.draw2d.ConnectionLayer;
import org.eclipse.draw2d.ConnectionRouter;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;

import org.eclipse.gmf.runtime.draw2d.ui.figures.PolylineConnectionEx;
import org.eclipse.gmf.runtime.draw2d.ui.internal.routers.FanRouter;
import org.eclipse.gmf.runtime.draw2d.ui.internal.routers.ForestRouter;
import org.eclipse.gmf.runtime.draw2d.ui.internal.routers.ObliqueRouter;
import org.eclipse.gmf.runtime.draw2d.ui.internal.routers.RectilinearRouter;


/**
 * This class provides a hook for the default routers available through the drawing
 * surface.  Specifically, the oblique, rectilinear and tree routers.
 * 
 * @author sshaw
 * @canBeSeenBy %partners
 */
public class ConnectionLayerEx extends ConnectionLayer {

	private boolean dirtied = false;
	static private boolean allowJumpLinks = true;
	
	/**
	 * Adds the given figure with the given contraint at the given index.
	 * If the figure is a connection, its connection router is set.
	 * In addition this method override will dirty the jump link
	 * information so that next display update they can be regenerated.
	 *
	 * @param figure  <code>IFigure</code> being added.
	 * @param constraint  Constraint of the figure being added.
	 * @param index  Index where the figure is to be added. 
	 */
	public void add(IFigure figure, Object constraint, int index) {
		super.add(figure, constraint, index);

		dirtyJumpLinks(figure.getBounds());
	}

	/**
	 * Removes the given figure from the connection layer.
	 * In addition this method override will dirty the jump link
	 * information so that next display update they can be regenerated.
	 *
	 * @param figure  <code>IFigure</code> being removed.
	 */
	public void remove(IFigure figure) {
		super.remove(figure);

		dirtyJumpLinks(figure.getBounds());
	}

	/**
	 * Determines if we should jump links at all in the connection layer.
	 * To address scaleability concerns, we turn off jump links while editing connections.
	 * 
	 * @return <code>boolean</code> <code>true</code> if the jump links capability is currently
	 * available, <code>false</code> otherwise.
	 */
	static public boolean shouldJumpLinks() {
		return allowJumpLinks;
	}
	
	/**
	 * Method setJumpLinks
	 * This method will set if we should jump links at all in the connection layer.
	 * To address scaleability concerns, we turn off jump links while editing connections.
	 * 
	 * @param set the current state of the jump links capability based on the <code>boolean</code> 
	 * value passed.  <code>true</code> indicates that jump links is available, <code>false</code>
	 * otherwise.
	 */
	static public void setJumpLinks(boolean set) {
		allowJumpLinks = set;
	}
	
	/**
	 * Method cleanJumpLinks
	 * This method will reset the "dirtied" flag so that we know to redirty
	 * all the connectors when the method "dirtyJumpLinks" is called.
	 */
	public void cleanJumpLinks() {
		dirtied = false;
	}
	
	/**
	 * Iterates through all the connections and set a 
	 * flag indicating that the jump link information needs to be updated.
	 * At display time, the connection will regenerate the jump link information.
	 * 
	 * @param region the <code>Rectangle</code> that indicates the dirty region inside
	 * which the jump links calculations should be regenerated.
	 */
	public void dirtyJumpLinks(Rectangle region) {
		
		if (!dirtied && shouldJumpLinks()) {
			List children = getChildren();
			ListIterator childIter = children.listIterator();
			while (childIter.hasNext()) {
				IFigure poly = (IFigure) childIter.next();
				if (poly instanceof PolylineConnectionEx)
					if (poly.getBounds().intersects(region))
						((PolylineConnectionEx) poly).refreshLine();
			}
			dirtied = true;
		}
	}

	/**
	 * Returns the connection router being used by this layer.
	 *
	 * @return  Connection router being used by this layer.
	 * @see  #setConnectionRouter(ConnectionRouter)
	 */
	public ConnectionRouter getConnectionRouter() {
		return getObliqueRouter();
	}

	private ConnectionRouter obliqueRouter = null;
	private ConnectionRouter rectilinearRouter = null;
	private ConnectionRouter treeRouter = null;

	/**
	 * Provides an access point to the oblique router for the entire
	 * layer.  Each connection will contain a reference to this router so that
	 * the router can keep track of overlapping connections and reroute accordingly.
	 * 
	 * @return the <code>ConnectionRouter</code> that handles oblique style routing.
	 */
	public ConnectionRouter getObliqueRouter() {
		if (obliqueRouter == null) {
			AutomaticRouter router = new FanRouter();
			router.setNextRouter(new ObliqueRouter());
			obliqueRouter = router;
		}

		return obliqueRouter;
	}

	/**
	 * Provides an access point to the rectilinear router for the entire
	 * layer.  Each connection will contain a reference to this router so that
	 * the router can keep track of overlapping connections and reroute accordingly.
	 * 
	 * @return the <code>ConnectionRouter</code> that handles rectilinear style routing.
	 */
	public ConnectionRouter getRectilinearRouter() {
		if (rectilinearRouter == null)
			rectilinearRouter = new RectilinearRouter();

		return rectilinearRouter;
	}
	
	/**
	 * This method provides an access point to the tree router for the entire
	 * layer.  Each connection will contain a reference to this router so that
	 * the router can keep track of overlapping connections and reroute accordingly.
	 * 
	 * @return the <code>ConnectionRouter</code> that handles tree style routing.
	 */
	public ConnectionRouter getTreeRouter() {
		if (treeRouter == null)
			treeRouter = new ForestRouter();

		return treeRouter;
	}
}
