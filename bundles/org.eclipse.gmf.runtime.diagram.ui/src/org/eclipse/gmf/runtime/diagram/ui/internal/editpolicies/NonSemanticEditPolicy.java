/******************************************************************************
 * Copyright (c) 2004, 2006 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/


package org.eclipse.gmf.runtime.diagram.ui.internal.editpolicies;

import org.eclipse.gef.commands.Command;
import org.eclipse.gmf.runtime.diagram.core.commands.DeleteCommand;
import org.eclipse.gmf.runtime.diagram.ui.commands.ICommandProxy;
import org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.SemanticEditPolicy;
import org.eclipse.gmf.runtime.emf.type.core.requests.DestroyElementRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.IEditCommandRequest;

/**
 * This Concrete edit policy will return command(s) in response to
 * semantic requests for non-semantic shapes.
 * 
 * @author Jody Schofield
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
				return new ICommandProxy(new DeleteCommand(editRequest
                    .getEditingDomain(), ((GraphicalEditPart) getHost())
                    .getPrimaryView()));
			}
		}
		return super.getSemanticCommand( editRequest );
	}
}
