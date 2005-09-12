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