/******************************************************************************
 * Copyright (c) 2002, 2006 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.common.ui.services.dnd.core;

import org.eclipse.ui.IWorkbenchPart;


/**
 * Interface for accessing the attributes used to determine the
 * <code>IDragSourceListener</code>
 * 
 * @author Vishy Ramaswamy
 */
public interface IDragListenerContext
	extends IListenerContext {

	/**
	 * Returns a boolean to indicate whether a direct match is required for the
	 * element type or not. For a compatible type the element type supported by
	 * the provider should be assignable from this context's element type.
	 * 
	 * @return true if compatible is requested, false otherwise (for a direct
	 *         match)
	 */
	public boolean isCompatible();

	/**
	 * Returns the active <code>IWorkbenchPart</code>
	 * 
	 * @return Returns the active <code>IWorkbenchPart</code>
	 */
	public IWorkbenchPart getActivePart();

	/**
	 * Returns an element type associated with the current selection in the
	 * active <code>IWorkbenchPart</code>
	 * 
	 * @return Returns a <code>Class</code>
	 */
	public Class getSelectedElementType();
}