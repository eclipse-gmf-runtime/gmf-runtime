/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.emf.commands.core.commands;

import org.eclipse.core.runtime.IProgressMonitor;

import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.emf.core.edit.MObjectState;
import org.eclipse.gmf.runtime.emf.core.util.EObjectUtil;
import org.eclipse.gmf.runtime.emf.type.core.commands.DestroyElementCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.DestroyElementRequest;

/**
 * Command to destroy a model element.
 * 
 * @author ldamus
 */
public class MSLDestroyElementCommand
	extends DestroyElementCommand {

	/**
	 * Constructs a new command to destroy a model element.
	 * 
	 * @param request
	 *            the destroy element request
	 */
	public MSLDestroyElementCommand(DestroyElementRequest request) {
		super(request);
	}

	protected CommandResult doExecute(IProgressMonitor progressMonitor) {

		MObjectState elementState = EObjectUtil.getState(getElementToDestroy());

		if (MObjectState.DETACHED != elementState) {
			EObjectUtil.destroy(getElementToDestroy());
		}
		return newOKCommandResult();
	}

}