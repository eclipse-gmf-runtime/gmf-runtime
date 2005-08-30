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
 * Global Paste Action
 * 
 * @author Vishy Ramaswamy
 */
public final class GlobalPasteAction extends GlobalAction {
    /**
     * Label definition of the paste action.
     */
    private static final String PASTE_TEXT = ResourceManager.getI18NString("GlobalPasteAction.label"); //$NON-NLS-1$

    /**
     * Action definition id of the paste action.
     */
    private static final String PASTE = "org.eclipse.gmf.runtime.common.ui.actions.global.paste"; //$NON-NLS-1$

    /**
     * Imagedescriptor for the paste action
     */
    private static final ImageDescriptor PASTE_IMAGE = ResourceManager.getInstance().getImageDescriptor("full/etool16/paste_edit.gif"); //$NON-NLS-1$

    /**
     * Imagedescriptor for the paste action
     */
    private static final ImageDescriptor DISABLED_PASTE_IMAGE = ResourceManager.getInstance().getImageDescriptor("full/dtool16/paste_edit.gif"); //$NON-NLS-1$

    /**
     * Imagedescriptor for the paste action
     */
    private static final ImageDescriptor HOVER_PASTE_IMAGE = ResourceManager.getInstance().getImageDescriptor("full/ctool16/paste_edit.gif"); //$NON-NLS-1$

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
        setText(PASTE_TEXT);

        /* set the image */
        setImageDescriptor(PASTE_IMAGE);
        setHoverImageDescriptor(HOVER_PASTE_IMAGE);
        setDisabledImageDescriptor(DISABLED_PASTE_IMAGE);

        /* set the context sensitive help */
        WorkbenchHelp.setHelp(this, IHelpContextIds.PX_U_DEFAULT_CS_HELP);
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
