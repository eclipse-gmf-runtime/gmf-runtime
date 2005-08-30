/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
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
