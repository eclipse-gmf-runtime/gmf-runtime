/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.common.ui.action.ide.actions.global;

import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.help.WorkbenchHelp;

import org.eclipse.gmf.runtime.common.ui.action.global.GlobalAction;
import org.eclipse.gmf.runtime.common.ui.action.ide.global.IDEGlobalActionId;
import org.eclipse.gmf.runtime.common.ui.action.ide.internal.IHelpContextIds;
import org.eclipse.gmf.runtime.common.ui.action.ide.internal.l10n.ResourceManager;

/** Global Bookmark Action
 * <p>
 * This action provides the mechanism for enabling the Edit->Add Bookmark...
 * menu item.
 * <p>
 * @author Kevin Cornell
 */
public class GlobalBookmarkAction extends GlobalAction {

    /**
     * @param workbenchPage
     */
    public GlobalBookmarkAction(IWorkbenchPage workbenchPage) {
        super(workbenchPage);
    }

    /**
     * @param workbenchPart
     */
    public GlobalBookmarkAction(IWorkbenchPart workbenchPart) {
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
                : getActionId());

        /* Set the label */
        setText(ResourceManager.getI18NString("GlobalBookmarkAction.label")); //$NON-NLS-1$

        /* Set the context sensitive help */
        WorkbenchHelp.setHelp(this, IHelpContextIds.PX_U_DEFAULT_CS_HELP);

        super.init();
    }

    /* (non-Javadoc)
     * @see org.eclipse.gmf.runtime.common.ui.action.internal.global.GlobalAction#getActionId()
     */
    public String getActionId() {
        return IDEGlobalActionId.BOOKMARK;
    }

    /* (non-Javadoc)
     * @see org.eclipse.gmf.runtime.common.ui.action.AbstractActionHandler#isSelectionListener()
     */
    protected boolean isSelectionListener() {
        return true;
    }
}
