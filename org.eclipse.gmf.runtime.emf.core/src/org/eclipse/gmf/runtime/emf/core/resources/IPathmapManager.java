/******************************************************************************
 * Copyright (c) 2002, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.emf.core.resources;

import org.eclipse.core.runtime.IStatus;

/**
 * Specification of an object that manages URI mappings (path variables) for
 * the <code>pathmap</code> URI scheme on an editing domain's resource set.
 * 
 * @author rafikj
 */
public interface IPathmapManager {
	
	/**
	 * Set the value of a path variable.
	 * 
	 * @param name the path variable name
	 * @param value the path variable value (a file URI)
	 * 
	 * @return a status indicating success (OK) or failure (ERROR) to set the
	 *     path variable.  In case of error, the status message provides a
	 *     user-friendly explanation of the reason 
	 */
	IStatus addPathVariable(String name, String value);
	
	/**
	 * Removes a path variable and its value.
	 * 
	 * @param name the path variable name
	 * 
	 * @return a status indicating success (OK) or failure (ERROR) to remove the
	 *     path variable.  In case of error, the status message provides a
	 *     user-friendly explanation of the reason 
	 */
	IStatus removePathVariable(String name);

	/**
	 * Queries the current value of a path variable.
	 * 
	 * @param name the path variable name
	 * @return the path variable value (a URI) or an empty string if
	 *    the specified variable is undefined
	 */
	String getPathVariable(String name);
}
