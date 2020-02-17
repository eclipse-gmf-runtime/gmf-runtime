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

package org.eclipse.gmf.examples.runtime.diagram.logic.internal.commands;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.examples.runtime.diagram.logic.semantic.InputTerminal;
import org.eclipse.gmf.examples.runtime.diagram.logic.semantic.OutputTerminal;
import org.eclipse.gmf.examples.runtime.diagram.logic.semantic.SemanticPackage;
import org.eclipse.gmf.examples.runtime.diagram.logic.semantic.Wire;
import org.eclipse.gmf.runtime.emf.core.util.EMFCoreUtil;
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
		Wire oWire = (Wire) EMFCoreUtil.create(getElementToEdit(),
			getContainmentFeature(), getElementType().getEClass());

		oWire.setSource((OutputTerminal) getSource());
		oWire.setTarget((InputTerminal) getTarget());

		return oWire;
	}

	/**
	 * A wire can only be created when the source is an output terminal and the
	 * target is an input terminal.
	 */
	public boolean canExecute() {

		if ((getSource() instanceof OutputTerminal &&
			getTarget() instanceof InputTerminal)) {
			return super.canExecute();
		}
		
		return false;
	}

}