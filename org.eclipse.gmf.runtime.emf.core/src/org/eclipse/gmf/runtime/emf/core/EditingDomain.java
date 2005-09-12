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
import org.eclipse.emf.ecore.resource.ResourceSet;


/**
 * A class that exposes an editing domain in the modeling platform. This
 * class manages the EMF-based models in the modeling platform. It provides:
 * <ul>
 * <li>Model Access</li>
 * <li>Batched Notifications</li>
 * </ul>
 * <p>
 * API clients should <b>not</b> extend this class nor instantiate it. 
 * </p>
 */
public abstract class EditingDomain {

	/**
	 * Runs the <code>operation</code> to read and/or modify models. Changes
	 * performed by the specified <code>operation</code> will be grouped as a
	 * single logical undoable operation and pushed on the editing domain's
	 * operation stack. Undo and redo support is automatically provided for
	 * changes performed to models accessible from the domain's
	 * <code>ResourceSet</code>.
	 * 
	 * @param operation
	 *            The operation object to execute.
	 * @param monitor
	 *            The monitor used to report progress or cancel the operation.
	 * 
	 * @throws InvocationTargetException
	 *             An exception other than <code>InterruptedException</code>
	 *             occured. The cause exception is chained in this exception.
	 * @throws InterruptedException
	 *             Operation cancelled
	 * @see EditingDomain#getResourceSet()
	 */
	public abstract void run(final ResourceSetOperation operation,
			final IProgressMonitor monitor)
		throws InvocationTargetException, InterruptedException;

	/**
	 * Adds the specified <code>listener</code> to this
	 * <code>EditingDomain</code>'s listener list. A listener cannot be added
	 * twice. If it is added twice, the second addition will be ignored.
	 * 
	 * @see OperationListener
	 * @param listener
	 *            The listener to add.
	 */
	public abstract void addOperationListener(OperationListener listener);

	/**
	 * Removes the specified <code>listener</code> from this
	 * <code>EditingDomain</code>'s listener list.
	 * 
	 * @param listener
	 *            The listener to remove.
	 */
	public abstract void removeOperationListener(OperationListener listener);

	/**
	 * Returns the set of <code>Resource</code> managed by this
	 * <code>EditingDomain</code>
	 * 
	 * @return The <code>ResourceSet</code>
	 */
	public abstract ResourceSet getResourceSet();
}