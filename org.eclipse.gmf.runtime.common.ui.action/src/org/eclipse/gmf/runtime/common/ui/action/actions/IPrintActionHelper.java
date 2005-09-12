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

package org.eclipse.gmf.runtime.common.ui.action.actions;

import org.eclipse.ui.IWorkbenchPart;

/**
 * An interface to implement for bringing up the print dialog.
 * 
 * See ModelerPrintActionHelper for an explanation on why I use it for print
 * preview.
 * 
 * @author Wayne Diu, wdiu
 */
public interface IPrintActionHelper {

	/**
	 * The method that brings up the print dialog
	 * 
	 * @param workbenchPart the workbench part containing the diagram to
	 * print
	 */
	public void doPrint(IWorkbenchPart workbenchPart);
}
