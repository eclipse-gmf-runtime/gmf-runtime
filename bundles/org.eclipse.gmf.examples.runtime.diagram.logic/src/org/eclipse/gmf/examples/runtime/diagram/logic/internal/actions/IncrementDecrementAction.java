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

package org.eclipse.gmf.examples.runtime.diagram.logic.internal.actions;

import java.util.HashMap;

import org.eclipse.gef.Request;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.LogicDiagramPlugin;
import org.eclipse.gmf.runtime.diagram.ui.actions.DiagramAction;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IWorkbenchPage;

/**
 * @author qili
 * 
 * Action to increment/decrement value for LED figures.
 */

public class IncrementDecrementAction
	extends DiagramAction
	implements LogicActionIds {

	private static final String INCREMENT_REQUEST = "Increment", //$NON-NLS-1$
			DECREMENT_REQUEST = "Decrement"; //$NON-NLS-1$

	private String actionId;

	private HashMap actionMap = new HashMap();
	{
		actionMap.put(ACTION_INCREMENT_VALUE, INCREMENT_REQUEST);
		actionMap.put(ACTION_DECREMENT_VALUE, DECREMENT_REQUEST);
	}

	/**
	 * Construct a Decompose Action
	 * 
	 * @param workbenchPage
	 */
	public IncrementDecrementAction(IWorkbenchPage workbenchPage,
			String actionId) {
		super(workbenchPage);
		this.actionId = actionId;
	}

	/**
	 * @see org.eclipse.gmf.runtime.common.ui.internal.action.IDisposableAction#init()
	 */
	public void init() {
		super.init();
		if (actionId.equals(ACTION_INCREMENT_VALUE)) {
			setId("Increment"); //$NON-NLS-1$
			setText("Increment"); //$NON-NLS-1$
			setToolTipText("Increment LED"); //$NON-NLS-1$
			setImageDescriptor(ImageDescriptor.createFromFile(
				LogicDiagramPlugin.class, "icons/plus.gif")); //$NON-NLS-1$
		} else if (actionId.equals(ACTION_DECREMENT_VALUE)) {
			setId("Decrement"); //$NON-NLS-1$
			setText("Decrement"); //$NON-NLS-1$
			setToolTipText("Decrement LED"); //$NON-NLS-1$
			setImageDescriptor(ImageDescriptor.createFromFile(
				LogicDiagramPlugin.class, "icons/minus.gif")); //$NON-NLS-1$
		}
	}

	/**
	 * @see org.eclipse.gmf.runtime.diagram.ui.actions.DiagramAction#createTargetRequest()
	 */
	protected Request createTargetRequest() {
		return new Request(actionMap.get(actionId));
	}

	/**
	 * @see org.eclipse.gmf.runtime.common.ui.internal.action.AbstractActionHandler#isSelectionListener()
	 */
	protected boolean isSelectionListener() {
		return true;
	}
}
