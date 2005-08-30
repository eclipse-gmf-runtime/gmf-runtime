/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.examples.runtime.diagram.logic.internal.commands;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.gmf.examples.runtime.diagram.logic.model.InputTerminal;
import org.eclipse.gmf.examples.runtime.diagram.logic.model.OutputTerminal;
import org.eclipse.gmf.examples.runtime.diagram.logic.model.SemanticPackage;
import org.eclipse.gmf.examples.runtime.diagram.logic.model.Wire;
import org.eclipse.gmf.runtime.emf.core.util.EObjectUtil;
import org.eclipse.gmf.runtime.emf.type.core.commands.CreateRelationshipCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateRelationshipRequest;

/**
 * Command to create a new wire element.
 * 
 * @author ldamus
 */
public class CreateWireCommand
	extends CreateRelationshipCommand {

	/**
	 * Constructs a new command to create a wire element.
	 * 
	 * @param request
	 *            the create request
	 */
	public CreateWireCommand(CreateRelationshipRequest request) {
		super(request);
		setEClass(SemanticPackage.eINSTANCE.getContainerElement());
	}

	/**
	 * Creates a wire and sets its source and target.
	 */
	protected EObject doDefaultElementCreation() {
		Wire oWire = (Wire) EObjectUtil.create(getElementToEdit(),
			getContainmentFeature(), getElementType().getEClass());

		oWire.setSource((OutputTerminal) getSource());
		oWire.setTarget((InputTerminal) getTarget());

		return oWire;
	}

	/**
	 * A wire can only be created when the source is an output terminal and the
	 * target is an input terminal.
	 */
	public boolean isExecutable() {

		if (!(getSource() instanceof OutputTerminal)) {
			return false;
		}
		if (getTarget() != null && !(getTarget() instanceof InputTerminal)) {
			return false;
		}

		return super.isExecutable();
	}

}