/******************************************************************************
 * Copyright (c) 2002, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.common.ui.action.internal.actions.global;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.help.WorkbenchHelp;

import org.eclipse.gmf.runtime.common.ui.action.actions.global.ClipboardManager;
import org.eclipse.gmf.runtime.common.ui.action.actions.global.GlobalActionManager;
import org.eclipse.gmf.runtime.common.ui.action.global.GlobalAction;
import org.eclipse.gmf.runtime.common.ui.action.global.GlobalActionId;
import org.eclipse.gmf.runtime.common.ui.action.internal.IHelpContextIds;
import org.eclipse.gmf.runtime.common.ui.action.internal.l10n.ResourceManager;

/**
 * Global Copy Action
 * 
 * @author Vishy Ramaswamy
 */
public final class GlobalCopyAction extends GlobalAction {
	/**
	 * Label definition of the copy action.
	 */
	private static final String COPY_TEXT = ResourceManager.getI18NString("CopyAction.label"); //$NON-NLS-1$

	/**
	 * Action definition id of the copy action.
	 */
	private static final String COPY = "org.eclipse.gmf.runtime.common.ui.actions.global.copy"; //$NON-NLS-1$

	/**
	 * Imagedescriptor for the copy action
	 */
	private static final ImageDescriptor COPY_IMAGE = ResourceManager.getInstance().getImageDescriptor("full/etool16/copy_edit.gif"); //$NON-NLS-1$

	/**
	 * Imagedescriptor for the copy action
	 */
	private static final ImageDescriptor DISABLED_COPY_IMAGE = ResourceManager.getInstance().getImageDescriptor("full/dtool16/copy_edit.gif"); //$NON-NLS-1$

	/**
	 * Imagedescriptor for the copy action
	 */
	private static final ImageDescriptor HOVER_COPY_IMAGE = ResourceManager.getInstance().getImageDescriptor("full/ctool16/copy_edit.gif"); //$NON-NLS-1$

	/**
	 * @param workbenchPage
	 */
	public GlobalCopyAction(IWorkbenchPage workbenchPage) {
		super(workbenchPage);
	}

	/**
	 * Constructor.
	 * 
	 * @param workbenchPart the <code>IWorkbenchPart</code> associated with this action
	 */
	public GlobalCopyAction(IWorkbenchPart workbenchPart) {
		super(workbenchPart);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.ui.action.IDisposableAction#init()
	 */
	public void init() {
		/* set the id */
		setId(
			getWorkbenchActionConstant() != null
				? getWorkbenchActionConstant()
				: COPY);

		/* set the label */
		setText(COPY_TEXT);

		/*  set the image */
		setImageDescriptor(COPY_IMAGE);
		setHoverImageDescriptor(HOVER_COPY_IMAGE);
		setDisabledImageDescriptor(DISABLED_COPY_IMAGE);

		/*  set the context sensitive help */
		WorkbenchHelp.setHelp(this, IHelpContextIds.PX_U_DEFAULT_CS_HELP);
		super.init();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.ui.action.internal.global.GlobalAction#getActionId()
	 */
	public String getActionId() {
		return GlobalActionId.COPY;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.ui.action.AbstractActionHandler#doRun(org.eclipse.core.runtime.IProgressMonitor)
	 */
	protected void doRun(IProgressMonitor progressMonitor) {
		super.doRun(progressMonitor);

		/* Flush the clipboard manager */
		ClipboardManager.getInstance().flushCacheToClipboard();

		/* Set the paste action enablement for this part */
		GlobalAction paste =
			GlobalActionManager.getInstance().getGlobalAction(
				getWorkbenchPart(),
				GlobalActionId.PASTE);
		if (paste != null) {
			paste.refresh();
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.ui.action.AbstractActionHandler#isSelectionListener()
	 */
	protected boolean isSelectionListener() {
		return true;
	}
}
