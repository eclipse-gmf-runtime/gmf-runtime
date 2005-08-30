/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
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