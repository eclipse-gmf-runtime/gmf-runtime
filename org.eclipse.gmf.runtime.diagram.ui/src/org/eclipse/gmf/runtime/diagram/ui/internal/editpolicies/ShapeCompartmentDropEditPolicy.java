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
/*
 * Created on Aug 12, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.eclipse.gmf.runtime.diagram.ui.internal.editpolicies;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.requests.ChangeBoundsRequest;

import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.DragDropEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.requests.ArrangeRequest;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateViewRequest;
import org.eclipse.gmf.runtime.diagram.ui.requests.DropObjectsRequest;
import org.eclipse.gmf.runtime.diagram.ui.requests.RefreshConnectionsRequest;
import org.eclipse.gmf.runtime.diagram.ui.requests.RequestConstants;
import org.eclipse.gmf.runtime.emf.core.util.EObjectAdapter;


/**
 * Supports droping model elements onto the shape compartment editpart.
 * @author mhanner
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.*
 */
public class ShapeCompartmentDropEditPolicy
	extends DragDropEditPolicy {

	/**
	 * Overriden to ensure that we don't drag the top level shape inside the shape compartment itself since this precipitates 
	 * difficult movement behavior of the top level shape.
	 * 
	 * @see org.eclipse.gmf.runtime.diagram.ui.editpolicies.DragDropEditPolicy#getDropCommand(org.eclipse.gef.requests.ChangeBoundsRequest)
	 */
	protected Command getDropCommand(ChangeBoundsRequest request) {
		List editparts = request.getEditParts();
		if (editparts.size() == 1) {
			Object obj = editparts.get(0);
			if (obj instanceof EditPart) {
				EditPart requestEP = (EditPart)obj;
				if (getHost() instanceof IGraphicalEditPart) {
					IGraphicalEditPart gep = (IGraphicalEditPart)getHost();
					if (gep.getTopGraphicEditPart().equals(requestEP)) {
						return null;
					}
				}
			}
		}
		
		return super.getDropCommand(request);
	}
	
	/**
	 * Returns a command to create a view for each of the elements defined
	 * in the supplied request.
	 */
	protected Command getDropObjectsCommand(DropObjectsRequest dropRequest) {
		List viewDescriptors = new ArrayList();
		Iterator iter = dropRequest.getObjects().iterator();

		while (iter.hasNext()) {
			Object obj = iter.next();
			if (obj instanceof EObject) {
				viewDescriptors.add(new CreateViewRequest.ViewDescriptor(
					new EObjectAdapter((EObject) obj),
					((IGraphicalEditPart) getHost())
						.getDiagramPreferencesHint()));
			}
		}
		
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
}
