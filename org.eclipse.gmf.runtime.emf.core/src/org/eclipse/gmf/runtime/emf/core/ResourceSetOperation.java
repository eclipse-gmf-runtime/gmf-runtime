/******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.emf.core;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;


/**
 * An abstract base class for operations that an
 * <code>EditingDomain</code> executes.
 * <p>
 * API clients should <b>not</b> extend this class directly.
 * </p>
 * @see org.eclipse.gmf.runtime.emf.core.EditingDomain#run(ResourceSetOperation, IProgressMonitor)
 */
public abstract class ResourceSetOperation {

	private IStatus result = null;

	/**
	 * Returns the result from this operation's execution. <code>null</code>
	 * if the operation has not executed yet.
	 * 
	 * @return The operation result
	 */
	public IStatus getResult() {
		return result;
	}

	/**
	 * Sets the result of this operation. This function is expected to be called
	 * only by the modeling platform.
	 * 
	 * @param resultIn
	 *            The result of this operation execution
	 */
	public void setResult(IStatus resultIn) {
		result = resultIn;
	}

	/**
	 * Runs this operation.  Progress should be reported to the given progress monitor.
	 * A request to cancel the operation should be honored and acknowledged 
	 * by throwing <code>InterruptedException</code>.
	 *
	 * @param monitor the progress monitor to use to display progress and receive
	 *   requests for cancelation
	 * @exception InvocationTargetException if the run method must propagate a checked exception,
	 * 	it should wrap it inside an <code>InvocationTargetException</code>; runtime exceptions are automatically
	 *  handled by the calling context
	 * @exception InterruptedException if the operation detects a request to cancel, 
	 *  using <code>IProgressMonitor.isCanceled()</code>, it should exit by throwing 
	 *  <code>InterruptedException</code>
	 *
	 * @see IProgressMonitor
	 */
	public synchronized final void run(IProgressMonitor monitor)
		throws InvocationTargetException, InterruptedException {
		execute(monitor);
	}

	/**
	 * Execute this operation. Subclasses are expected to implement their
	 * specific behaviour.
	 * 
	 * @param monitor
	 *            Progress monitor to display progress and allow for user cancel
	 *            requests
	 */
	protected abstract void execute(IProgressMonitor monitor)
		throws InvocationTargetException, InterruptedException;
}