/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.editpolicies;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.AccessibleHandleProvider;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.editpolicies.FeedbackHelper;
import org.eclipse.gef.requests.ReconnectRequest;

import org.eclipse.gmf.runtime.diagram.ui.editparts.ConnectionEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeNodeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.figures.ConnectorEndHandle;
import org.eclipse.gmf.runtime.diagram.ui.figures.ConnectorStartHandle;

/*
 * @canBeSeenBy %partners
 */
/**
 * connector end point edit policy
 * @author mmostafa
 */
public class ConnectorEndpointEditPolicy
	extends org.eclipse.gef.editpolicies.ConnectionEndpointEditPolicy {

	private ConnectionAnchor originalAnchor;

	private FeedbackHelper feedbackHelper;
	
	protected void addSelectionHandles() {
		super.addSelectionHandles();
	}

	/**
	 * gets the connection figure
	 * @return <code>PolylineConnection</code>
	 */
	protected PolylineConnection getConnectionFigure() {
		return (PolylineConnection) ((GraphicalEditPart) getHost()).getFigure();
	}

	protected void removeSelectionHandles() {
		super.removeSelectionHandles();
	}
	
	protected void eraseConnectionMoveFeedback(ReconnectRequest request){
		if (originalAnchor == null)
			return;
		if (request.isMovingStartAnchor())
			getConnection().setSourceAnchor(originalAnchor);
		else
			getConnection().setTargetAnchor(originalAnchor);
		originalAnchor = null;
		feedbackHelper = null;
	}

	protected void showConnectionMoveFeedback(ReconnectRequest request) {
		ShapeNodeEditPart node = null;
		if (request.getTarget() instanceof ShapeNodeEditPart)
			node = (ShapeNodeEditPart) request.getTarget();
		if (originalAnchor == null) {
			if (request.isMovingStartAnchor())
				originalAnchor = getConnection().getSourceAnchor();
			else
				originalAnchor = getConnection().getTargetAnchor();
		}
		ConnectionAnchor anchor = null;
		if (node != null) {
			if (request.isMovingStartAnchor())
				anchor = node.getSourceConnectionAnchor(request);
			else
				anchor = node.getTargetConnectionAnchor(request);
		}
		FeedbackHelper helper = getFeedbackHelper(request);
		Point p = new Point(request.getLocation());
		((IGraphicalEditPart)getHost()).getFigure().translateToAbsolute(p);
		helper.update(anchor, p);
	}

	protected FeedbackHelper getFeedbackHelper(ReconnectRequest request) {
		if (feedbackHelper == null) {
			feedbackHelper = new FeedbackHelper();
			feedbackHelper.setConnection(getConnection());
			feedbackHelper.setMovingStartAnchor(request.isMovingStartAnchor());
		}
		return feedbackHelper;
	}
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.gef.editpolicies.SelectionHandlesEditPolicy#createSelectionHandles()
	 */
	protected List createSelectionHandles() {
		List list = new ArrayList();
		list.add(new ConnectorEndHandle((ConnectionEditPart)getHost()));
		list.add(new ConnectorStartHandle((ConnectionEditPart)getHost()));
		return list;
	}

	/**
	 * Override for AccessibleHandleProvider when deactivated
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=69316
	 * 
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	public Object getAdapter(Class key) {
		if (key == AccessibleHandleProvider.class)
			//handles == null when deactivated
			if (handles == null) {
				return null;
			}
		return super.getAdapter(key);
	}
	
}
