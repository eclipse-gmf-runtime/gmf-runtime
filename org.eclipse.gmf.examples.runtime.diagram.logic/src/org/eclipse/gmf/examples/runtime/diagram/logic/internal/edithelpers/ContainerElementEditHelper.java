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

import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.commands.CreateWireCommand;
import org.eclipse.gmf.examples.runtime.diagram.logic.model.SemanticPackage;
import org.eclipse.gmf.examples.runtime.diagram.logic.model.util.LogicSemanticType;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateElementRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateRelationshipRequest;

/**
 * Edit helper for logic container elements.
 * 
 * @author ldamus
 * @canBeSeenBy org.eclipse.gmf.examples.runtime.diagram.logic.*
 */
public class ContainerElementEditHelper
	extends LogicElementEditHelper {

	/**
	 * Gets a command to create a child in the container element.
	 */
	protected ICommand getCreateCommand(CreateElementRequest req) {

		setDefaultContainmentFeature(req);
		return super.getCreateCommand(req);
	}

	/**
	 * Creates a wire relationship.
	 */
	protected ICommand getCreateRelationshipCommand(
			CreateRelationshipRequest req) {

		setDefaultContainmentFeature(req);

		if (req.getElementType() == LogicSemanticType.WIRE) {
			return new CreateWireCommand(req);
		}

		return super.getCreateRelationshipCommand(req);
	}

	/**
	 * Sets the containment feature in <code>req</code> to the default, if it
	 * hasn't been specified.
	 * 
	 * @param req
	 *            the create request
	 */
	private void setDefaultContainmentFeature(CreateElementRequest req) {

		if (req.getContainmentFeature() == null) {
			req.setContainmentFeature(SemanticPackage.eINSTANCE
				.getContainerElement_Children());
		}
	}

}