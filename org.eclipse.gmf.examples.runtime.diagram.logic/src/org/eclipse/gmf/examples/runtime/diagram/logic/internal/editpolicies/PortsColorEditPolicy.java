/******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/
package org.eclipse.gmf.examples.runtime.diagram.logic.internal.editpolicies;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.commands.ModifyPortsColorCommand;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.util.StringConstants;
import org.eclipse.gmf.runtime.diagram.ui.commands.ICommandProxy;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.ComponentEditPolicy;

/**
 * Edit policy responsible for handling ports color change requests
 * 
 * @author aboyko
 * 
 */
public class PortsColorEditPolicy extends ComponentEditPolicy {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.diagram.ui.editpolicies.ComponentEditPolicy#getCommand(org.eclipse.gef.Request)
	 */
	public Command getCommand(Request request) {
		if (StringConstants.PORTSCOLOR_REQUEST.equals(request.getType())) {
			IGraphicalEditPart host = (IGraphicalEditPart) getHost();
			Integer color = (Integer) request.getExtendedData().get(
					StringConstants.PORTS_COLOR_PROPERTY_NAME);
			return new ICommandProxy(new ModifyPortsColorCommand(host
					.getEditingDomain(), host, color));
		}
		return super.getCommand(request);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editpolicies.AbstractEditPolicy#getTargetEditPart(org.eclipse.gef.Request)
	 */
	public EditPart getTargetEditPart(Request request) {
		if (StringConstants.PORTSCOLOR_REQUEST.equals(request.getType()))
			return getHost();
		return null;
	}
}
