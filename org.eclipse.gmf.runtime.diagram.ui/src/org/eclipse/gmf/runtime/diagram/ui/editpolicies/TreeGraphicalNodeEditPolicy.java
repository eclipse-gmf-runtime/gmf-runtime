/******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.editpolicies;

import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateConnectionRequest;

import org.eclipse.gmf.runtime.diagram.ui.editparts.ConnectionEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.INodeEditPart;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.notation.Routing;
import org.eclipse.gmf.runtime.notation.View;

/**
 * This class overrides the default GraphicalNodeEditPolicy to allow for a
 * redirection of the target editpart when connecting. Specifically it used for
 * connections of the same semantic type and will route them using the tree
 * routing. The common example is with generalizations. It is typical for an
 * inheritance hierarchy to be viewed as a tree.
 * 
 * @author sshaw
 */
public class TreeGraphicalNodeEditPolicy extends GraphicalNodeEditPolicy {

	/**
	 * isTreeConnection Method determines if the user gesture that spawned the
	 * request should result in the connection being made into a tree view. The
	 * criteria is whether the semantic type being requested is the same as the
	 * semantic type being targeted.
	 * 
	 * @param request
	 *            Request that is sent from the user gesture
	 * @return boolean true if connection should be made into a tree, false
	 *         otherwise.
	 */
	private boolean isTreeConnection(Request request) {
		IGraphicalEditPart editPart = (IGraphicalEditPart)getHost();
		View view = editPart.getNotationView();
		String modelHint = ViewUtil.getSemanticElementClassId(view);
		String hint = getSemanticHint(request);

		if (modelHint.equals(hint)) {
			return true;
		}

		return false;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.diagram.ui.editpolicies.GraphicalNodeEditPolicy#getConnectionCompleteEditPart(org.eclipse.gef.Request)
	 */
	protected INodeEditPart getConnectionCompleteEditPart(Request request) {
		if (isTreeConnection(request)) {
			return (INodeEditPart) ((ConnectionEditPart) getHost()).getTarget();
		}

		return super.getConnectionCompleteEditPart(request);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.editpolicies.GraphicalNodeEditPolicy#getConnectionTargetAnchor(org.eclipse.gef.Request)
	 */
	protected ConnectionAnchor getConnectionTargetAnchor(Request request) {
		if (isTreeConnection(request)) {
			INodeEditPart nep = getConnectionCompleteEditPart(request);
			return nep.getTargetConnectionAnchor((ConnectionEditPart)getHost());
		}
		
		return super.getConnectionTargetAnchor(request);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy#getConnectionCompleteCommand(org.eclipse.gef.requests.CreateConnectionRequest)
	 */
	protected Command getConnectionCompleteCommand(CreateConnectionRequest request) {
		Command cmd = super.getConnectionCompleteCommand(request);

		Command cmdRouter = getRoutingAdjustment(getViewAdapter(), 
												getSemanticHint(request), Routing.TREE_LITERAL, 
												request.getTargetEditPart());
		if (cmdRouter != null) {
			cmd = cmd == null ? cmdRouter : cmd.chain(cmdRouter);
		}
		return cmd;
	}

}
