/******************************************************************************
 * Copyright (c) 2002, 2004 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

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
