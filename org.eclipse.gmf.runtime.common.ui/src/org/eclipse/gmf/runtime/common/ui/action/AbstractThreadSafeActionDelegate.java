/******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/
package org.eclipse.gmf.runtime.common.ui.action;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.gmf.runtime.common.ui.action.AbstractActionDelegate;

/**
 * Thread safe vesrion of the AbstractActionDelegate that avoids threading
 * issues when the action is run in an non UI thread.
 * 
 * @author wdiu, Wayne Diu
 * 
 * @deprecated {@link AbstractActionDelegate} has been enhanced to handle errors
 *             from either a UI or non-UI thread.
 */
public abstract class AbstractThreadSafeActionDelegate extends
		AbstractActionDelegate {

	/**
	 * Show the error UI in the display thread.
	 * 
	 * @param status
	 *            IStatus passed to superclass
	 */
	protected void openErrorDialog(final IStatus status) {
		getWorkbenchPart().getSite().getShell().getDisplay().asyncExec(
				new Runnable() {
					public void run() {
						AbstractThreadSafeActionDelegate.super
								.openErrorDialog(status);
					}
				});
	}

}
