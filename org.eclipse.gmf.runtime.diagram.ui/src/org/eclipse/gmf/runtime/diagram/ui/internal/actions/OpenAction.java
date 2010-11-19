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

package org.eclipse.gmf.runtime.diagram.ui.internal.actions;

import org.eclipse.gef.Request;
import org.eclipse.gmf.runtime.diagram.ui.actions.ActionIds;
import org.eclipse.gmf.runtime.diagram.ui.actions.DiagramAction;
import org.eclipse.gmf.runtime.diagram.ui.l10n.DiagramUIMessages;
import org.eclipse.gmf.runtime.diagram.ui.requests.RequestConstants;
import org.eclipse.ui.IWorkbenchPage;




/**
 * @author tmacdoug
 *
 * This action is used to handle open actions on a diagram
 *
 */
public class OpenAction
extends DiagramAction {
	
	/**
	 * @param workbenchPage
	 */
	public OpenAction(IWorkbenchPage workbenchPage) {
		super(workbenchPage);
	}

	public void init() {
		super.init();
		
		setId(ActionIds.OPEN);
		setText(DiagramUIMessages.Open_ActionLabelText);
		setToolTipText(DiagramUIMessages.Open_ActionToolTipText);
	}

	protected Request createTargetRequest() {		
		return new Request(RequestConstants.REQ_OPEN);
	}

	protected boolean isSelectionListener() {
		return true;
	}


}
