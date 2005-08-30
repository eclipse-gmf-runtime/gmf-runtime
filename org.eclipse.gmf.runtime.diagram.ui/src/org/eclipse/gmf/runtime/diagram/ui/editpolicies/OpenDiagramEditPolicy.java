/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004.  All Rights Reserved.       		       |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.editpolicies;


import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;

import org.eclipse.gmf.runtime.diagram.ui.commands.EtoolsProxyCommand;
import org.eclipse.gmf.runtime.diagram.ui.commands.OpenDiagramCommand;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.View;

/**
 * Edit Policy which opens the diagram corresponding to the host's semantic model.
 * 
 * @author jcorchis
 */
public class OpenDiagramEditPolicy extends OpenEditPolicy {

	/** 
	 * Returns a Command to open a diagram for the given NalDiagramView.
	 * @return Command
	 * @see org.eclipse.gmf.runtime.diagram.ui.editpolicies.OpenEditPolicy#getOpenCommand(org.eclipse.gef.Request)
	 */
	protected Command getOpenCommand(Request request) {
		EditPart targetEditPart = getTargetEditPart(request);
		if (targetEditPart instanceof IGraphicalEditPart) {
			IGraphicalEditPart editPart = (IGraphicalEditPart)targetEditPart;
			View view = editPart.getNotationView();
			if (view !=null){
				EObject element = ViewUtil.resolveSemanticElement(view);
				if (element instanceof Diagram) {
					return new EtoolsProxyCommand(
						new OpenDiagramCommand(element));
				}
			}
		}
		return null;
	}

}
