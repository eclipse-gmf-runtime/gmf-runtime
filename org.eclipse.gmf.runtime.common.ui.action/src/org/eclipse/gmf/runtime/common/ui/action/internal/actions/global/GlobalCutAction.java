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
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

import org.eclipse.gmf.runtime.common.ui.action.actions.global.ClipboardManager;
import org.eclipse.gmf.runtime.common.ui.action.actions.global.GlobalActionManager;
import org.eclipse.gmf.runtime.common.ui.action.global.GlobalAction;
import org.eclipse.gmf.runtime.common.ui.action.global.GlobalActionId;
import org.eclipse.gmf.runtime.common.ui.action.internal.IHelpContextIds;
import org.eclipse.gmf.runtime.common.ui.action.internal.l10n.CommonUIActionMessages;

/**
 * Global Cut Action
 * 
 * @author Vishy Ramaswamy
 */
public final class GlobalCutAction extends GlobalAction {

    /**
     * Action definition id of the cut action.
     */
    private static final String CUT = "org.eclipse.gmf.runtime.common.ui.actions.global.cut"; //$NON-NLS-1$

	/**
	 * @param workbenchPage
	 */
	public GlobalCutAction(IWorkbenchPage workbenchPage) {
		super(workbenchPage);
	}

    /**
     * @param workbenchPart
     */
    public GlobalCutAction(IWorkbenchPart workbenchPart) {
        super(workbenchPart);
    }
    
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.ui.action.IDisposableAction#init()
	 */
	public void init(){
        /* set the id */
        setId(
            getWorkbenchActionConstant() != null
                ? getWorkbenchActionConstant()
                : CUT);

        /* set the label */
        setText(CommonUIActionMessages.GlobalCutAction_label);

        /*  set the image */
        ISharedImages sharedImages = PlatformUI.getWorkbench().getSharedImages();
        setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_CUT));
        setHoverImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_CUT));
        setDisabledImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_CUT_DISABLED));

        /* set the context sensitive help */
        PlatformUI.getWorkbench().getHelpSystem().setHelp(this, IHelpContextIds.PX_U_DEFAULT_CS_HELP);
		
		super.init();
    }

    /* (non-Javadoc)
     * @see org.eclipse.gmf.runtime.common.ui.action.internal.global.GlobalAction#getActionId()
     */
    public String getActionId() {
        return GlobalActionId.CUT;
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
