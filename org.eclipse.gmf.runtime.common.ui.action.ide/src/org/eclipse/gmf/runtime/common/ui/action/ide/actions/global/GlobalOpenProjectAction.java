/******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.common.ui.action.ide.actions.global;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.help.WorkbenchHelp;

import org.eclipse.gmf.runtime.common.ui.action.global.GlobalAction;
import org.eclipse.gmf.runtime.common.ui.action.ide.global.IDEGlobalActionId;
import org.eclipse.gmf.runtime.common.ui.action.ide.internal.IHelpContextIds;
import org.eclipse.gmf.runtime.common.ui.action.ide.internal.l10n.ResourceManager;

/**
 * Global action to open a project resource.
 * 
 * @author ldamus
 */
public class GlobalOpenProjectAction
	extends GlobalAction {

	/**
	 * Creates a new action.
	 * 
	 * @param workbenchPage
	 *            the workbench page
	 */
	public GlobalOpenProjectAction(IWorkbenchPage workbenchPage) {
		super(workbenchPage);
	}

	/**
	 * Creates a new action.
	 * 
	 * @param workbenchPart
	 *            the workbench part
	 */
	public GlobalOpenProjectAction(IWorkbenchPart workbenchPart) {
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

		setText(ResourceManager.getI18NString("GlobalOpenProjectAction.label")); //$NON-NLS-1$
		WorkbenchHelp.setHelp(this, IHelpContextIds.PX_U_DEFAULT_CS_HELP);
		super.init();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.ui.action.internal.core.global.GlobalAction#getActionId()
	 */
	public String getActionId() {
		return IDEGlobalActionId.OPEN_PROJECT;
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

		GlobalAction closeProjectAction = IDEGlobalActionManager.getInstance()
			.getGlobalAction(getWorkbenchPart(), IDEGlobalActionId.CLOSE_PROJECT);

		if (closeProjectAction != null) {
			closeProjectAction.refresh();
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