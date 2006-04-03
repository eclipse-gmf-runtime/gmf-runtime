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

import org.eclipse.gmf.examples.runtime.diagram.logic.internal.commands.CreateWireCommand;
import org.eclipse.gmf.examples.runtime.diagram.logic.semantic.InputTerminal;
import org.eclipse.gmf.examples.runtime.diagram.logic.semantic.OutputTerminal;
import org.eclipse.gmf.examples.runtime.diagram.logic.semantic.SemanticPackage;
import org.eclipse.gmf.examples.runtime.diagram.logic.semantic.util.LogicSemanticType;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
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
    
    // Default containment features.
    {
        getDefaultContainmentFeatures().put(
            SemanticPackage.eINSTANCE.getElement(),
            SemanticPackage.eINSTANCE.getContainerElement_Children());
    }

	/**
     * Gets a command to create a child in the container element.
     */
	protected ICommand getCreateCommand(CreateElementRequest req) {

		return super.getCreateCommand(req);
	}

	/**
	 * Creates a wire relationship.
	 */
	protected ICommand getCreateRelationshipCommand(
			CreateRelationshipRequest req) {

		if (req.getElementType() == LogicSemanticType.WIRE
            && req.getSource() instanceof OutputTerminal
            && req.getTarget() instanceof InputTerminal) {
            
            return new CreateWireCommand(req);
        }

		return super.getCreateRelationshipCommand(req);
	}

}