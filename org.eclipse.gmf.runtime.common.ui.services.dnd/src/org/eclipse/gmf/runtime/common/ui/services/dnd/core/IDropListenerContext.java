/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2005.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.common.ui.services.dnd.core;

import org.eclipse.ui.IWorkbenchPart;

import org.eclipse.gmf.runtime.common.ui.services.dnd.internal.core.IListenerContext;

/**
 * Interface for accessing the attributes used to determine the
 * <code>IDropTargetListener</code>.
 * 
 * @author Vishy Ramaswamy
 */
public interface IDropListenerContext
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
	 * Returns an element type associated with the current target selected on
	 * the drop target viewer
	 * 
	 * @return Returns a <code>Class</code>
	 */
	public Class getTargetElementType();
}