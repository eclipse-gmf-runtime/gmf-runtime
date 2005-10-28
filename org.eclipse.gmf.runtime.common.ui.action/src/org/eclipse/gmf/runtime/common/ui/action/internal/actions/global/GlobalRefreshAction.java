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
import org.eclipse.gmf.runtime.common.ui.action.internal.IHelpContextIds;
import org.eclipse.gmf.runtime.common.ui.action.internal.l10n.CommonUIActionMessages;
import org.eclipse.gmf.runtime.common.ui.action.internal.l10n.ResourceManager;

/**
 * Global Refresh Action
 * 
 * @author Vishy Ramaswamy
 */
public final class GlobalRefreshAction extends GlobalAction {

    /**
     * Action definition id of the REFRESH action.
     */
    private static final String REFRESH = "org.eclipse.gmf.runtime.common.ui.actions.global.refresh"; //$NON-NLS-1$

    /**
     * Imagedescriptor for the REFRESH action
     */
    private static final ImageDescriptor REFRESH_IMAGE = ResourceManager.getInstance().getImageDescriptor("full/etool16/refresh_nav.gif"); //$NON-NLS-1$

    /**
     * Imagedescriptor for the REFRESH action
     */
    private static final ImageDescriptor DISABLED_REFRESH_IMAGE = ResourceManager.getInstance().getImageDescriptor("full/dtool16/refresh_nav.gif"); //$NON-NLS-1$

    /**
     * Imagedescriptor for the REFRESH action
     */
    private static final ImageDescriptor HOVER_REFRESH_IMAGE = ResourceManager.getInstance().getImageDescriptor("full/ctool16/refresh_nav.gif"); //$NON-NLS-1$

	/**
	 * @param workbenchPage
	 */
	public GlobalRefreshAction(IWorkbenchPage workbenchPage) {
		super(workbenchPage);
	}


    /**
     * @param workbenchPart
     */
    public GlobalRefreshAction(IWorkbenchPart workbenchPart) {
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
                : REFRESH);

        /* set the label */
        setText(CommonUIActionMessages.GlobalRefreshAction_label);

        /*  set the image */
        setImageDescriptor(REFRESH_IMAGE);
        setHoverImageDescriptor(HOVER_REFRESH_IMAGE);
        setDisabledImageDescriptor(DISABLED_REFRESH_IMAGE);

        /*  set the context sensitive help */
        PlatformUI.getWorkbench().getHelpSystem().setHelp(this, IHelpContextIds.PX_U_DEFAULT_CS_HELP);
		
		super.init();
    }

    /* (non-Javadoc)
     * @see org.eclipse.gmf.runtime.common.ui.action.internal.global.GlobalAction#getActionId()
     */
    public String getActionId() {
        return GlobalActionId.REFRESH;
    }

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.ui.action.AbstractActionHandler#isSelectionListener()
	 */
	protected boolean isSelectionListener() {
		return true;
	}
}
