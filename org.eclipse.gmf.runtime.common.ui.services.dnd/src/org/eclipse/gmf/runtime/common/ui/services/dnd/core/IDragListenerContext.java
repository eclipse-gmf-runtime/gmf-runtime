/******************************************************************************
 * Copyright (c) 2002, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.common.ui.services.dnd.core;

import org.eclipse.ui.IWorkbenchPart;

import org.eclipse.gmf.runtime.common.ui.services.dnd.internal.core.IListenerContext;

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