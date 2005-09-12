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
 * A ResourceSetOperation whose execution can potentially modify models.
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
 *  	editingDomain.run( new ResourceSetModifyOperation(&quot;MyOperationTitle&quot;) {
 *  
 *  		protected void execute(IProgressMonitor monitor)
 *  			throws InvocationTargetException, InterruptedException {
 *  			
 *  			// Perform modifications to the editing domain's models
 *  			...
 *  		}
 *  	}, new NullProgressMonitor());
 *  
 * </pre>
 * 
 * </p>
 * 
 * @see org.eclipse.gmf.runtime.emf.core.EditingDomain#run(ResourceSetOperation,
 *      org.eclipse.core.runtime.IProgressMonitor)
 */
public abstract class ResourceSetModifyOperation
	extends ResourceSetOperation {

	private String label = null;

	/**
	 * Constructs a ResourceSetModifyOperation with a short description of what
	 * the operation does. This label is used as a format parameter presented to
	 * users under the <b>Edit > Undo </b> and <b>Edit > Redo </b> menus.
	 * <p>
	 * An <code>ResourceSetModifyOperation</code> ran from within an
	 * {@link org.eclipse.gmf.runtime.emf.core.OperationListener#done(IOperationEvent)} implementation 
	 * may not see its label honoured in the <b>Edit </b> menu. Regardless of
	 * this fact, <b>always </b> provide a valid user consumable title.
	 * </p>
	 * 
	 * @param label
	 *            the operation's label.
	 */
	public ResourceSetModifyOperation(String label) {
		super();
		this.label = label;
	}

	/**
	 * Returns a short description of what the operation does. This label is
	 * used as a format parameter presented to users under the <b>Edit > Undo
	 * </b> and <b>Edit > Redo </b> menus.
	 * 
	 * @return The operation's label
	 */
	public final String getLabel() {
		return label;
	}
}