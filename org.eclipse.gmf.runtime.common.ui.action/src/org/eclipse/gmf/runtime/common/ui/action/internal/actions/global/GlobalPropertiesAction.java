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

import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.help.WorkbenchHelp;

import org.eclipse.gmf.runtime.common.ui.action.global.GlobalAction;
import org.eclipse.gmf.runtime.common.ui.action.global.GlobalActionId;
import org.eclipse.gmf.runtime.common.ui.action.internal.IHelpContextIds;
import org.eclipse.gmf.runtime.common.ui.action.internal.l10n.ResourceManager;

/**
 * Global Properties Action
 * 
 * @author Vishy Ramaswamy
 */
public final class GlobalPropertiesAction extends GlobalAction {
    /**
     * Label definition of the properties action.
     */
    private static final String PROPERTIES_TEXT = ResourceManager.getI18NString("GlobalPropertiesAction.label"); //$NON-NLS-1$

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
        setText(PROPERTIES_TEXT);

        /*  set the context sensitive help */
        WorkbenchHelp.setHelp(this, IHelpContextIds.PX_U_DEFAULT_CS_HELP);
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
