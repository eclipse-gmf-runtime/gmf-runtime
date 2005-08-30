/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2005.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.common.ui.services.dnd.drag;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchPart;

/**
 * Interface that contains the context information relevant to the drag source.
 * 
 * @author Vishy Ramaswamy
 */
public interface IDragSourceContext {

	/**
	 * Returns the selection of the drag source viewer
	 * 
	 * @return The selection of the drag source viewer
	 */
	public ISelection getSelection();

	/**
	 * Returns the active <code>IWorkbenchPart</code> associated with the drag
	 * source viewer.
	 * 
	 * @return The active <code>IWorkbenchPart</code>
	 */
	public IWorkbenchPart getActivePart();
}