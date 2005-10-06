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

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

import org.eclipse.gmf.runtime.common.ui.action.global.GlobalAction;
import org.eclipse.gmf.runtime.common.ui.action.global.GlobalActionId;
import org.eclipse.gmf.runtime.common.ui.action.internal.IHelpContextIds;
import org.eclipse.gmf.runtime.common.ui.action.internal.l10n.ResourceManager;

/**
 * Global Delete Action
 * 
 * @author vramaswa
 */
public final class GlobalDeleteAction extends GlobalAction {
	/**
	 * Label definition of the delete action.
	 */
	private static final String DELETE_TEXT = ResourceManager.getI18NString("GlobalDeleteAction.label"); //$NON-NLS-1$

	/**
	 * Action definition id of the delete action.
	 */
	private static final String DELETE = "org.eclipse.gmf.runtime.common.ui.actions.global.delete"; //$NON-NLS-1$

	/**
	 * Imagedescriptor for the delete action
	 */
	private static final ImageDescriptor DELETE_IMAGE = ResourceManager.getInstance().getImageDescriptor("full/etool16/delete_edit.gif"); //$NON-NLS-1$

	/**
	 * Imagedescriptor for the delete action
	 */
	private static final ImageDescriptor DISABLED_DELETE_IMAGE = ResourceManager.getInstance().getImageDescriptor("full/dtool16/delete_edit.gif"); //$NON-NLS-1$

	/**
	 * Imagedescriptor for the delete action
	 */
	private static final ImageDescriptor HOVER_DELETE_IMAGE = ResourceManager.getInstance().getImageDescriptor("full/ctool16/delete_edit.gif"); //$NON-NLS-1$

	/**
	 * @param workbenchPage
	 */
	public GlobalDeleteAction(IWorkbenchPage workbenchPage) {
		super(workbenchPage);
	}

	/**
	 * @param workbenchPart
	 */
	public GlobalDeleteAction(IWorkbenchPart workbenchPart) {
		super(workbenchPart);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.ui.action.IDisposableAction#init()
	 */
	public void init() {
		/* Set the id */
		setId(
			getWorkbenchActionConstant() != null
				? getWorkbenchActionConstant()
				: DELETE);

		/* Set the label */
		setText(DELETE_TEXT);

		/* Set the image */
		setImageDescriptor(DELETE_IMAGE);
		setHoverImageDescriptor(HOVER_DELETE_IMAGE);
		setDisabledImageDescriptor(DISABLED_DELETE_IMAGE);

		/* Set the context sensitive help */
		PlatformUI.getWorkbench().getHelpSystem().setHelp(this, IHelpContextIds.PX_U_DEFAULT_CS_HELP);
		
		super.init();
	}


	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.ui.action.internal.global.GlobalAction#getActionId()
	 */
	public String getActionId() {
		return GlobalActionId.DELETE;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.ui.action.AbstractActionHandler#isSelectionListener()
	 */
	protected boolean isSelectionListener() {
		return true;
	}
}
