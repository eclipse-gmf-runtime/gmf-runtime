/******************************************************************************
 * Copyright (c) 2002, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.common.ui.util;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.gmf.runtime.common.core.util.Log;
import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.common.ui.internal.CommonUIDebugOptions;
import org.eclipse.gmf.runtime.common.ui.internal.CommonUIPlugin;
import org.eclipse.gmf.runtime.common.ui.internal.CommonUIStatusCodes;

/**
 * Utilities for windows.
 * There is one utility method here to center the dialog relative to its parent.
 * 
 * There are also several utility methods that can be used on controls in a
 * window.
 * 
 * @author wdiu, Wayne Diu
 */
public class WindowUtil {

	/**
	 * Center the dialog relative to a parent window.
	 * @param dialogShell contains the dialog to center
	 * @param parentShell contains the window to center relative to
	 */
	public static void centerDialog(Shell dialogShell, Shell parentShell) {
		try {
			Point dialogSize = dialogShell.getSize();
			//don't use Display and then get shell from there, since
			//we are trying to find the shell of the whole Eclipse window
			Point windowLocation = parentShell.getLocation();
			Point windowSize = parentShell.getSize();
			//do not take the absolute value.
			dialogShell.setLocation(
				((windowSize.x - dialogSize.x) / 2) + windowLocation.x,
				((windowSize.y - dialogSize.y) / 2) + windowLocation.y);
		} catch (Exception e) {
			//any exception when centering a dialog may be ignored
			Trace.catching(CommonUIPlugin.getDefault(), CommonUIDebugOptions.EXCEPTIONS_CATCHING, WindowUtil.class, "Failed to center dialog", e); //$NON-NLS-1$
            Log.error(CommonUIPlugin.getDefault(), CommonUIStatusCodes.GENERAL_UI_FAILURE, "Failed to center dialog", e); //$NON-NLS-1$
		}
	}

	/**
	 * Dispose a parent's children
	 * 
	 * @param parent composite containing children to be disposed.
	 */
	public static void disposeChildren(Composite parent) {
		Control[] children = parent.getChildren();
		for (int i = 0; i < children.length; i++) {
			children[i].dispose();
		}
	}

	/**
	 * Returns height and width data in a GridData for the button that was
	 * passed in.  You can call button.setLayoutData with the returned
	 * data.
	 * 
	 * @param button which has already been made.  We'll be making the
	 * GridData for this button, so be sure that the text has already
	 * been set.
	 * @return GridData for this button with the suggested height and width
	 */
	public static GridData makeButtonData(Button button) {
		GC gc = new GC(button);
		gc.setFont(button.getFont());

		GridData data = new GridData();
		data.heightHint =
			Dialog.convertVerticalDLUsToPixels(
				gc.getFontMetrics(),
				IDialogConstants.BUTTON_HEIGHT);
		data.widthHint =
			Math.max(
				Dialog.convertHorizontalDLUsToPixels(
					gc.getFontMetrics(),
					IDialogConstants.BUTTON_WIDTH),
				button.computeSize(SWT.DEFAULT, SWT.DEFAULT, true).x);

		gc.dispose();

		return data;
	}

	/**
	 * Makes the GridData for a button to be a fixed size, regardless of
	 * the contents of the button
	 * @param button the button control that we'll make the GridData for.
	 * @return GridData the new GridData for the button control.
	 */
	public static GridData makeFixedButtonData(Button button) {
		GC gc = new GC(button);
		gc.setFont(button.getFont());

		GridData gridData = new GridData();

		gridData.widthHint =
			Dialog.convertHorizontalDLUsToPixels(
				gc.getFontMetrics(),
				IDialogConstants.BUTTON_WIDTH);

		gridData.heightHint =
			Dialog.convertVerticalDLUsToPixels(
				gc.getFontMetrics(),
				IDialogConstants.BUTTON_HEIGHT);

		gc.dispose();

		return gridData;
	}

	/**
	 * Display a message box
	 * @param message the String message for the message box
	 * @param title the String title for the message box
	 * @param swtStyle the int style for the message box
	 * @param shell the Shell for the message box, such as Display.getActive().getShell()
	 */
	public static void doMessageBox(
		String message,
		String title,
		int swtStyle,
		Shell shell) {
		MessageBox messageBox = new MessageBox(shell, swtStyle);

		//stuff inside
		messageBox.setMessage(message);

		//title
		messageBox.setText(title);
		messageBox.open();
	}

}
