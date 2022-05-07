/******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.tests.runtime.diagram.ui.action;

import org.eclipse.gef.commands.Command;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.DeleteFromDiagramAction;
import org.eclipse.ui.IWorkbenchPage;


/**
 * @author mmostafa
 */
public class DeleteFromDiagramTestAction
	extends DeleteFromDiagramAction {

	/**
	 * @param workbenchPage
	 */
	public DeleteFromDiagramTestAction(IWorkbenchPage workbenchPage) {
		super(workbenchPage);
	}
	
	public Command _getCommand(){
		return super.getCommand();
	}
}
