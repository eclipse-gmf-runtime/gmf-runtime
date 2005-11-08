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
 * Global Rename Action
 * 
 * @author Vishy Ramaswamy
 */
public final class GlobalRenameAction extends GlobalAction {
 
    /**
     * Action definition id of the rename action.
     */
    private static final String RENAME = "org.eclipse.gmf.runtime.common.ui.actions.global.rename"; //$NON-NLS-1$

    /**
     * Imagedescriptor for the rename action
     */
    private static final ImageDescriptor RENAME_IMAGE = CommonUIActionPlugin.imageDescriptorFromPlugin
    	(CommonUIActionPlugin.getPluginId(), CommonUIActionPluginImages.IMG_RENAME_EDIT_ETOOL16);

    /**
     * Imagedescriptor for the rename action
     */
    private static final ImageDescriptor DISABLED_RENAME_IMAGE = CommonUIActionPlugin.imageDescriptorFromPlugin
    	(CommonUIActionPlugin.getPluginId(), CommonUIActionPluginImages.IMG_RENAME_EDIT_DTOOL16);
    
    /**
     * Imagedescriptor for the rename action
     */
    private static final ImageDescriptor HOVER_RENAME_IMAGE = CommonUIActionPlugin.imageDescriptorFromPlugin
    	(CommonUIActionPlugin.getPluginId(), CommonUIActionPluginImages.IMG_RENAME_EDIT_CTOOL16);
    
	/**
	 * @param workbenchPage
	 */
	public GlobalRenameAction(IWorkbenchPage workbenchPage) {
		super(workbenchPage);
	}

    /**
     * @param workbenchPart
     */
    public GlobalRenameAction(IWorkbenchPart workbenchPart) {
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
                : RENAME);

        /* set the label */
        setText(CommonUIActionMessages.GlobalRenameAction_label);

        /*  set the image */
        setImageDescriptor(RENAME_IMAGE);
        setHoverImageDescriptor(HOVER_RENAME_IMAGE);
        setDisabledImageDescriptor(DISABLED_RENAME_IMAGE);

        /*  set the context sensitive help */
        PlatformUI.getWorkbench().getHelpSystem().setHelp(this, IHelpContextIds.PX_U_DEFAULT_CS_HELP);
		
		super.init();
    }

    /* (non-Javadoc)
     * @see org.eclipse.gmf.runtime.common.ui.action.internal.global.GlobalAction#getActionId()
     */
    public String getActionId() {
        return GlobalActionId.RENAME;
    }

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.ui.action.AbstractActionHandler#isSelectionListener()
	 */
	protected boolean isSelectionListener() {
		return true;
	}
}
