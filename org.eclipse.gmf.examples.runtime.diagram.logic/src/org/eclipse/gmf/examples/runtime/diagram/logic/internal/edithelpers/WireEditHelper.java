/*
 * Created on May 2, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.eclipse.gmf.examples.runtime.diagram.logic.internal.edithelpers;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.commands.ReorientWireCommand;
import org.eclipse.gmf.examples.runtime.diagram.logic.model.SemanticPackage;
import org.eclipse.gmf.runtime.emf.core.util.EObjectContainmentUtil;
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
	protected ICommand getEditContextCommand(
			final GetEditContextRequest req) {

		IEditCommandRequest editRequest = req.getEditCommandRequest();

		if (editRequest instanceof CreateRelationshipRequest) {
			final CreateRelationshipRequest createRelationshipRequest = (CreateRelationshipRequest) editRequest;

			EObject container = EObjectContainmentUtil
			.findContainerOfAnySubtype(
					createRelationshipRequest.getSource(),
					SemanticPackage.eINSTANCE
							.getContainerElement());
			
			GetEditContextCommand result = new GetEditContextCommand(req);
			result.setEditContext(container);
			return result;
		}
		return null;
	}

	/**
	 * Gets a command to change the source or target of a wire.
	 */
	protected ICommand getReorientRelationshipCommand(
			ReorientRelationshipRequest req) {

		return new ReorientWireCommand(req);
	}
}