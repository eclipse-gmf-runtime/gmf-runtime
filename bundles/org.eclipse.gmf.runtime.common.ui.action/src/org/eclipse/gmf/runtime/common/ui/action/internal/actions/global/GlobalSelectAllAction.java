/******************************************************************************
 * Copyright (c) 2002, 2005 IBM Corporation and others.
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
import org.eclipse.gmf.runtime.common.ui.action.internal.IHelpContextIds;
import org.eclipse.gmf.runtime.common.ui.action.internal.l10n.CommonUIActionMessages;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

/**
 * Global SelectAll Action
 * 
 * @author vramaswa
 */
public final class GlobalSelectAllAction extends GlobalAction {
 
    /**
     * Action definition id of the select all action.
     */
    private static final String SELECT_ALL = "org.eclipse.gmf.runtime.common.ui.actions.global.selectAll"; //$NON-NLS-1$

	/**
	 * @param workbenchPage
	 */
	public GlobalSelectAllAction(IWorkbenchPage workbenchPage) {
		super(workbenchPage);
	}

    /**
     * Constructor for GlobalSelectAllAction.
     * @param workbenchPart
     */
    public GlobalSelectAllAction(IWorkbenchPart workbenchPart) {
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
                : SELECT_ALL);

        /* Set the label */
        setText(CommonUIActionMessages.SelectAllAction_label);

        /* Set the context sensitive help */
        PlatformUI.getWorkbench().getHelpSystem().setHelp(this, IHelpContextIds.PX_U_DEFAULT_CS_HELP);
		
		super.init();
    }

    /* (non-Javadoc)
     * @see org.eclipse.gmf.runtime.common.ui.action.internal.global.GlobalAction#getActionId()
     */
    public String getActionId() {
        return GlobalActionId.SELECT_ALL;
    }

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.ui.action.AbstractActionHandler#isSelectionListener()
	 */
	protected boolean isSelectionListener() {
		return true;
	}
}
