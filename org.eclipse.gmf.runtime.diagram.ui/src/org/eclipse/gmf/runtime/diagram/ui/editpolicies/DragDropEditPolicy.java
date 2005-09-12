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

package org.eclipse.gmf.runtime.diagram.ui.editpolicies;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.editpolicies.GraphicalEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.tools.ToolUtilities;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.graphics.Color;

import org.eclipse.gmf.runtime.diagram.ui.commands.EtoolsProxyCommand;
import org.eclipse.gmf.runtime.diagram.ui.commands.XtoolsProxyCommand;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.requests.DropObjectsRequest;
import org.eclipse.gmf.runtime.diagram.ui.requests.RequestConstants;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.draw2d.ui.figures.FigureUtilities;
import org.eclipse.gmf.runtime.notation.View;

/**
 * Abstract Implementation for all EditPolicies that deal with the
 * REQ_DRAG, REQ_DROP and REQ_DROP_EOBJECTS requests 
 * as defined in <code>org.eclipse.gmf.runtime.diagram.ui.requests.RequestConstants<code>
 * 
 * The default implementation REQ_DRAG is to cast it into a REQ_ORPHAN request
 * The default implementation REQ_DROP is to cast it into a REQ_ADD request
 * 
 * 
 * @author melaasar
 */
public class DragDropEditPolicy extends GraphicalEditPolicy {

	/**
	 * the background feedback color
	 */
	private static final Color GRAY = new Color(null, 200, 200, 200);

	/**
	 * the original background color
	 */
	protected Color revertColor;

	/**
	 * the original opacity
	 */
	protected boolean opacity;

	/**
	 * This will only return a command if the host is resolvable so that 
	 * trying to drop on an unresolved shape will not be allowed.
	 * @see org.eclipse.gef.EditPolicy#getCommand(Request)
	 */
	public Command getCommand(Request request) {
		if (RequestConstants.REQ_DRAG.equals(request.getType())) {
			return getDragCommand((ChangeBoundsRequest) request);
		} else if (RequestConstants.REQ_DROP.equals(request.getType())) {
			return getDropCommand((ChangeBoundsRequest) request);
		} else if (
			RequestConstants.REQ_DROP_OBJECTS.equals(request.getType())) {
			DropObjectsRequest dndRequest = (DropObjectsRequest) request;
			dndRequest.setRequiredDetail(getRequiredDragDetail(dndRequest));
			return getDropObjectsCommand(dndRequest);
		}
		return null;
	}

	/**
	 * gets a drag command
	 * @param request the request
	 * @return command
	 */
	protected Command getDragCommand(ChangeBoundsRequest request) {
		ChangeBoundsRequest req = new ChangeBoundsRequest(REQ_ORPHAN);
		req.setEditParts(request.getEditParts());
		req.setMoveDelta(request.getMoveDelta());
		req.setSizeDelta(request.getSizeDelta());
		req.setLocation(request.getLocation());
		req.setResizeDirection(request.getResizeDirection());
		return getHost().getCommand(req);
	}

	/**
	 * gets a drop command
	 * @param request the request
	 * @return command
	 */
	protected Command getDropCommand(ChangeBoundsRequest request) {
		ChangeBoundsRequest req = new ChangeBoundsRequest(REQ_ADD);
		req.setEditParts(request.getEditParts());
		req.setMoveDelta(request.getMoveDelta());
		req.setSizeDelta(request.getSizeDelta());
		req.setLocation(request.getLocation());
		req.setResizeDirection(request.getResizeDirection());
		return getHost().getCommand(req);
	}

	/**
	 * getDropElementCommand
	 * Returns a command to handle a DropObjectsRequest of an EObject
	 * 
	 * @param element EObject that is being dropped.
	 * @param request DropObjectsRequest that the element has retrieved from.
	 * @return Command that handles the dropping of the EObject element.
	 */
	protected Command getDropElementCommand(EObject element, DropObjectsRequest request) {
		return null;
	}
	
