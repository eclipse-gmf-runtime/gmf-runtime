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

import java.util.Collection;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.emf.type.core.requests.DestroyElementRequest;

/**
 * Command to create a model element using the EMF action protocol.
 * 
 * @author ldamus
 */
public class DestroyElementCommand
	extends EditElementCommand {

	/**
	 * The element to be destroyed.
	 */
	private final EObject elementToDestroy;

	/**
	 * Constructs a new command to destroy a model element.
	 * 
	 * @param request
	 *            the destroy element requestO
	 */
	public DestroyElementCommand(DestroyElementRequest request) {

		super(request.getLabel(), request.getContainer(), request);
		this.elementToDestroy = request.getElementToDestroy();
	}

	protected CommandResult doExecute(IProgressMonitor progressMonitor) {

		EReference reference = getElementToDestroy().eContainmentFeature();

		if (reference.isMany()) {
			((Collection) getElementToEdit().eGet(reference))
				.remove(getElementToDestroy());
			
		} else {
			getElementToEdit().eSet(reference, null);
		}
		return newOKCommandResult();
	}
	
	/**
	 * Gets the element to be destroyed.
	 * @return the element to be destroyed
	 */
	protected EObject getElementToDestroy() {
		return elementToDestroy;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.emf.commands.core.internal.commands.EditElementCommand#isExecutable()
	 */
	public boolean isExecutable() {
		return elementToDestroy != null;
	}

}