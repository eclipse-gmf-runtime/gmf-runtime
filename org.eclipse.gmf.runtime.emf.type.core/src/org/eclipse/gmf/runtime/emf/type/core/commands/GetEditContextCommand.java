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

	protected CommandResult doExecute(IProgressMonitor progressMonitor) {
		return newOKCommandResult(getEditContext());
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