	/**
	 * getDropObjectsCommand
	 * This provides a generic implementation for handling the 
	 * DropObjectsRequest which checks for EObject and gets a command
	 * from the getDropEementCommand method.
	 * @param request DropObjectsRequest that is being handled
	 * @return Command that handles the request.
	 */
	protected Command getDropObjectsCommand(DropObjectsRequest request) {
		CompoundCommand cm = new CompoundCommand();
		Iterator elements = request.getObjects().iterator();
		while (elements.hasNext()) {
			Object obj = elements.next();
			if (obj instanceof EObject) {
				Command cmd = getDropElementCommand((EObject)obj, request);
			if (cmd != null)
				cm.add(cmd);
		}
		}
		
		if (cm.isEmpty())
			return null;
		
		return new EtoolsProxyCommand(new XtoolsProxyCommand(cm.unwrap()));
	}

	/**
	 * @see org.eclipse.gef.EditPolicy#getTargetEditPart(Request)
	 */
	public EditPart getTargetEditPart(final Request request) {
		return understandsRequest(request) ? getHost() : null;
	}

	/**
	 * Only understands DRAG, DROP and DROP_ELEMENTS requests.
	 * 
	 * @return whether the request is supported
	 */
	public boolean understandsRequest(Request request) {
		return RequestConstants.REQ_MOVE.equals(request.getType())
			|| RequestConstants.REQ_DRAG.equals(request.getType())
			|| RequestConstants.REQ_DROP.equals(request.getType())
			|| RequestConstants.REQ_DROP_OBJECTS.equals(request.getType());
	}

	/**
	 * @see org.eclipse.gef.EditPolicy#showTargetFeedback(org.eclipse.gef.Request)
	 */
	public void showTargetFeedback(Request request) {
		Command c = getCommand(request);
		if (c != null && c.canExecute()) {
			if (revertColor == null) {
				revertColor = getHostFigure().getBackgroundColor();
				opacity = getHostFigure().isOpaque();
				getHostFigure().setBackgroundColor(
					FigureUtilities.mixColors(GRAY, revertColor));
				getHostFigure().setOpaque(true);
			}
		}
	}

	/**
	 * @see org.eclipse.gef.EditPolicy#eraseTargetFeedback(org.eclipse.gef.Request)
	 */
	public void eraseTargetFeedback(Request request) {
		if (revertColor != null) {
			getHostFigure().setBackgroundColor(revertColor);
			getHostFigure().setOpaque(opacity);
			revertColor = null;
		}
	}

	/**
	 * 
	 * @param request
	 * @return int 
	 */
	protected int getRequiredDragDetail(Request request) {
		return DND.DROP_COPY;
	}

	/**
	 * return the host Edit Part's semantic element, if the semantic element
	 * is <code>null</code> or unresolvable it will return <code>null</code>
	 * @return EObject
	 */
	protected EObject getHostObject() {
		return ViewUtil
			.resolveSemanticElement((View) ((IGraphicalEditPart) getHost())
				.getModel());
	}

	/**
	 * Retrieves the list of elements being dropped
	 * @param request the request
	 * @return List of elements
	 */
	protected DropObjectsRequest castToDropObjectsRequest(ChangeBoundsRequest request) {
		Iterator editParts =
			ToolUtilities
				.getSelectionWithoutDependants(request.getEditParts())
				.iterator();

		List elements = new ArrayList();
		while (editParts.hasNext()) {
			EditPart editPart = (EditPart) editParts.next();
			if (editPart instanceof IGraphicalEditPart) {
				EObject element = ViewUtil
					.resolveSemanticElement((View) ((IGraphicalEditPart) editPart)
						.getModel());
				if (element != null)
					elements.add(element);
			}
		}

		DropObjectsRequest req = new DropObjectsRequest();
		req.setObjects(elements);
		req.setAllowedDetail(DND.DROP_COPY | DND.DROP_MOVE | DND.DROP_LINK);
		req.setLocation(request.getLocation());
		req.setRequiredDetail(getRequiredDragDetail(request));
		return req;
	}

}
