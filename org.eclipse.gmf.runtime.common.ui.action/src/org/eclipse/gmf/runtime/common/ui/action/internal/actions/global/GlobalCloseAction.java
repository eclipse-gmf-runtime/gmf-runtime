/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.common.ui.action.internal.actions.global;

import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.help.WorkbenchHelp;

import org.eclipse.gmf.runtime.common.ui.action.global.GlobalAction;
import org.eclipse.gmf.runtime.common.ui.action.global.GlobalActionId;
import org.eclipse.gmf.runtime.common.ui.action.internal.IHelpContextIds;
import org.eclipse.gmf.runtime.common.ui.action.internal.l10n.ResourceManager;

/**
 * Global Close Action
 * 
 * @author Michael Yee
 */
public class GlobalCloseAction extends GlobalAction {

    /**
     * Constructor for GlobalCloseAction
     * @param workbenchPart The part associated with this action
     */
    public GlobalCloseAction(IWorkbenchPart workbenchPart) {
        super(workbenchPart);
    }

    /**
     * @param workbenchPage
     */
    public GlobalCloseAction(IWorkbenchPage workbenchPage) {
        super(workbenchPage);
    }

    /* (non-Javadoc)
     * @see org.eclipse.gmf.runtime.common.ui.action.IDisposableAction#init()
     */
    public void init() {

        // Set the id
        setId(
            getWorkbenchActionConstant() != null
                ? getWorkbenchActionConstant()
                : getActionId());

        // Set the label
        setText(ResourceManager.getI18NString("GlobalCloseAction.label")); //$NON-NLS-1$

        // Do not define image descriptors. The "Close" action does not have one.

        // Set the context sensitive help
        WorkbenchHelp.setHelp(this, IHelpContextIds.PX_U_DEFAULT_CS_HELP);
        super.init();
   }

    /* (non-Javadoc)
     * @see org.eclipse.gmf.runtime.common.ui.action.internal.global.GlobalAction#getActionId()
     */
    public String getActionId() {
        return GlobalActionId.CLOSE;
    }
    
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.ui.action.AbstractActionHandler#isSelectionListener()
	 */
	protected boolean isSelectionListener() {
		return true;
	}
}
