/******************************************************************************
 * Copyright (c) 2002, 2005 IBM Corporation and others.
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
import org.eclipse.gmf.runtime.common.ui.action.internal.CommonUIActionPlugin;
import org.eclipse.gmf.runtime.common.ui.action.internal.IHelpContextIds;
import org.eclipse.gmf.runtime.common.ui.action.internal.l10n.CommonUIActionMessages;
import org.eclipse.gmf.runtime.common.ui.action.internal.l10n.CommonUIActionPluginImages;

/**
 * Global Save Action
 * 
 * @author vramaswa
 */
public final class GlobalSaveAction extends GlobalAction {

	/**
	 * Action definition id of the save action.
	 */
	private static final String SAVE = "org.eclipse.gmf.runtime.common.ui.actions.global.save"; //$NON-NLS-1$

	/**
	 * Imagedescriptor for the save action
	 */
	private static final ImageDescriptor SAVE_IMAGE = CommonUIActionPlugin.imageDescriptorFromPlugin
		(CommonUIActionPlugin.getPluginId(), CommonUIActionPluginImages.IMG_SAVE_EDIT_ETOOL16);
		
	/**
	 * Imagedescriptor for the save action
	 */
	private static final ImageDescriptor DISABLED_SAVE_IMAGE = CommonUIActionPlugin.imageDescriptorFromPlugin
		(CommonUIActionPlugin.getPluginId(), CommonUIActionPluginImages.IMG_SAVE_EDIT_DTOOL16);
	
	/**
	 * Imagedescriptor for the save action
	 */
	private static final ImageDescriptor HOVER_SAVE_IMAGE = CommonUIActionPlugin.imageDescriptorFromPlugin
		(CommonUIActionPlugin.getPluginId(), CommonUIActionPluginImages.IMG_SAVE_EDIT_CTOOL16);
	
	/**
	 * @param workbenchPage
	 */
	public GlobalSaveAction(IWorkbenchPage workbenchPage) {
		super(workbenchPage);
	}


	/**
	 * @param workbenchPart
	 */
	public GlobalSaveAction(IWorkbenchPart workbenchPart) {
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
				: SAVE);

		/* Set the label */
		setText(CommonUIActionMessages.GlobalSaveAction_label);

		/* Set the image */
		setImageDescriptor(SAVE_IMAGE);
		setHoverImageDescriptor(HOVER_SAVE_IMAGE);
		setDisabledImageDescriptor(DISABLED_SAVE_IMAGE);

		/* Set the context sensitive help */
		PlatformUI.getWorkbench().getHelpSystem().setHelp(this, IHelpContextIds.PX_U_DEFAULT_CS_HELP);
		
		super.init();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.ui.action.internal.global.GlobalAction#getActionId()
	 */
	public String getActionId() {
		return GlobalActionId.SAVE;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.ui.action.AbstractActionHandler#isSelectionListener()
	 */
	protected boolean isSelectionListener() {
		return true;
	}
}
