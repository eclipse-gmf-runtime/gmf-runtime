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
 * A ResourceSetOperation whose execution can read models.
 * <p>
 * API clients are expected to extend this class and provide their own
 * implementation of
 * {@link org.eclipse.gmf.runtime.emf.core.ResourceSetOperation#execute(org.eclipse.core.runtime.IProgressMonitor) ResourceSetOperation.execute}
 * </p>
 * <p>
 * Example:
 * 
 * <pre>
 * 
 *            	editingDomain.run( new ResourceSetReadOperation() {
 *            
 *            		protected void execute(IProgressMonitor monitor)
 *            			throws InvocationTargetException, InterruptedException {
 *            			
 *            			// Perform access to the editing domain's models
 *            			...
 *            		}
 *            	}, new NullProgressMonitor());
 *  
 * </pre>
 * 
 * </p>
 * 
 * @see org.eclipse.gmf.runtime.emf.core.EditingDomain#run(ResourceSetOperation,
 *      org.eclipse.core.runtime.IProgressMonitor)
 */
public abstract class ResourceSetReadOperation
	extends ResourceSetOperation {
	// Tag Interface
}