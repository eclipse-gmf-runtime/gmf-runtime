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

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.help.WorkbenchHelp;

import org.eclipse.gmf.runtime.common.ui.action.global.GlobalAction;
import org.eclipse.gmf.runtime.common.ui.action.global.GlobalActionId;
import org.eclipse.gmf.runtime.common.ui.action.internal.IHelpContextIds;
import org.eclipse.gmf.runtime.common.ui.action.internal.l10n.ResourceManager;

/**
 * Global Move Action
 * 
 * @author Vishy Ramaswamy
 */
public final class GlobalMoveAction extends GlobalAction {
	/**
	 * Label definition of the move action.
	 */
	private static final String MOVE_TEXT = ResourceManager.getI18NString("GlobalMoveAction.label"); //$NON-NLS-1$

	/**
	 * Action definition id of the move action.
	 */
	private static final String MOVE = "org.eclipse.gmf.runtime.common.ui.actions.global.move"; //$NON-NLS-1$

	/**
	 * Imagedescriptor for the move action
	 */
	private static final ImageDescriptor MOVE_IMAGE = ResourceManager.getInstance().getImageDescriptor("full/etool16/move_edit.gif"); //$NON-NLS-1$

	/**
	 * Imagedescriptor for the move action
	 */
	private static final ImageDescriptor DISABLED_MOVE_IMAGE = ResourceManager.getInstance().getImageDescriptor("full/dtool16/move_edit.gif"); //$NON-NLS-1$

	/**
	 * Imagedescriptor for the move action
	 */
	private static final ImageDescriptor HOVER_MOVE_IMAGE = ResourceManager.getInstance().getImageDescriptor("full/ctool16/move_edit.gif"); //$NON-NLS-1$

	/**
	 * @param workbenchPage
	 */
	public GlobalMoveAction(IWorkbenchPage workbenchPage) {
		super(workbenchPage);
	}

	/**
	 * @param workbenchPart
	 */
	public GlobalMoveAction(IWorkbenchPart workbenchPart) {
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
				: MOVE);

		/* set the label */
		setText(MOVE_TEXT);

		/*  set the image */
		setImageDescriptor(MOVE_IMAGE);
		setHoverImageDescriptor(HOVER_MOVE_IMAGE);
		setDisabledImageDescriptor(DISABLED_MOVE_IMAGE);

		/*  set the context sensitive help */
		WorkbenchHelp.setHelp(this, IHelpContextIds.PX_U_DEFAULT_CS_HELP);
		super.init();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.ui.action.internal.global.GlobalAction#getActionId()
	 */
	public String getActionId() {
		return GlobalActionId.MOVE;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.ui.action.AbstractActionHandler#isSelectionListener()
	 */
	protected boolean isSelectionListener() {
		return true;
	}
}
