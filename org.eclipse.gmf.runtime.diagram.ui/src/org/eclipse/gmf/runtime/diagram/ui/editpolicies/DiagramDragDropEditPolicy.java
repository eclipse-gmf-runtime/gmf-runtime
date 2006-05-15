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
import org.eclipse.gef.requests.ChangeBoundsRequest;

import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ListCompartmentEditPart;
import org.eclipse.gmf.runtime.diagram.ui.requests.ArrangeRequest;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateViewRequest;
import org.eclipse.gmf.runtime.diagram.ui.requests.DropObjectsRequest;
import org.eclipse.gmf.runtime.diagram.ui.requests.RefreshConnectionsRequest;
import org.eclipse.gmf.runtime.diagram.ui.requests.RequestConstants;
import org.eclipse.gmf.runtime.emf.core.util.EObjectAdapter;

/**
 * @author choang
 *
 * Default edit policy to handle request of type <code>org.eclipse.gmf.runtime.diagram.ui.internal.requests.DropElementsRequest;<code>.
 * The default behavior will to Create a CreateViewRequest.
 */
public class DiagramDragDropEditPolicy extends DragDropEditPolicy {

	/**
	 * getDropFileCommand
	 * Handler method for the dropRequest as a result of a file transfer drag / drop operation.
	 * @param dropRequest DropObjectsRequest that contains a list of Strings in it's objects list.
	 * @return Command that that create the resulting view of the file if any, null otherwise.
	 */
	protected Command getDropFileCommand(DropObjectsRequest dropRequest) {
		Iterator iter = dropRequest.getObjects().iterator();
		while (iter.hasNext()) {
			Object obj = iter.next();
			if (obj instanceof String) {
				//String filePath = (String)obj;
			}
		}
		
		return null;
	}
	
	/**
	 * @see org.eclipse.gef.EditPolicy#getCommand(Request)
	 */
	public Command getDropObjectsCommand(DropObjectsRequest dropRequest) {
		// Create a view request from the drop request and then forward getting
		// the command for that.

		List viewDescriptors = new ArrayList();
		Iterator iter = dropRequest.getObjects().iterator();

		if (dropRequest.getObjects().size() > 0 &&
			dropRequest.getObjects().get(0) instanceof String) {
			return getDropFileCommand(dropRequest);
		}
		
		while (iter.hasNext())
			viewDescriptors.add(new CreateViewRequest.ViewDescriptor(
				new EObjectAdapter((EObject) iter.next()),
				((IGraphicalEditPart) getHost()).getDiagramPreferencesHint()));

		return createViewsAndArrangeCommand(dropRequest, viewDescriptors);
	}

	/**
	 * createViewsAndArrangeCommand
	 * Method to create all the view based on the viewDescriptors list and provide
	 * a default arrangement of them.
	 * 
	 * @param dropRequest
	 * @param viewDescriptors
	 * @return command
	 */
	protected Command createViewsAndArrangeCommand(DropObjectsRequest dropRequest, List viewDescriptors) {
		CreateViewRequest createViewRequest =
			new CreateViewRequest(viewDescriptors);
		createViewRequest.setLocation(dropRequest.getLocation());
		Command createCommand = getHost().getCommand(createViewRequest);

		if (createCommand != null) {
			List result = (List)createViewRequest.getNewObject();
			dropRequest.setResult(result);

			RefreshConnectionsRequest refreshRequest =
				new RefreshConnectionsRequest(result);
			Command refreshCommand = getHost().getCommand(refreshRequest);

			ArrangeRequest arrangeRequest =
				new ArrangeRequest(RequestConstants.REQ_ARRANGE_DEFERRED);
			arrangeRequest.setViewAdaptersToArrange(result);
			Command arrangeCommand = getHost().getCommand(arrangeRequest);

			CompoundCommand cc = new CompoundCommand(createCommand.getLabel());
			cc.add(createCommand.chain(refreshCommand));
			cc.add(arrangeCommand);
			
			return cc;
		}
		return null;
	}

	/**
	 * @see org.eclipse.gef.EditPolicy#showTargetFeedback(org.eclipse.gef.Request)
	 */
	public void showTargetFeedback(Request request) {
		// no feedback for the diagram
	}

	/**
	 * Handles dropping attributes from a class to the diagram to show as an
	 * association.
	 * @see org.eclipse.gmf.runtime.diagram.ui.editpolicies.DragDropEditPolicy#getDropCommand(org.eclipse.gef.requests.ChangeBoundsRequest)
	 */
	protected Command getDropCommand(ChangeBoundsRequest request) {
		if (request.getEditParts().size() > 0) {
			EditPart parentEP =
				((IGraphicalEditPart) request.getEditParts().get(0)).getParent();
			if (parentEP instanceof ListCompartmentEditPart) {
				Object originalType = request.getType();
				request.setType(RequestConstants.REQ_SHOW_AS_ALTERNATE_VIEW);
				Command cmd = parentEP.getCommand(request);
				request.setType(originalType);
				if (cmd != null) {
					return cmd;
				} 
			}
		}
		return super.getDropCommand(request);
	}

}
