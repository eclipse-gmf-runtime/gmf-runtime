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
 * Global Properties Action
 * 
 * @author Vishy Ramaswamy
 */
public final class GlobalPropertiesAction extends GlobalAction {

    /**
     * Action definition id of the properties action.
     */
    private static final String PROPERTIES = "org.eclipse.gmf.runtime.common.ui.actions.global.properties"; //$NON-NLS-1$

	/**
	 * @param workbenchPage
	 */
	public GlobalPropertiesAction(IWorkbenchPage workbenchPage) {
		super(workbenchPage);
	}


    /**
     * @param workbenchPart
     */
    public GlobalPropertiesAction(IWorkbenchPart workbenchPart) {
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
                : PROPERTIES);

        /* set the label */
        setText(CommonUIActionMessages.GlobalPropertiesAction_label);

        /*  set the context sensitive help */
        PlatformUI.getWorkbench().getHelpSystem().setHelp(this, IHelpContextIds.PX_U_DEFAULT_CS_HELP);
		
		super.init();
    }

    /* (non-Javadoc)
     * @see org.eclipse.gmf.runtime.common.ui.action.internal.global.GlobalAction#getActionId()
     */
    public String getActionId() {
        return GlobalActionId.PROPERTIES;
    }

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.ui.action.AbstractActionHandler#isSelectionListener()
	 */
	protected boolean isSelectionListener() {
		return true;
	}
}
