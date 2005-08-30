/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004.  All Rights Reserved.       		       |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.internal.editpolicies;

import java.util.Iterator;
import java.util.List;

import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.swt.dnd.DND;

import org.eclipse.gmf.runtime.diagram.core.internal.commands.CreateDiagramLinkCommand;
import org.eclipse.gmf.runtime.diagram.ui.commands.EtoolsProxyCommand;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.DragDropEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.l10n.PresentationResourceManager;
import org.eclipse.gmf.runtime.diagram.ui.requests.DropObjectsRequest;
import org.eclipse.gmf.runtime.diagram.ui.requests.RequestConstants;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.View;

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
			
					CreateDiagramLinkCommand com = new CreateDiagramLinkCommand(
								PresentationResourceManager.
									getI18NString("Command.CreateDiagramLink"),//$NON-NLS-1$
								view, 
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
