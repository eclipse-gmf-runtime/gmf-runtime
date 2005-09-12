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



/**
 * A listener to {@link org.eclipse.gmf.runtime.emf.core.IOperationEvent IOperationEvent}s.
 * 
 * API clients should extend this class and register their listeners through
 * {@link org.eclipse.gmf.runtime.emf.core.EditingDomain#addOperationListener(OperationListener) EditingDomain.addOperationListener}
 * and unregister through
 * {@link org.eclipse.gmf.runtime.emf.core.EditingDomain#removeOperationListener(OperationListener) EditingDomain.removeOperationListener}
 */
public abstract class OperationListener {

	/**
	 * Notifies listener that an {@link ResourceSetOperation}was executed.
	 * Implements default do nothing behaviour. Derived client implementations
	 * generally override this method to look for specific events.
	 * <p>
	 * Implementations are allowed to start new ResourceSetReadOperation.
	 * </p>
	 * <p>
	 * Implementations are allowed to start new ResourceSetModifyOperation.
	 * However, it should be noted that the title of the operation may not be
	 * displayed in the <b>Edit </b> menu in case the event was sent as the
	 * result of another ResourceSetModifyOperation.
	 * </p>
	 * <p>
	 * The listener is invoked on the thread making the change. 
	 * </p>
	 * 
	 * @param event
	 *            Event describing the change
	 */
	public void done(IOperationEvent event) {
		// Do nothing
	}

	/**
	 * Notifies listener that an {@link ResourceSetOperation}was undone.
	 * Implements default do nothing behaviour. Derived client implementations
	 * generally override this method to look for specific events.
	 * <p>
	 * Implementations are allowed to start new ResourceSetReadOperation.
	 * </p>
	 * <p>
	 * Implementations are <b>not </b> allowed to start new
	 * ResourceSetModifyOperation.
	 * </p>
	 * <p>
	 * The listener is invoked on the thread making the change. 
	 * </p>
	 * 
	 * @param event
	 *            Event describing the change
	 */
	public void undone(IOperationEvent event) {
		// Do nothing
	}

	/**
	 * Notifies listener that an {@link ResourceSetOperation}was redone
	 * Implements default do nothing behaviour. Derived client implementations
	 * generally override this method to look for specific events.
	 * <p>
	 * Implementations are allowed to start new ResourceSetReadOperation.
	 * </p>
	 * <p>
	 * Implementations are <b>not </b> allowed to start new
	 * ResourceSetModifyOperation.
	 * </p>
	 * <p>
	 * The listener is invoked on the thread making the change. 
	 * </p>
	 * 
	 * @param event
	 *            Event describing the change
	 */
	public void redone(IOperationEvent event) {
		// Do nothing
	}
}