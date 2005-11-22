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

import java.text.MessageFormat;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

import org.eclipse.gmf.runtime.common.ui.action.global.GlobalAction;
import org.eclipse.gmf.runtime.common.ui.action.global.GlobalActionId;
import org.eclipse.gmf.runtime.common.ui.action.internal.IHelpContextIds;
import org.eclipse.gmf.runtime.common.ui.action.internal.l10n.CommonUIActionMessages;

/**
 * Global Redo Action
 * This class simply delegates all the calls to the <code>RedoActionHandler</code>
 * 
 * @author vramaswa
 */
public final class GlobalRedoAction extends GlobalAction {
    
    /**
     * Action definition id of the redo action.
     */
    private static final String REDO = "org.eclipse.gmf.runtime.common.ui.actions.global.redo"; //$NON-NLS-1$

	/**
	 * @param workbenchPage
	 */
	public GlobalRedoAction(IWorkbenchPage workbenchPage) {
		super(workbenchPage);
	}

    /**
     * @param workbenchPart
     */
    public GlobalRedoAction(IWorkbenchPart workbenchPart) {
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
                : REDO);

        /* Set the label */
        setText(CommonUIActionMessages.GlobalRedoAction_label);

        /* Set the image */
        ISharedImages sharedImages = PlatformUI.getWorkbench().getSharedImages();
        setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_REDO));
        setHoverImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_REDO));
        setDisabledImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_REDO_DISABLED));

        /* Set the context sensitive help */
        PlatformUI.getWorkbench().getHelpSystem().setHelp(this, IHelpContextIds.PX_U_DEFAULT_CS_HELP);
		
		super.init();
    }

    /* (non-Javadoc)
     * @see org.eclipse.gmf.runtime.common.ui.action.internal.global.GlobalAction#getActionId()
     */
    public String getActionId() {
        return GlobalActionId.REDO;
    }

    /* (non-Javadoc)
     * @see org.eclipse.gmf.runtime.common.ui.action.AbstractActionHandler#doRun(org.eclipse.core.runtime.IProgressMonitor)
     */
    protected void doRun(IProgressMonitor progressMonitor) {
    	try {
    		// RATLC00138974 - prevents the user trying to redo while redo is in progress.
    		setEnabled(false);
    		getCommandManager().redo();
    	} finally {
    		refresh();
    	}
    }

    /* (non-Javadoc)
     * @see org.eclipse.gmf.runtime.common.ui.action.IRepeatableAction#refresh()
     */
    public void refresh() {
		setEnabled(getCommandManager().canRedo());
		String label = CommonUIActionMessages.GlobalRedoAction_label;
		String commandLabel = getCommandManager().getRedoLabel();
		if (commandLabel != null) {
			label = MessageFormat.format(CommonUIActionMessages.GlobalRedoAction_formattedLabel,
				new Object[] {removeMnemonics(commandLabel)});
		}
		setText(label);
    }

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.ui.action.AbstractActionHandler#isCommandStackListener()
	 */
	protected boolean isCommandStackListener() {
		return true;
	}
}
