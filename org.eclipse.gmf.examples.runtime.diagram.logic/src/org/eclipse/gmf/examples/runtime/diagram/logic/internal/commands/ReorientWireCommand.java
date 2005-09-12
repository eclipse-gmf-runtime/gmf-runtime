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

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.EObject;

import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.examples.runtime.diagram.logic.model.InputTerminal;
import org.eclipse.gmf.examples.runtime.diagram.logic.model.OutputTerminal;
import org.eclipse.gmf.examples.runtime.diagram.logic.model.Wire;
import org.eclipse.gmf.runtime.emf.type.core.commands.EditElementCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.ReorientRelationshipRequest;

/**
 * Command to change the source or target of a wire.
 * 
 * @author ldamus
 */
public class ReorientWireCommand
	extends EditElementCommand {

	/**
	 * The reorient direction.
	 */
	private final int reorientDirection;

	/**
	 * The relationship's new source or target.
	 */
	private final EObject newEnd;

	/**
	 * Constructs a new command.
	 * 
	 * @param request
	 *            the reorient relationship request
	 */
	public ReorientWireCommand(ReorientRelationshipRequest request) {
		super(request.getLabel(), request.getRelationship(), request);
		this.reorientDirection = request.getDirection();
		this.newEnd = request.getNewRelationshipEnd();
	}

	/**
	 * Reorients the wire.
	 */
	protected CommandResult doExecute(IProgressMonitor progressMonitor) {

		Wire wire = (Wire) getElementToEdit();
		if (reorientDirection == ReorientRelationshipRequest.REORIENT_SOURCE) {
			wire.setSource((OutputTerminal) newEnd);

		} else if (reorientDirection == ReorientRelationshipRequest.REORIENT_TARGET) {
			wire.setTarget((InputTerminal) newEnd);
		}
		return newOKCommandResult(wire);
	}

	/**
	 * The source can be changed to a new output terminal. The target can be
	 * changed to a new target terminal.
	 */
	public boolean isExecutable() {

		if (reorientDirection == ReorientRelationshipRequest.REORIENT_SOURCE
			&& !(newEnd instanceof OutputTerminal)) {
			return false;
		}

		if (reorientDirection == ReorientRelationshipRequest.REORIENT_TARGET
			&& !(newEnd instanceof InputTerminal)) {
			return false;
		}
		return super.isExecutable();
	}
}