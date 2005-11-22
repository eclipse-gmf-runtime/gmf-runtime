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
 * Global Undo Action
 * This class simply delegates all the calls to the <code>UndoActionHandler</code>
 * 
 * @author vramaswa
 */
public final class GlobalUndoAction extends GlobalAction {
    
    /**
     * Action definition id of the undo action.
     */
    private static final String UNDO = "org.eclipse.gmf.runtime.common.ui.actions.global.undo"; //$NON-NLS-1$

	/**
	 * @param workbenchPage
	 */
	public GlobalUndoAction(IWorkbenchPage workbenchPage) {
		super(workbenchPage);
	}


    /**
     * @param workbenchPart
     */
    public GlobalUndoAction(IWorkbenchPart workbenchPart) {
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
                : UNDO);

        /* Set the label */
        setText(CommonUIActionMessages.GlobalUndoAction_label);

        /* Set the image */
        ISharedImages sharedImages = PlatformUI.getWorkbench().getSharedImages();
        setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_UNDO));
        setHoverImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_UNDO));
        setDisabledImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_UNDO_DISABLED));

        /* Set the context sensitive help */
        PlatformUI.getWorkbench().getHelpSystem().setHelp(this, IHelpContextIds.PX_U_DEFAULT_CS_HELP);
		
		super.init();
    }

    /* (non-Javadoc)
     * @see org.eclipse.gmf.runtime.common.ui.action.internal.global.GlobalAction#getActionId()
     */
    public String getActionId() {
        return GlobalActionId.UNDO;
    }

    /* (non-Javadoc)
     * @see org.eclipse.gmf.runtime.common.ui.action.AbstractActionHandler#doRun(org.eclipse.core.runtime.IProgressMonitor)
     */
    protected void doRun(IProgressMonitor progressMonitor) {
    	try {
    		// RATLC00138974 - prevents the user trying to undo while undo is in progress.
    		setEnabled(false);
    		getCommandManager().undo();
    	} finally {
    		refresh();
    	}
    }

    /* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.ui.action.IRepeatableAction#refresh()
	 */
	public void refresh() {
		setEnabled(getCommandManager().canUndo());

		String label = CommonUIActionMessages.GlobalUndoAction_label;
		String commandLabel = getCommandManager().getUndoLabel();
		if (commandLabel != null) {
			label = MessageFormat.format(CommonUIActionMessages.GlobalUndoAction_formattedLabel,
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
