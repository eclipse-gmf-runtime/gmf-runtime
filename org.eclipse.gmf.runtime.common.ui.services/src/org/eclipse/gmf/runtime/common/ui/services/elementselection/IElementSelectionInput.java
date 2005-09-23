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
package org.eclipse.gmf.runtime.common.ui.services.elementselection;

import java.util.List;

import org.eclipse.core.runtime.IAdaptable;

/**
 * Interface describing the input for the element selection service.
 * 
 * @author Anthony Hunter <a href="mailto:anthonyh@ca.ibm.com">
 *         anthonyh@ca.ibm.com </a>
 */
public interface IElementSelectionInput {

	
	/**
	 * Retrieves the scope of the search for input types.
	 * 
	 * @return ElementSelectionScope the scope of the search for input types.
	 */
	public ElementSelectionScope getScope();
	
	/**
	 * Retrieves a list of the input types. Each input type should implement the
	 * <code>IElementType</code> interface.
	 * 
	 * @return List a list of the input types.
	 */
	public List getTypes();

	/**
	 * Retrieves a context for the input. Most frequently the context is an EMF
	 * EObject.
	 * 
	 * @return IAdaptable a context for the input.
	 */
	public IAdaptable getContext();
}
