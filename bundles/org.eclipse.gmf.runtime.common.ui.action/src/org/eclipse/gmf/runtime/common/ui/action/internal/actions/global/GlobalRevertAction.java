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

package org.eclipse.gmf.runtime.common.ui.action.internal.actions.global;

import org.eclipse.gmf.runtime.common.ui.action.global.GlobalAction;
import org.eclipse.gmf.runtime.common.ui.action.global.GlobalActionId;
import org.eclipse.gmf.runtime.common.ui.action.internal.l10n.CommonUIActionMessages;
import org.eclipse.ui.ISaveablePart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;

/**
 * Global action for the retargetable "REVERT" action.
 * 
 * @author ldamus
 */
public class GlobalRevertAction
	extends GlobalAction {

	/**
	 * Action definition id of the REVERT action.
	 */
	private static final String REVERT = "org.eclipse.gmf.runtime.common.ui.actions.global.revert"; //$NON-NLS-1$

	/**
	 * Constructs a new action instance.
	 * 
	 * @param workbenchPage
	 *            the workbench page
	 */
	public GlobalRevertAction(IWorkbenchPage workbenchPage) {
		super(workbenchPage);
	}

	/**
	 * Constructs a new action instance.
	 * 
	 * @param workbenchPart
	 *            the workbench part
	 */
	public GlobalRevertAction(IWorkbenchPart workbenchPart) {
		super(workbenchPart);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.ui.internal.action.global.GlobalAction#getActionId()
	 */
	public String getActionId() {
		return GlobalActionId.REVERT;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.ui.internal.action.global.GlobalAction#init()
	 */
	public void init() {

		// Set the ID
		setId(getWorkbenchActionConstant() != null ? getWorkbenchActionConstant()
			: REVERT);

		// Set the label
		setText(CommonUIActionMessages.GlobalRevertAction_label);

		super.init();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.ui.internal.action.AbstractActionHandler#isSelectionListener()
	 */
	protected boolean isSelectionListener() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.ui.internal.action.AbstractActionHandler#isPropertyListener()
	 */
	protected boolean isPropertyListener() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IPropertyListener#propertyChanged(java.lang.Object,
	 *      int)
	 */
	public void propertyChanged(Object source, int propId) {
		if (propId == ISaveablePart.PROP_DIRTY) {
			refresh();
		}
	}

}