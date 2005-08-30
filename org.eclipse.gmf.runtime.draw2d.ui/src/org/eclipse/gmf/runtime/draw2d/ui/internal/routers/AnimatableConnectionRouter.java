/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.draw2d.ui.internal.routers;

import org.eclipse.draw2d.BendpointConnectionRouter;
import org.eclipse.draw2d.Connection;

import org.eclipse.gmf.runtime.draw2d.ui.internal.figures.AnimationFigureHelper;

/**
 * @author sshaw
 * @canBeSeenBy org.eclipse.gmf.runtime.draw2d.ui.*
 *
 * BendpointConnectionRouter that supports animation
 */
public class AnimatableConnectionRouter
	extends BendpointConnectionRouter {
	
	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.ConnectionRouter#route(org.eclipse.draw2d.Connection)
	 */
	final public void route(Connection conn) {
		if (!AnimationFigureHelper.getInstance().layoutManagerHook(conn))
			routeBendpoints(conn);
	}
	
	/**
	 * routeBendpoints
	 * Method that performs the actual routing of the Connection.  Clients will override this instead
	 * of route which is final.
	 * 
	 * @param conn Connection to be routed.
	 */
	protected void routeBendpoints(Connection conn) {
		super.route(conn);
	}
}
