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