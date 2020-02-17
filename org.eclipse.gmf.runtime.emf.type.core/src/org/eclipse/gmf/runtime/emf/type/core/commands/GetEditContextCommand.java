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

package org.eclipse.gmf.runtime.emf.type.core.commands;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;

import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.emf.type.core.requests.GetEditContextRequest;

/**
 * Command to get the edit context for a given request.
 * 
 * @author ldamus
 */
public class GetEditContextCommand extends EditElementCommand {

	/**
	 * The edit context.
	 */
	private Object editContext;

	/**
	 * Constructs a new command. Automatically initializes the edit context with
	 * that carried in the <code>request</code>.
	 * 
	 * @param request
	 *            the command request
	 */
	public GetEditContextCommand(GetEditContextRequest request) {
		super(request.getLabel(), null, request);
		setEditContext(request.getEditContext());
	}

	protected CommandResult doExecuteWithResult(IProgressMonitor monitor, IAdaptable info)
	    throws ExecutionException {

		return CommandResult.newOKCommandResult(getEditContext());
	}

	/**
	 * Gets the edit context.
	 * 
	 * @return the edit context
	 */
	public Object getEditContext() {
		return editContext;
	}

	/**
	 * Sets the edit context.
	 * 
	 * @param editContext
	 *            the edit context
	 */
	public void setEditContext(Object editContext) {
		this.editContext = editContext;
		((GetEditContextRequest) getRequest()).setEditContext(editContext);
	}

}