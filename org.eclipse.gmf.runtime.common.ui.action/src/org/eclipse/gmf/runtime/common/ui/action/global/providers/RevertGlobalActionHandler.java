/******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.common.ui.action.global.providers;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.common.ui.action.internal.l10n.CommonUIActionMessages;
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

			String message = CommonUIActionMessages.RevertGlobalActionHandler_messageBox_message
				+ "\n\n" + CommonUIActionMessages.RevertGlobalActionHandler_messageBox_prompt; //$NON-NLS-1$

			MessageBox messageBox = new MessageBox(window.getShell(), SWT.YES
				| SWT.NO | SWT.CANCEL | SWT.ICON_QUESTION);

			messageBox.setText(CommonUIActionMessages.RevertGlobalActionHandler_messageBox_title);
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