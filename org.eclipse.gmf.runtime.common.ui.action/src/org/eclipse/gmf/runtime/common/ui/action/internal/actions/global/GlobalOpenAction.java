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

import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.help.WorkbenchHelp;

import org.eclipse.gmf.runtime.common.ui.action.global.GlobalAction;
import org.eclipse.gmf.runtime.common.ui.action.global.GlobalActionId;
import org.eclipse.gmf.runtime.common.ui.action.internal.IHelpContextIds;
import org.eclipse.gmf.runtime.common.ui.action.internal.l10n.ResourceManager;

/** 
 * Global Open Action - this action provides a global "open" type of action. 
 * 
 * <p>Typically, a double click on some UI representation for a document 
 * causes that document to be opened. In order to override the default 
 * double click behaviour, a contribution should be added for this "open" 
 * action and when the view/editor processes a double click, it should obtain 
 * this action from the Global Action Manager and call its run method to 
 * perform the desired navigation which in turn will call the appropriate 
 * global action handler.
 * 
 * @author Kevin Cornell
 */
public final class GlobalOpenAction extends GlobalAction {

	/**
	 * @param workbenchPage
	 */
	public GlobalOpenAction(IWorkbenchPage workbenchPage) {
		super(workbenchPage);
	}

    /**
     * @param workbenchPart
     */
    public GlobalOpenAction(IWorkbenchPart workbenchPart) {
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
        setText(ResourceManager.getI18NString("GlobalOpenAction.label")); //$NON-NLS-1$

        /* Do not define image dewscriptors. The "Open" command does not have them. */

        /* Set the context sensitive help */
        WorkbenchHelp.setHelp(this, IHelpContextIds.PX_U_DEFAULT_CS_HELP);
		super.init();
   }

    /* (non-Javadoc)
     * @see org.eclipse.gmf.runtime.common.ui.action.internal.global.GlobalAction#getActionId()
     */
    public String getActionId() {
        return GlobalActionId.OPEN;
    }

    /**
     * Returns the workbenchActionConstant. Since there is no equivalent workbench
     * (retargetable) action for open, return NULL. This will prevent the global action
     * manager from attempting to add this action to the part's action bars. 
     * (see GlobalActionManager.setGlobalActionHandlers()). 
     * 
     * If a future version of Eclipse provides a retargetable "open" action, simply delete 
     * this method from here and use the superclass method.
     * 
     * @return String
     */
    public String getWorkbenchActionConstant() {
        return null;
    }

    /** 
     * This method overrides the default run() method in AbstractActionHandler to prevent
     * calls being made when in an invalid state. When a double click occurs, a workbench
     * part should obtain this global action, and if enabled call this run() method. 
     * However,  if this method is called when the action is not enabled, the corresponding
     * action handlers could be in an invalid state. Therefore, only perform the run() if
     * the action is enabled.
     */
    public void run() {
        if (isEnabled()) {
            super.run();
        }
    }

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.ui.action.AbstractActionHandler#isSelectionListener()
	 */
	protected boolean isSelectionListener() {
		return true;
	}
}
