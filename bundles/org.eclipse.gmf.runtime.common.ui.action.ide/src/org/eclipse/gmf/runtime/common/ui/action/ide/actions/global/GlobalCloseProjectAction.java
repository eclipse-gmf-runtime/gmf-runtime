/******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.common.ui.action.ide.actions.global;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gmf.runtime.common.ui.action.global.GlobalAction;
import org.eclipse.gmf.runtime.common.ui.action.ide.global.IDEGlobalActionId;
import org.eclipse.gmf.runtime.common.ui.action.ide.internal.IHelpContextIds;
import org.eclipse.gmf.runtime.common.ui.action.ide.internal.l10n.CommonUIActionIDEMessages;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

/**
 * Global action to close a project resource.
 * 
 * @author ldamus
 */
public class GlobalCloseProjectAction
	extends GlobalAction {

	/**
	 * Creates a new action.
	 * 
	 * @param workbenchPage
	 *            the workbench page
	 */
	public GlobalCloseProjectAction(IWorkbenchPage workbenchPage) {
		super(workbenchPage);
	}

	/**
	 * Creates a new action.
	 * 
	 * @param workbenchPart
	 *            the workbench part
	 */
	public GlobalCloseProjectAction(IWorkbenchPart workbenchPart) {
		super(workbenchPart);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.ui.action.internal.core.IDisposableAction#init()
	 */
	public void init() {
		setId(getWorkbenchActionConstant() != null ? getWorkbenchActionConstant()
			: getActionId());

		setText(CommonUIActionIDEMessages.GlobalCloseProjectAction_label);
		PlatformUI.getWorkbench().getHelpSystem().setHelp(this, IHelpContextIds.PX_U_DEFAULT_CS_HELP);
		
		super.init();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.ui.action.internal.core.global.GlobalAction#getActionId()
	 */
	public String getActionId() {
		return IDEGlobalActionId.CLOSE_PROJECT;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.ui.action.internal.core.global.GlobalAction#getWorkbenchActionConstant()
	 */
	public String getWorkbenchActionConstant() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.IAction#run()
	 */
	public void run() {
		if (isEnabled()) {
			super.run();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.ui.action.internal.core.global.GlobalAction#doRun(org.eclipse.core.runtime.IProgressMonitor)
	 */
	protected void doRun(IProgressMonitor progressMonitor) {
		super.doRun(progressMonitor);

		// Normally, global action enablement is refreshed on selection change,
		// but in the
		// case of opening and closing projects, the enablement should be
		// calculated immediately.
		refresh();

		GlobalAction openProjectAction = IDEGlobalActionManager.getInstance()
			.getGlobalAction(getWorkbenchPart(), IDEGlobalActionId.OPEN_PROJECT);

		if (openProjectAction != null) {
			openProjectAction.refresh();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.ui.action.AbstractActionHandler#isSelectionListener()
	 */
	protected boolean isSelectionListener() {
		return true;
	}
}