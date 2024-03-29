/******************************************************************************
 * Copyright (c) 2002, 2003 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.common.ui.action.ide.actions.global;

import org.eclipse.gmf.runtime.common.ui.action.global.GlobalAction;
import org.eclipse.gmf.runtime.common.ui.action.ide.global.IDEGlobalActionId;
import org.eclipse.gmf.runtime.common.ui.action.ide.internal.IHelpContextIds;
import org.eclipse.gmf.runtime.common.ui.action.ide.internal.l10n.CommonUIActionIDEMessages;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

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
        setText(CommonUIActionIDEMessages.GlobalBookmarkAction_label);

        /* Set the context sensitive help */
        PlatformUI.getWorkbench().getHelpSystem().setHelp(this, IHelpContextIds.PX_U_DEFAULT_CS_HELP);
		
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
