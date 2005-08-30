/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.emf.ui;

import org.eclipse.swt.widgets.Display;

import org.eclipse.gmf.runtime.emf.core.IOperationEvent;
import org.eclipse.gmf.runtime.emf.core.OperationListener;
import org.eclipse.gmf.runtime.emf.core.ResourceSetOperation;


/**
 * A listener to {@link org.eclipse.gmf.runtime.emf.core.IOperationEvent IOperationEvent}s.
 * 
 * API clients should extend this class and register their listeners through
 * {@link org.eclipse.gmf.runtime.emf.core.EditingDomain#addOperationListener(OperationListener) EditingDomain.addOperationListener}
 * and unregister through
 * {@link org.eclipse.gmf.runtime.emf.core.EditingDomain#removeOperationListener(OperationListener) EditingDomain.removeOperationListener}
 */
public abstract class UIOperationListener
	extends OperationListener {

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.OperationListener#done(org.eclipse.gmf.runtime.emf.core.IOperationEvent)
	 */
	public final void done(final IOperationEvent event) {
		Display.getDefault().asyncExec(new Runnable() {
			
			/**
			 * @see java.lang.Runnable#run()
			 */
			public void run() {
				doneOnUI(event);
			}
		});
	}
	
	/**
	 * @see org.eclipse.gmf.runtime.emf.core.OperationListener#redone(org.eclipse.gmf.runtime.emf.core.IOperationEvent)
	 */
	public final void redone(final IOperationEvent event) {
		Display.getDefault().asyncExec(new Runnable() {
			
			/**
			 * @see java.lang.Runnable#run()
			 */
			public void run() {
				redoneOnUI(event);
			}
		});
	}
	
	/**
	 * @see org.eclipse.gmf.runtime.emf.core.OperationListener#undone(org.eclipse.gmf.runtime.emf.core.IOperationEvent)
	 */
	public final void undone(final IOperationEvent event) {
		Display.getDefault().asyncExec(new Runnable() {
			
			/**
			 * @see java.lang.Runnable#run()
			 */
			public void run() {
				undoneOnUI(event);
			}
		});
	}
	
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
	 * This listener is invoked on the Display thread.
	 * </p>
	 * 
	 * @param event
	 *            Event describing the change
	 */
	public void doneOnUI(IOperationEvent event) {
		// Subclasses should implement this
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
	 * This listener is invoked on the Display thread.
	 * </p>
	 * 
	 * @param event
	 *            Event describing the change
	 */
	public void redoneOnUI(IOperationEvent event) {
		// Subclasses should implement this
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
	 * This listener is invoked on the Display thread.
	 * </p>
	 * 
	 * @param event
	 *            Event describing the change
	 */
	public void undoneOnUI(IOperationEvent event) {
		//subclasses should implement this.
	}
}
