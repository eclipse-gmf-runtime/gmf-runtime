/******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.emf.type.core.commands;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.emf.core.util.EMFCoreUtil;
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
	
	protected CommandResult doExecuteWithResult(IProgressMonitor monitor, IAdaptable info)
	    throws ExecutionException {

		if (getElementToDestroy().eResource() != null) {
			EMFCoreUtil.destroy(getElementToDestroy());
		}
		return CommandResult.newOKCommandResult();
	}
	
	/**
	 * Gets the element to be destroyed.
	 * @return the element to be destroyed
	 */
	protected EObject getElementToDestroy() {
		return elementToDestroy;
	}

	public boolean canExecute() {
		return elementToDestroy != null;
	}

}