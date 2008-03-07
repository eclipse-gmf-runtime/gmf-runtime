/******************************************************************************
 * Copyright (c) 2002, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.common.ui.action.actions.global;
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
 * Global action for printing
 * 
 * @author Wayne Diu, wdiu
 */
public final class GlobalPrintAction extends GlobalAction {

	/**
	 * Imagedescriptor for the print action
	 */
	private static final ImageDescriptor PRINT_IMAGE = CommonUIActionPlugin.imageDescriptorFromPlugin
		(CommonUIActionPlugin.getPluginId(), CommonUIActionPluginImages.IMG_PRINT_EDIT_ETOOL16);
	/**
	 * Imagedescriptor for the print action
	 */
	private static final ImageDescriptor DISABLED_PRINT_IMAGE = CommonUIActionPlugin.imageDescriptorFromPlugin
		(CommonUIActionPlugin.getPluginId(), CommonUIActionPluginImages.IMG_PRINT_EDIT_DTOOL16);
		
	/**
	 * @param workbenchPage
	 */
	public GlobalPrintAction(IWorkbenchPage workbenchPage) {
		super(workbenchPage);
	}


	/**
	 * @param workbenchPart
	 */
	public GlobalPrintAction(IWorkbenchPart workbenchPart) {
		super(workbenchPart);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.ui.action.IDisposableAction#init()
	 */
	public void init() {
		/* set the id */
		setId(getWorkbenchActionConstant());

		/* set the label */
		setText(CommonUIActionMessages.GlobalPrintAction_label);

		/* change the image in case someone tries this from a context menu,
		 * not needed from the file menu */
		setImageDescriptor(PRINT_IMAGE);
		setHoverImageDescriptor(PRINT_IMAGE);
		setDisabledImageDescriptor(DISABLED_PRINT_IMAGE);

		/* set the context sensitive help */
		PlatformUI.getWorkbench().getHelpSystem().setHelp(this, IHelpContextIds.PX_U_DEFAULT_CS_HELP);
		
		super.init();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.ui.action.internal.global.GlobalAction#getActionId()
	 */
	public String getActionId() {
		return GlobalActionId.PRINT;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.ui.action.IRepeatableAction#refresh()
	 */
	public void refresh() {
		//
		// In phase 2 of printing enhancements we will remove the OS restriction.
		//
		setEnabled(!getGlobalActionHandlerData().isEmpty() && System.getProperty("os.name").toUpperCase().startsWith("WIN")); //$NON-NLS-1$ //$NON-NLS-2$
		
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.ui.action.AbstractActionHandler#isSelectionListener()
	 */
	protected boolean isSelectionListener() {
		return true;
	}
}
