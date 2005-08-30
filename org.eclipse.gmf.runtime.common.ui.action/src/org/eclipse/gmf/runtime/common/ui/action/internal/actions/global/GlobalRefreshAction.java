/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2005.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.common.ui.action.internal.actions.global;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.help.WorkbenchHelp;

import org.eclipse.gmf.runtime.common.ui.action.global.GlobalAction;
import org.eclipse.gmf.runtime.common.ui.action.global.GlobalActionId;
import org.eclipse.gmf.runtime.common.ui.action.internal.IHelpContextIds;
import org.eclipse.gmf.runtime.common.ui.action.internal.l10n.ResourceManager;

/**
 * Global Refresh Action
 * 
 * @author Vishy Ramaswamy
 */
public final class GlobalRefreshAction extends GlobalAction {
    /**
     * Label definition of the REFRESH action.
     */
    private static final String REFRESH_TEXT = ResourceManager.getI18NString("GlobalRefreshAction.label"); //$NON-NLS-1$

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
        setText(REFRESH_TEXT);

        /*  set the image */
        setImageDescriptor(REFRESH_IMAGE);
        setHoverImageDescriptor(HOVER_REFRESH_IMAGE);
        setDisabledImageDescriptor(DISABLED_REFRESH_IMAGE);

        /*  set the context sensitive help */
        WorkbenchHelp.setHelp(this, IHelpContextIds.PX_U_DEFAULT_CS_HELP);
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
