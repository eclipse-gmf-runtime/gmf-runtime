/******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.tests.runtime.diagram.ui.action;

import org.eclipse.gef.commands.Command;
import org.eclipse.ui.IWorkbenchPage;

import org.eclipse.gmf.runtime.diagram.ui.actions.internal.DeleteFromDiagramAction;


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
