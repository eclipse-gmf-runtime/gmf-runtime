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
 * Global Paste Action
 * 
 * @author Vishy Ramaswamy
 */
public final class GlobalPasteAction extends GlobalAction {

    /**
     * Action definition id of the paste action.
     */
    private static final String PASTE = "org.eclipse.gmf.runtime.common.ui.actions.global.paste"; //$NON-NLS-1$

    /**
     * Imagedescriptor for the paste action
     */
    private static final ImageDescriptor PASTE_IMAGE = CommonUIActionPlugin.imageDescriptorFromPlugin
    	(CommonUIActionPlugin.getPluginId(), CommonUIActionPluginImages.IMG_PASTE_EDIT_ETOOL16);
    	
    /**
     * Imagedescriptor for the paste action
     */
    private static final ImageDescriptor DISABLED_PASTE_IMAGE = CommonUIActionPlugin.imageDescriptorFromPlugin
    	(CommonUIActionPlugin.getPluginId(), CommonUIActionPluginImages.IMG_PASTE_EDIT_DTOOL16);

    /**
     * Imagedescriptor for the paste action
     */
    private static final ImageDescriptor HOVER_PASTE_IMAGE = CommonUIActionPlugin.imageDescriptorFromPlugin
    	(CommonUIActionPlugin.getPluginId(), CommonUIActionPluginImages.IMG_PASTE_EDIT_CTOOL16);
    
	/**
	 * @param workbenchPage
	 */
	public GlobalPasteAction(IWorkbenchPage workbenchPage) {
		super(workbenchPage);
	}

    /**
     * @param workbenchPart
     */
    public GlobalPasteAction(IWorkbenchPart workbenchPart) {
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
                : PASTE);

        /* set the label */
        setText(CommonUIActionMessages.GlobalPasteAction_label);

        /* set the image */
        setImageDescriptor(PASTE_IMAGE);
        setHoverImageDescriptor(HOVER_PASTE_IMAGE);
        setDisabledImageDescriptor(DISABLED_PASTE_IMAGE);

        /* set the context sensitive help */
        PlatformUI.getWorkbench().getHelpSystem().setHelp(this, IHelpContextIds.PX_U_DEFAULT_CS_HELP);
		
        super.init();
    }

    /* (non-Javadoc)
     * @see org.eclipse.gmf.runtime.common.ui.action.internal.global.GlobalAction#getActionId()
     */
    public String getActionId() {
        return GlobalActionId.PASTE;
    }

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.ui.action.AbstractActionHandler#isSelectionListener()
	 */
	protected boolean isSelectionListener() {
		return true;
	}
}
