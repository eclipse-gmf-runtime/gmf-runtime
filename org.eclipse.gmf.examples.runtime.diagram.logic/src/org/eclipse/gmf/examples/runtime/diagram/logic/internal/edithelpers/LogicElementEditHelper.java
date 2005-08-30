/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.examples.runtime.diagram.logic.internal.edithelpers;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.emf.commands.core.edithelpers.MSLEditHelper;
import org.eclipse.gmf.runtime.emf.type.core.commands.GetEditContextCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateElementRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.GetEditContextRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.IEditCommandRequest;
import com.ibm.xtools.notation.Diagram;

/**
 * Superclass for all of the logic element edit helpers.
 * 
 * @author ldamus
 * @canBeSeenBy org.eclipse.gmf.examples.runtime.diagram.logic.*
 */
public class LogicElementEditHelper
	extends MSLEditHelper {

	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.emf.type.core.edithelper.AbstractEditHelper#getContainerCommand(org.eclipse.gmf.runtime.emf.type.core.requests.GetContainerRequest)
	 */
	protected ICommand getEditContextCommand(GetEditContextRequest req) {

		GetEditContextCommand result = null;
		
		IEditCommandRequest editRequest = req.getEditCommandRequest();
		
		if (editRequest instanceof CreateElementRequest) {
			result = new GetEditContextCommand(req);
			EObject container = ((CreateElementRequest) editRequest).getContainer();

			if (container instanceof Diagram) {
				EObject element = ((Diagram) container).getElement();
	
				if (element == null) {
					// Element is null if the diagram was created using the wizard
					EObject annotation = ((Diagram) container).eContainer();
	
					if (annotation != null) {
						element = annotation.eContainer();
					}
				}
				container = element;
			}
			result.setEditContext(container);
		}
		return result;
	}
}