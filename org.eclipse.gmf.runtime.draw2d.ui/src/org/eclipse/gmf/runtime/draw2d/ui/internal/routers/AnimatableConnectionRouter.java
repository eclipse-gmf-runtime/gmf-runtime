/******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

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
