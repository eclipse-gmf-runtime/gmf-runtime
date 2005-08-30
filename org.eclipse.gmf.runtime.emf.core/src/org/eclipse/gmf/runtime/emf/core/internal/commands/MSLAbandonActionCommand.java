/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.emf.core.internal.commands;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import org.eclipse.gmf.runtime.emf.core.internal.domain.MSLEditingDomain;
import org.eclipse.gmf.runtime.emf.core.internal.plugin.MSLPlugin;
import org.eclipse.gmf.runtime.emf.core.internal.plugin.MSLStatusCodes;

/**
 * A special command that causes the write action to be abandoned, with a
 * user-consumable message.
 * 
 * @author Christian W. Damus
 */
public class MSLAbandonActionCommand
	extends MSLAbstractCommand {

	private IStatus status;

	/**
	 * Initializes me with my localized error message.
	 * 
	 * @param domain the editing domain
	 * @param message the user-readable message
	 */
	public MSLAbandonActionCommand(MSLEditingDomain domain, String message) {
		super(domain);
		this.status = new Status(
			IStatus.ERROR,
			MSLPlugin.getPluginId(),
			MSLStatusCodes.OPERATION_FAILED,
			message,
			null);
	}

	public void dispose() {

		super.dispose();

		status = null;
	}
	
	/**
	 * Creates the action abandonment status to return to the user.
	 * 
	 * @return the status (error severity)
	 */
	public IStatus getStatus() {
		return status;
	}

	public Type getType() {
		return MCommand.ABANDON_ACTION;
	}
	
	public void execute() {
		// nothing to do
	}
	
	public void undo() {
		// nothing to do
	}
	
	public void redo() {
		// nothing to do
	}
}