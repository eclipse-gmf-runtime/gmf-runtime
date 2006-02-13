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

import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;

import org.eclipse.gmf.runtime.diagram.core.commands.DeleteCommand;
import org.eclipse.gmf.runtime.diagram.ui.commands.EtoolsProxyCommand;
import org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.SemanticEditPolicy;
import org.eclipse.gmf.runtime.emf.type.core.requests.DestroyElementRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.IEditCommandRequest;

/**
 * This Concrete edit policy will return command(s) in response to
 * semantic requests for non-semantic shapes.
 * 
 * @author Jody Schofield
 * @canBeSeenBy %level1
 */
public class NonSemanticEditPolicy extends SemanticEditPolicy {

	/**
	 * This method overrides the one contained in the SemanticEditPolicy
	 * and if the request is a DestroyElementRequest it prevents the request
	 * from going to the semantic service for a command.
	 * 
	 * @param editRequest edit command request to get a command for
	 * @return Command
	 */
	protected Command getSemanticCommand(IEditCommandRequest editRequest) {

		if ( editRequest instanceof DestroyElementRequest ) {
			if (getHost() instanceof GraphicalEditPart){
				return new EtoolsProxyCommand(new DeleteCommand(editRequest
                    .getEditingDomain(), ((GraphicalEditPart) getHost())
                    .getPrimaryView()));
			}
		}
		return super.getSemanticCommand( editRequest );
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.EditPolicy#getCommand(org.eclipse.gef.Request)
	 */
	public Command getCommand(Request request) {
		if (REQ_RECONNECT_SOURCE.equals(request.getType()) || REQ_RECONNECT_TARGET.equals(request.getType())) {
			return null;
		}
		return super.getCommand(request);
	}
}
