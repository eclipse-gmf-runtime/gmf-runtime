/******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.examples.runtime.diagram.logic.internal.edithelpers;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.emf.type.core.commands.GetEditContextCommand;
import org.eclipse.gmf.runtime.emf.type.core.edithelper.AbstractEditHelper;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateElementRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.GetEditContextRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.IEditCommandRequest;
import org.eclipse.gmf.runtime.notation.Diagram;

/**
 * Superclass for all of the logic element edit helpers.
 * 
 * @author ldamus
 * @canBeSeenBy org.eclipse.gmf.examples.runtime.diagram.logic.*
 */
public class LogicElementEditHelper
	extends AbstractEditHelper {

	
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