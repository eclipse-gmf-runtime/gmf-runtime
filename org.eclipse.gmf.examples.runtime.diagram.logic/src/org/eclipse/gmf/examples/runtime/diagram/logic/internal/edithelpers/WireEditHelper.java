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
package org.eclipse.gmf.examples.runtime.diagram.logic.internal.edithelpers;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.commands.ReorientWireCommand;
import org.eclipse.gmf.examples.runtime.diagram.logic.semantic.InputTerminal;
import org.eclipse.gmf.examples.runtime.diagram.logic.semantic.OutputTerminal;
import org.eclipse.gmf.examples.runtime.diagram.logic.semantic.SemanticPackage;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.emf.core.util.EMFCoreUtil;
import org.eclipse.gmf.runtime.emf.type.core.commands.GetEditContextCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateRelationshipRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.GetEditContextRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.IEditCommandRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.ReorientRelationshipRequest;

/**
 * Edit helper for logic wire elements.
 * 
 * @author ldamus
 * @canBeSeenBy org.eclipse.gmf.examples.runtime.diagram.logic.*
 */
public class WireEditHelper
	extends LogicElementEditHelper {

	/**
	 * Gets a command to determine the container for a new wire element.
	 */
	protected ICommand getEditContextCommand(final GetEditContextRequest req) {

		IEditCommandRequest editRequest = req.getEditCommandRequest();

		if (editRequest instanceof CreateRelationshipRequest) {
			final CreateRelationshipRequest createRelationshipRequest = (CreateRelationshipRequest) editRequest;

			if (hasValidSourceAndTarget(createRelationshipRequest)) {

				// Get the nearest container element to own the new wire.
				EObject container = EMFCoreUtil.getContainer(
					createRelationshipRequest.getSource(),
					SemanticPackage.eINSTANCE.getContainerElement());

				GetEditContextCommand result = new GetEditContextCommand(req);
				result.setEditContext(container);
				return result;
			}
		}
		return null;
	}
	
	/**
	 * Checks the source and target elements in
	 * <code>createRelationshipRequest</code>. Wires can only be created from
	 * an <code>OutputTerminal</code> to and <code>InputTerminal</code>.
	 * 
	 * @param createRelationshipRequest
	 *            the request
	 * @return <code>true</code> if the source and target are valid,
	 *         <code>false</code> otherwise.
	 */
	private boolean hasValidSourceAndTarget(
			CreateRelationshipRequest createRelationshipRequest) {

		// If source is specified, it must be an output terminal.
		EObject source = createRelationshipRequest.getSource();
		if (source != null && !(source instanceof OutputTerminal)) {
			return false;
		}

		// If target is specified, it must be an input terminal.
		EObject target = createRelationshipRequest.getTarget();
		if (target != null && !(target instanceof InputTerminal)) {
			return false;
		}
		
		return true;
	}

	/**
	 * Gets a command to change the source or target of a wire.
	 */
	protected ICommand getReorientRelationshipCommand(
			ReorientRelationshipRequest req) {

		return new ReorientWireCommand(req);
	}
}