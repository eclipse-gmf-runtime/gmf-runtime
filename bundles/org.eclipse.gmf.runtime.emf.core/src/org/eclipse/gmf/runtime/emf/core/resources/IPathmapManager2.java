/******************************************************************************
 * Copyright (c) 2008 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.emf.core.resources;

import java.util.Map;

import org.eclipse.core.runtime.IStatus;

public interface IPathmapManager2 extends IPathmapManager {
	/**
	 * Adds a map of file path variables (not folders).
	 * 
	 * @param settings A map from variables(String) to values(String).
	 * 
	 * @return a status indicating success (OK) or failure (ERROR) to set the
	 *     path variable.  In case of error, the status message provides a
	 *     user-friendly explanation of the reason
	 */
	public IStatus addFilePathVariables(Map settings, boolean dirtyResources);
	
	/**
	 * Sets the value of a path variable to a file (not a folder).
	 * 
	 * @param name the path variable name
	 * @param value the path variable value pointing to a specific file (not a folder)
	 * 
	 * @return a status indicating success (OK) or failure (ERROR) to set the
	 *     path variable.  In case of error, the status message provides a
	 *     user-friendly explanation of the reason
	 */
	public IStatus addFilePathVariable(String name, String value, boolean dirtyResources);
	
	/**
	 * Adds a map of folder path variables (not files).
	 * 
	 * @param settings A map from variables(String) to values(String).
	 * 
	 * @return a status indicating success (OK) or failure (ERROR) to set the
	 *     path variable.  In case of error, the status message provides a
	 *     user-friendly explanation of the reason
	 */
	public IStatus addFolderPathVariables(Map settings, boolean dirtyResources);
	
	/**
	 * Sets the value of a path variable to a folder (not a file).
	 * 
	 * @param name the path variable name
	 * @param value the path variable value pointing to a specific folder (not a file)
	 * 
	 * @return a status indicating success (OK) or failure (ERROR) to set the
	 *     path variable.  In case of error, the status message provides a
	 *     user-friendly explanation of the reason
	 */
	public IStatus addFolderPathVariable(String name, String value, boolean dirtyResources);
}
