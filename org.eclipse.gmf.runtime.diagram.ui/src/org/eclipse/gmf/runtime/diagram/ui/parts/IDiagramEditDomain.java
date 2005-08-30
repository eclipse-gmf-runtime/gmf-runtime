/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2004.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.parts;

import org.eclipse.gmf.runtime.common.ui.action.ActionManager;

/**
 * Diagram Edit Domain Interface 
 * @author melaasar
 *
 */ 
public interface IDiagramEditDomain {

	/**
	 * Returns the DiagramCommandStack to be used with ICommands.
	 * @return The command stack for this edit domain.
	 */
	public DiagramCommandStack getDiagramCommandStack();

	/**
	 * Returns the ActionManager 
	 * @return the Action Manager
	 */
	public ActionManager getActionManager();

}
