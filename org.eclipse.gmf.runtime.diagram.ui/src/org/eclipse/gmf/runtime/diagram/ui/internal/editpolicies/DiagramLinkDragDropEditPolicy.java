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

package org.eclipse.gmf.runtime.diagram.ui.internal.editpolicies;

import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gmf.runtime.diagram.core.internal.commands.CreateDiagramLinkCommand;
import org.eclipse.gmf.runtime.diagram.ui.commands.EtoolsProxyCommand;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.DragDropEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.l10n.DiagramUIMessages;
import org.eclipse.gmf.runtime.diagram.ui.requests.DropObjectsRequest;
import org.eclipse.gmf.runtime.diagram.ui.requests.RequestConstants;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.swt.dnd.DND;

/**
 * Edit Policy which supports dropping NalDiagramView onto shapes.  The host's 
 * semantic model get sets to the dropped diagram.
 * 
 * @author jcorchis
 * @canBeSeenBy %level1
 */
public class DiagramLinkDragDropEditPolicy extends DragDropEditPolicy {

	/**
	 * @see org.eclipse.gmf.runtime.diagram.ui.editpolicies.DragDropEditPolicy#getCommand(DropElementsRequest)
	 */
	public Command getDropObjectsCommand(DropObjectsRequest request) {

		// Return the non-null command if the dropped selection contains at least
		// one diagram element.
		if (RequestConstants.REQ_DROP_OBJECTS.equals(request.getType())) {
			DropObjectsRequest elements = request;
			Iterator i = elements.getObjects().iterator();
			while (i.hasNext()) {
				Object element = i.next();
				if (element instanceof Diagram) {

					Diagram diagram = (Diagram) element;
					View view = (View)getHost().getModel();
                    

                    TransactionalEditingDomain editingDomain = ((IGraphicalEditPart) getHost())
                        .getEditingDomain();
			
					CreateDiagramLinkCommand com = new CreateDiagramLinkCommand(
                        editingDomain,
                        DiagramUIMessages.Command_CreateDiagramLink, view,
                        diagram);

					return new EtoolsProxyCommand(com);
				}
			}
		}

		return null;
	}

	/**
	 * @see org.eclipse.gmf.runtime.diagram.ui.editpolicies.DragDropEditPolicy#understandsRequest()
	 */
	public boolean understandsRequest(Request request) {
		if (RequestConstants.REQ_DROP_OBJECTS.equals(request.getType())) {
			List elements = ((DropObjectsRequest) request).getObjects();
			return elements.size() == 1 && elements.get(0) instanceof Diagram;
		}
		return super.understandsRequest(request);

	}

	/**
	 * @see org.eclipse.gef.EditPolicy#showTargetFeedback(org.eclipse.gef.Request)
	 */
	public void showTargetFeedback(Request request) {
		if (understandsRequest(request))
			super.showTargetFeedback(request);
	}

	/**
	 * @see org.eclipse.gmf.runtime.diagram.ui.editpolicies.DragDropEditPolicy#getDragOperation(org.eclipse.gef.Request)
	 */
	protected int getRequiredDragDetail(Request request) {
		if (request instanceof DropObjectsRequest){
			DropObjectsRequest req = (DropObjectsRequest) request;
			if ((req.getAllowedDetail() & DND.DROP_LINK) != 0)
				return DND.DROP_LINK;
			return DND.DROP_COPY;
		}
		return super.getRequiredDragDetail(request);
	}	
	
}
