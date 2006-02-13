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
 * 
 * @deprecated Attach a {@link org.eclipse.emf.transaction.ResourceSetListener}
 *     to the {@link org.eclipse.emf.transaction.TXEditingDomain}, instead.
 *     The most common usage of <code>OperationListener</code> is for post-commit
 *     events (after a write action closes); implement the
 *     {@link org.eclipse.emf.transaction.ResourceSetListener#resourceSetChanged(org.eclipse.emf.transaction.ResourceSetChangeEvent)}
 *     callback for this purpose. 
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
