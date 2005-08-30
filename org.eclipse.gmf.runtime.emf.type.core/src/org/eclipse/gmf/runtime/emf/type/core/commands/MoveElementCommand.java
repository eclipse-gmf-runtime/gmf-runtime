/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.emf.type.core.commands;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.emf.type.core.requests.MoveRequest;

/**
 * Command to move a model element using the EMF action protocol.
 * <P>
 * If the target feature is not specified in the request, the first feature in
 * the targetcontainer that can contain the new kind of element will be used.
 * 
 * @author ldamus
 */
public class MoveElementCommand
	extends EditElementCommand {

	/**
	 * The element to be moved.
	 */
	private final EObject elementToMove;

	/**
	 * The target container into which the element will be moved.
	 */
	private final EObject targetContainer;

	/**
	 * The target feature into which the element will be moved.
	 */
	private EReference targetFeature;

	/**
	 * Constructs a new command to move a model element
	 * 
	 * @param request
	 *            the move element request
	 */
	public MoveElementCommand(MoveRequest request) {

		super(request.getLabel(), request.getTargetContainer(), request);
		this.elementToMove = request.getElementToMove();
		this.targetContainer = request.getTargetContainer();
		this.targetFeature = request.getTargetFeature();
	}

	protected CommandResult doExecute(IProgressMonitor progressMonitor) {

		List list = (List) targetContainer.eGet(getTargetFeature());
		list.add(elementToMove);

		return newOKCommandResult();
	}

	/**
	 * Gets the target feature into which the element will be moved.
	 * 
	 * @return the target feature
	 */
	protected EReference getTargetFeature() {
		return targetFeature;
	}

	/**
	 * Gets the target container.
	 * 
	 * @return the target container
	 */
	protected EObject getTargetContainer() {
		return targetContainer;
	}

	protected EObject getElementToMove() {
		return elementToMove;
	}

	/**
	 * Sets the target feature.
	 * 
	 * @param targetFeature
	 *            the target feature
	 */
	protected void setTargetFeature(EReference targetFeature) {
		this.targetFeature = targetFeature;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.emf.commands.core.internal.commands.EditElementCommand#isExecutable()
	 */
	public boolean isExecutable() {

		if (targetContainer == null || elementToMove == null) {
			return false;
		}

		// IF the element is already in the target container...
		if (targetContainer.equals(elementToMove.eContainer())
			&& getTargetFeature() == elementToMove.eContainmentFeature()) {
			// Don't allow the reparenting
			return false;
		}

		return true;
	}

}