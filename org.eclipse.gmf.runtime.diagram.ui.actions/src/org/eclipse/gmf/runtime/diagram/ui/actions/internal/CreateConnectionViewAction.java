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
/*
 * Created on Jan 17, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.eclipse.gmf.runtime.diagram.ui.actions.internal;

import org.eclipse.gef.Request;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IWorkbenchPage;

import org.eclipse.gmf.runtime.diagram.ui.actions.DiagramAction;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateViewRequest;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateConnectionViewRequest.ConnectionViewDescriptor;

/**
 * This is the action handler for the diagram menu.   Adds actions to 
 * create note and text views.
 *
 * @author schafe
 */
public class CreateConnectionViewAction extends DiagramAction {

	protected String semanticHint;

	/**
	 * Constructor
	 * 
	 * @param workbenchPage, the workbench page
	 * @param id, the id of this action
	 * @param semanticHint
	 * @param label, the menu item label the user will see
	 * @param imageDescriptor, the image next to the label that the user sees
	 */
	public CreateConnectionViewAction(
		IWorkbenchPage workbenchPage,
		String actionId,
		String semanticHint,
		String label,
		ImageDescriptor imageDescriptor) {

		super(workbenchPage);
		setId(actionId);
		setSemanticHint(semanticHint);
		setText(label);
		setToolTipText(label);
		setImageDescriptor(imageDescriptor);
	}

	/**
	 * Creates a new request to create the shape view.
	 * 
	 * @return A request to create the shape view.
	 */
	protected Request createTargetRequest() {
		ConnectionViewDescriptor viewDescriptor;
		viewDescriptor = new ConnectionViewDescriptor(null, getSemanticHint(),
			getPreferencesHint());
		return new CreateViewRequest(viewDescriptor);
	}

	/**
	 * @see org.eclipse.gmf.runtime.common.ui.action.AbstractActionHandler#isSelectionListener()
	 */
	protected boolean isSelectionListener() {
		return true;
	}

	protected void setSemanticHint(String hint) {
		this.semanticHint = hint;
	}

	protected String getSemanticHint() {
		return this.semanticHint;
	}

}
