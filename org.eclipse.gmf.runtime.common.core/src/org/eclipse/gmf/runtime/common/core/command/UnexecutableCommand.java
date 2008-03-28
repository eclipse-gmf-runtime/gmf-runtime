/******************************************************************************
 * Copyright (c) 2002, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/
package org.eclipse.gmf.runtime.common.core.command;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * A command that cannot be executed. This is an implementation of the Null
 * Object pattern that can be used to provide a command object that can't be
 * executed to clients who require an
 * {@link org.eclipse.gmf.runtime.common.core.command.ICommand}.
 * 
 * @author melaasar
 * @author ldamus
 * 
 * @canBeSeenBy %partners
 */
public class UnexecutableCommand extends AbstractCommand {

	/**
	 * The singleton instance.
	 */
	public static final UnexecutableCommand INSTANCE = new UnexecutableCommand();

	/**
	 * Creates an instance of an unexecutable command.
	 */
	protected UnexecutableCommand() {
		super("", null);
	}

	/**
	 * @throws UnsupportedOperationException
	 */
	protected CommandResult doExecuteWithResult(IProgressMonitor progressMonitor,
			IAdaptable info) throws ExecutionException {
		
		 throw new UnsupportedOperationException("doExecuteWithResult"); //$NON-NLS-1$
	}

	/**
	 * @throws UnsupportedOperationException
	 */
	protected CommandResult doRedoWithResult(IProgressMonitor progressMonitor,
			IAdaptable info) throws ExecutionException {
		
		throw new UnsupportedOperationException("doRedoWithResult"); //$NON-NLS-1$
	}

	/**
	 * @throws UnsupportedOperationException
	 */
	protected CommandResult doUndoWithResult(IProgressMonitor progressMonitor,
			IAdaptable info) throws ExecutionException {
		
		throw new UnsupportedOperationException("doUndoWithResult"); //$NON-NLS-1$
	}

	/**
	 * @return false.
	 */
	public boolean canExecute() {
		return false;
	}

	/**
	 * @return false.
	 */
	public boolean canRedo() {
		return false;
	}

	/**
	 * @return false.
	 */
	public boolean canUndo() {
		return false;
	}
}
