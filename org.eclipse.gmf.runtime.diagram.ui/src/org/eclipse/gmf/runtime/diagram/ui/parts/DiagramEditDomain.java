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

import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gmf.runtime.common.ui.action.ActionManager;
import org.eclipse.ui.IEditorPart;

/**
 * The diagram editor edit domain that holds all data that is contexual
 * to the editor instance 
 * @author melaasar
 *
 */
public class DiagramEditDomain
	extends DefaultEditDomain
	implements IDiagramEditDomain {

	/** the action manager */
	private ActionManager actionManager;

	/**
	 * Creates a DefaultEditDomain for the given IEditorPart
	 * @param editorPart
	 */
	public DiagramEditDomain(IEditorPart editorPart) {
		super(editorPart);
	}

	/**
	 * gets the diagram editor part
	 * @return <code>IDiagramWorkbenchPart</code>
	 */
	public IDiagramWorkbenchPart getDiagramEditorPart() {
		return (IDiagramWorkbenchPart) getEditorPart();
	}

	/**
	 * @see org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramEditDomain#getDiagramCommandStack()
	 */
	public DiagramCommandStack getDiagramCommandStack() {
		return (DiagramCommandStack) getCommandStack();
	}

	/**
	 * gets the action manager
	 * @return <code>ActionManager</code>
	 */
	public ActionManager getActionManager() {
		return actionManager;
	}

	/**
	 * @param manager
	 */
	public void setActionManager(ActionManager manager) {
		actionManager = manager;
	}

}
