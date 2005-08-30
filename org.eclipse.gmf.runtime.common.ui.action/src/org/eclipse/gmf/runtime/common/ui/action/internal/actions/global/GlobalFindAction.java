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
 * Global Find Action
 * 
 * @author Vishy Ramaswamy
 */
public final class GlobalFindAction extends GlobalAction {
	/**
	 * Label definition of the find action.
	 */
	private static final String FIND_TEXT = ResourceManager.getI18NString("GlobalFindAction.label"); //$NON-NLS-1$

	/**
	 * Action definition id of the find action.
	 */
	private static final String FIND = "org.eclipse.gmf.runtime.common.ui.actions.global.find"; //$NON-NLS-1$

	/**
	 * @param workbenchPage
	 */
	public GlobalFindAction(IWorkbenchPage workbenchPage) {
		super(workbenchPage);
	}

	/**
	 * @param workbenchPart
	 */
	public GlobalFindAction(IWorkbenchPart workbenchPart) {
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
				: FIND);

		/* set the label */
		setText(FIND_TEXT);

		/*  set the context sensitive help */
		WorkbenchHelp.setHelp(this, IHelpContextIds.PX_U_DEFAULT_CS_HELP);

		super.init();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.ui.action.internal.global.GlobalAction#getActionId()
	 */
	public String getActionId() {
		return GlobalActionId.FIND;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.ui.action.IRepeatableAction#refresh()
	 */
	public void refresh() {
		setEnabled(!getGlobalActionHandlerData().isEmpty());
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.ui.action.AbstractActionHandler#isSelectionListener()
	 */
	protected boolean isSelectionListener() {
		return true;
	}
}