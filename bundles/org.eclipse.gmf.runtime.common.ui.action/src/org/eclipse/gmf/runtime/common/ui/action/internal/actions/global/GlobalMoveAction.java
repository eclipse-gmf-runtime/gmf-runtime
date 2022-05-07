/******************************************************************************
 * Copyright (c) 2002, 2003 IBM Corporation and others.
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
import org.eclipse.gmf.runtime.common.ui.action.internal.CommonUIActionPlugin;
import org.eclipse.gmf.runtime.common.ui.action.internal.IHelpContextIds;
import org.eclipse.gmf.runtime.common.ui.action.internal.l10n.CommonUIActionMessages;
import org.eclipse.gmf.runtime.common.ui.action.internal.l10n.CommonUIActionPluginImages;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

/**
 * Global Move Action
 * 
 * @author Vishy Ramaswamy
 */
public final class GlobalMoveAction extends GlobalAction {

	/**
	 * Action definition id of the move action.
	 */
	private static final String MOVE = "org.eclipse.gmf.runtime.common.ui.actions.global.move"; //$NON-NLS-1$

	/**
	 * Imagedescriptor for the move action
	 */
	private static final ImageDescriptor MOVE_IMAGE = CommonUIActionPlugin.imageDescriptorFromPlugin
		(CommonUIActionPlugin.getPluginId(), CommonUIActionPluginImages.IMG_MOVE_EDIT_ETOOL16);
		
	/**
	 * Imagedescriptor for the move action
	 */
	private static final ImageDescriptor DISABLED_MOVE_IMAGE = CommonUIActionPlugin.imageDescriptorFromPlugin
		(CommonUIActionPlugin.getPluginId(), CommonUIActionPluginImages.IMG_MOVE_EDIT_DTOOL16);
		
	/**
	 * @param workbenchPage
	 */
	public GlobalMoveAction(IWorkbenchPage workbenchPage) {
		super(workbenchPage);
	}

	/**
	 * @param workbenchPart
	 */
	public GlobalMoveAction(IWorkbenchPart workbenchPart) {
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
				: MOVE);

		/* set the label */
		setText(CommonUIActionMessages.GlobalMoveAction_label);

		/*  set the image */
		setImageDescriptor(MOVE_IMAGE);
		setHoverImageDescriptor(MOVE_IMAGE);
		setDisabledImageDescriptor(DISABLED_MOVE_IMAGE);

		/*  set the context sensitive help */
		PlatformUI.getWorkbench().getHelpSystem().setHelp(this, IHelpContextIds.PX_U_DEFAULT_CS_HELP);
		
		super.init();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.ui.action.internal.global.GlobalAction#getActionId()
	 */
	public String getActionId() {
		return GlobalActionId.MOVE;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.ui.action.AbstractActionHandler#isSelectionListener()
	 */
	protected boolean isSelectionListener() {
		return true;
	}
}
