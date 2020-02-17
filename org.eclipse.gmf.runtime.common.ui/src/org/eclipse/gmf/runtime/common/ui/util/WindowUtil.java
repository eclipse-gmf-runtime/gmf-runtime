/******************************************************************************
 * Copyright (c) 2002, 2009 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.common.ui.util;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.util.Geometry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.gmf.runtime.common.core.util.Log;
import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.common.ui.internal.CommonUIDebugOptions;
import org.eclipse.gmf.runtime.common.ui.internal.CommonUIPlugin;
import org.eclipse.gmf.runtime.common.ui.internal.CommonUIStatusCodes;

/**
 * Utilities for windows.
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
	 * Calculates the position of a window so that it does not run off the
	 * screen while taking given initLocation into account. Also, if lowerY is
	 * greater than -1 it takes that into account: if the window is moved above
	 * requested initLocation.y, than it has to be completely above lowerY. This
	 * is needed, for example, when the window is opened by clicking on the
	 * button and we don't wont the button to be hidden by this popup.
	 * 
	 * @param shell
	 *            Window shell
	 * @param initLocation
	 *            the initial location;
	 * @param lowerY
	 *            if -1, it is ignored; otherwise, if popup is moved up, it has
	 *            to be completely above lowerY
	 * @return Constrained location
	 * @since 1.2
	 */
	public static Point constrainWindowLocation(Shell shell,
			Point initLocation, int lowerY) {
		// First, find the (closest) monitor that contains the popup
		Monitor[] monitors = shell.getDisplay().getMonitors();
		int closest = Integer.MAX_VALUE;
		Monitor closestMonitor = monitors[0];
		Rectangle windowBounds = shell.getBounds();
		Point toFind = Geometry.centerPoint(windowBounds);
		for (int idx = 0; idx < monitors.length; idx++) {
			Monitor current = monitors[idx];
			Rectangle clientArea = current.getClientArea();
			if (clientArea.contains(toFind)) {
				closestMonitor = current;
				break;
			}
			int distance = Geometry.distanceSquared(Geometry
					.centerPoint(clientArea), toFind);
			if (distance < closest) {
				closest = distance;
				closestMonitor = current;
			}
		}
		Rectangle monitorBounds = closestMonitor.getClientArea();
		// Find location that ensures that popup stays within the screen.
		int windowX = Math.max(monitorBounds.x, Math.min(initLocation.x,
				monitorBounds.x + monitorBounds.width - windowBounds.width));
		int windowY = Math.max(monitorBounds.y, Math.min(initLocation.y,
				monitorBounds.y + monitorBounds.height - windowBounds.height));

		if (lowerY > -1 && windowY < initLocation.y
				&& windowY + windowBounds.height > lowerY) {
			// popup is moved up and is hiding part or all of the button (or
			// another control that launched the window);
			// make sure it is completely above the button
			windowY = lowerY - windowBounds.height;
		}
		return new Point(windowX, windowY);
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
				14);
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
				14);

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
