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