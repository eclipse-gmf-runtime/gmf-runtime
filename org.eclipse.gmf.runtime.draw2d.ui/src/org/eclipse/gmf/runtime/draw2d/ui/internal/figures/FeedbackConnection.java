/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.draw2d.ui.internal.figures;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.AbsoluteBendpoint;
import org.eclipse.draw2d.Bendpoint;
import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.ConnectionRouter;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.jface.util.Assert;

import org.eclipse.gmf.runtime.draw2d.ui.figures.PolylineConnectionEx;
import org.eclipse.gmf.runtime.draw2d.ui.geometry.PointListUtilities;

/*
 * @canBeSeenBy %level0
 */
public class FeedbackConnection extends PolylineConnectionEx {

	private Connection master = null;
	private List masterConstraint = null;

	public FeedbackConnection() {
		setLineStyle(Graphics.LINE_DASHDOT);
	}
		
	public FeedbackConnection(Connection master) {
		this();
		
		this.master = master;
		
		PointList pts = PointListUtilities.copyPoints(master.getPoints());
		setPoints(pts);
		setSourceAnchor(master.getSourceAnchor());
		setTargetAnchor(master.getTargetAnchor());
		setForegroundColor(master.getForegroundColor());
		
		if (master instanceof PolylineConnectionEx) {
			setSmoothness(((PolylineConnectionEx)master).getSmoothness());
			setRoutingStyles(((PolylineConnectionEx)master).isClosestDistanceRouting(),
							 ((PolylineConnectionEx)master).isAvoidObstacleRouting());
		}
			
		// Update the constraint based on the current figure
		ConnectionRouter masterRouter = master.getConnectionRouter();
		
		masterConstraint = (List)master.getRoutingConstraint();
		List newConstraint = new ArrayList(masterConstraint.size());
		
		setConnectionRouter(masterRouter);
		masterRouter.remove(master);

		for (int i=0; i<masterConstraint.size(); i++) {
			Bendpoint bp = (Bendpoint)masterConstraint.get(i);
			newConstraint.add( new AbsoluteBendpoint(bp.getLocation()) );
		}
		
		Assert.isTrue(newConstraint.size() >= 2 && newConstraint.size() == masterConstraint.size());
		setRoutingConstraint(newConstraint);
	}
	
	public void dispose() {
		if (getConnectionRouter() != null)
			getConnectionRouter().remove(this);
		setRoutingConstraint(null);
		
		if (getSourceAnchor() != null)
			getSourceAnchor().removeAnchorListener(this);
		if (getTargetAnchor() != null)
			getTargetAnchor().removeAnchorListener(this);

		if (master != null && masterConstraint != null) {
			master.setRoutingConstraint(masterConstraint);
		}
	}
}

