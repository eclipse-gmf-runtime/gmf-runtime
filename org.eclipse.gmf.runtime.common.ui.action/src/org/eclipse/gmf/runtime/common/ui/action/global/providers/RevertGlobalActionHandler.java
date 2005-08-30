/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.common.ui.action.global.providers;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.common.ui.action.internal.l10n.ResourceManager;
import org.eclipse.gmf.runtime.common.ui.editors.IRevertiblePart;
import org.eclipse.gmf.runtime.common.ui.services.action.global.AbstractGlobalActionHandler;
import org.eclipse.gmf.runtime.common.ui.services.action.global.IGlobalActionContext;

/**
 * A default global action handler for the revert retargetable action.
 * 
 * @author ldamus
 */
public class RevertGlobalActionHandler
	extends AbstractGlobalActionHandler {

	private final static String CONFIRMATION_DIALOG_TITLE = ResourceManager
		.getI18NString("RevertGlobalActionHandler.messageBox.title"); //$NON-NLS-1$

	private final static String CONFIRMATION_DIALOG_MESSAGE = ResourceManager
		.getI18NString("RevertGlobalActionHandler.messageBox.message"); //$NON-NLS-1$

	private final static String CONFIRMATION_DIALOG_PROMPT = ResourceManager
		.getI18NString("RevertGlobalActionHandler.messageBox.prompt"); //$NON-NLS-1$

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.ui.internal.action.global.IGlobalActionHandler#canHandle(org.eclipse.gmf.runtime.common.ui.internal.action.global.IGlobalActionContext)
	 */
	public boolean canHandle(IGlobalActionContext context) {
		IRevertiblePart revertablePart = getRevertablePart(context
			.getActivePart());

		if (revertablePart != null) {
			return revertablePart.isDirty();
		}

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.ui.internal.action.global.IGlobalActionHandler#getCommand(org.eclipse.gmf.runtime.common.ui.internal.action.global.IGlobalActionContext)
	 */
	public ICommand getCommand(IGlobalActionContext context) {

		IRevertiblePart revertablePart = getRevertablePart(context
			.getActivePart());

		if (revertablePart != null) {

			if (confirmRevert()) {
				revertablePart.doRevertToSaved();
			}
		}

		return null;
	}

	/**
	 * Prompts the user for confirmation of the revert action.
	 * 
	 * @return <code>true</code> if the user confirms that they want to
	 *         revert, <code>false</code> otherwise.
	 */
	private boolean confirmRevert() {
		IWorkbenchWindow window = PlatformUI.getWorkbench()
			.getActiveWorkbenchWindow();

		if (window != null) {

			String message = CONFIRMATION_DIALOG_MESSAGE
				+ "\n\n" + CONFIRMATION_DIALOG_PROMPT; //$NON-NLS-1$

			MessageBox messageBox = new MessageBox(window.getShell(), SWT.YES
				| SWT.NO | SWT.CANCEL | SWT.ICON_QUESTION);

			messageBox.setText(CONFIRMATION_DIALOG_TITLE);
			messageBox.setMessage(message);

			if (messageBox.open() == SWT.YES) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Casts or adapts the <code>workbenchPart</code> to an
	 * <code>IRevertiblePart</code>, if possible.
	 * 
	 * @param workbenchPart
	 *            the part
	 * @return the <code>IRevertiblePart</code>, or <code>null</code> if
	 *         the <code>workbenchPart</code> could not be adapted to an
	 *         <code>IRevertiblePart</code>.
	 */
	private IRevertiblePart getRevertablePart(IWorkbenchPart workbenchPart) {

		IRevertiblePart revertablePart = null;

		if (workbenchPart != null) {

			if (workbenchPart instanceof IRevertiblePart) {
				revertablePart = (IRevertiblePart) workbenchPart;

			} else {
				revertablePart = (IRevertiblePart) workbenchPart
					.getAdapter(IRevertiblePart.class);
			}
		}

		return revertablePart;
	}
}